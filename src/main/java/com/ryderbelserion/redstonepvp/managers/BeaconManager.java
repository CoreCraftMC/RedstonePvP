package com.ryderbelserion.redstonepvp.managers;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.api.objects.ItemDrop;
import com.ryderbelserion.redstonepvp.api.objects.beacons.Beacon;
import com.ryderbelserion.redstonepvp.api.objects.beacons.BeaconDrop;
import com.ryderbelserion.redstonepvp.managers.config.ConfigManager;
import com.ryderbelserion.redstonepvp.managers.config.types.Config;
import com.ryderbelserion.redstonepvp.managers.data.DataManager;
import com.ryderbelserion.redstonepvp.managers.data.types.Connector;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import com.ryderbelserion.vital.paper.util.scheduler.FoliaRunnable;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BeaconManager {

    private static final RedstonePvP plugin = RedstonePvP.getPlugin();

    private static final DataManager dataManager = plugin.getDataManager();

    private static final SettingsManager config = ConfigManager.getConfig();

    private static Map<String, Beacon> beaconDrops = new HashMap<>();

    private static final Map<String, ScheduledTask> beaconTasks = new HashMap<>();

    /**
     * Adds a location to the cache and the database.
     *2,-60,-19
     * @param name the name of the location.
     * @param location the location to add
     */
    public static void addLocation(final String name, final String location, final String time) {
        final Beacon beacon = new Beacon(name, location, time);

        // run off the main thread.
        CompletableFuture.runAsync(() -> {
            try (Connection connection = dataManager.getConnector().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("insert into beacon_locations(id, location, time) values (?, ?, ?)")) {
                    statement.setString(1, name);
                    statement.setString(2, location);
                    statement.setString(3, time);

                    statement.executeUpdate();
                }

                try (PreparedStatement statement = connection.prepareStatement("insert into beacon_items(id, weight, min, max) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, name);
                    statement.setDouble(2, 0.0);
                    statement.setInt(3, 0);
                    statement.setInt(4, 0);

                    statement.executeUpdate();

                    try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            final BeaconDrop drop = beacon.getDrop();

                            drop.addItem(null, generatedKeys.getInt(1), 0, 0, 0.0, false);
                        }
                    }
                }
            } catch (SQLException exception) {
                plugin.getComponentLogger().warn("Failed to add {}, {}, {}", name, location, time);

                exception.printStackTrace();
            }
        });

        // add to cache to ensure better performance in future checks. use a random uuid for the hashmap as we don't care what's there.
        beaconDrops.put(name, beacon);

        scheduleTask(name, beacon, MiscUtils.location(beacon.getRawLocation()));
    }

    public static void startTasks(final boolean isReload) {
        if (isReload) {
            beaconTasks.forEach((name, task) -> {
                plugin.getLogger().warning("The task for " + name + " was cancelled because the plugin reloaded.");

                task.cancel();
            });

            beaconTasks.clear();
        }

        beaconDrops.forEach((name, beacon) -> scheduleTask(name, beacon, MiscUtils.location(beacon.getRawLocation())));
    }

    /**
     * Populates the cache on startup from the database.
     *
     * @param dataManager {@link DataManager}
     */
    public static void populate(final DataManager dataManager) {
        beaconDrops = CompletableFuture.supplyAsync(() -> {
            final Map<String, Beacon> beaconDrops = new HashMap<>();

            final Connector connector = dataManager.getConnector();

            try (Connection connection = connector.getConnection()) {
                if (connector.tableExists(connection, "beacon_locations")) {
                    try (PreparedStatement statement = connection.prepareStatement("select * from beacon_locations")) {
                        final ResultSet resultSet = statement.executeQuery();

                        while (resultSet.next()) {
                            final Beacon drop = new Beacon(resultSet.getString("id"), resultSet.getString("location"), resultSet.getString("time"));

                            try (PreparedStatement next = connection.prepareStatement("select * from beacon_items where id=?")) {
                                next.setString(1, drop.getName());

                                final ResultSet query = next.executeQuery();

                                while (query.next()) {
                                    float weight = query.getFloat("weight");
                                    String item = query.getString("item");
                                    int position = query.getInt("position");
                                    int min = query.getInt("min");
                                    int max = query.getInt("max");

                                    final BeaconDrop beaconDrop = drop.getDrop();

                                    beaconDrop.addItem(item, position, min, max, weight, false);
                                }
                            }

                            beaconDrops.put(drop.getName(), drop);
                        }
                    }
                }
            } catch (SQLException exception) {
                plugin.getComponentLogger().warn("Failed to populate the cache on startup.", exception);
            }

            return beaconDrops;
        }).join();
    }

    /**
     * Removes a location from the cache and the database.
     *
     * @param name the name of the location
     */
    public static void removeLocation(final String name) {
        // if name is not null, we remove from the cache.
        if (name != null) {
            beaconDrops.remove(name);

            CompletableFuture.runAsync(() -> {
                try (Connection connection = dataManager.getConnector().getConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("delete from beacon_locations where id=?")) {
                        statement.setString(1, name);

                        statement.executeUpdate();
                    }
                } catch (SQLException exception) {
                    plugin.getComponentLogger().warn("Failed to delete location {}", name);

                    exception.printStackTrace();
                }
            });
        }
    }

    /**
     * Gets the active locations from either the cache or the database.
     *
     * @param queryDirectly true or false, decides whether to query the database or use the cache
     * @return a list of uuids
     */
    public static List<String> getLocations(final boolean queryDirectly) {
        if (queryDirectly) {
            return CompletableFuture.supplyAsync(() -> {
                List<String> names = new ArrayList<>();

                try (Connection connection = dataManager.getConnector().getConnection()) {
                    try (final PreparedStatement statement = connection.prepareStatement("select id from beacon_locations")) {
                        final ResultSet resultSet = statement.executeQuery();

                        while (resultSet.next()) {
                            names.add(resultSet.getString(1));
                        }
                    }
                } catch (SQLException exception) {
                    plugin.getComponentLogger().warn("Failed to fetch locations", exception);
                }

                return names;
            }).join();
        }

        return beaconDrops.keySet().stream().toList();
    }

    /**
     * Updates the beacon drop time.
     *
     * @param name name of the drop location
     * @param time the new time
     * @param updateDirectly true or false
     */
    public static void updateBeaconTime(final String name, final String time, final boolean updateDirectly) {
        if (updateDirectly) {
            CompletableFuture.runAsync(() -> {
                try (Connection connection = dataManager.getConnector().getConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("update beacon_locations set time=? where id=?")) {
                        statement.setString(1, time);
                        statement.setString(2, name);

                        statement.executeUpdate();
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });
        }

        final Beacon beacon = getBeacon(name);

        beacon.setTime(time);

        beaconDrops.put(name, beacon);

        final ScheduledTask task = beaconTasks.get(name);

        if (task != null) {
            // cancel old task
            task.cancel();

            beaconTasks.remove(name);

            // schedule new task
            scheduleTask(name, beacon, MiscUtils.location(beacon.getRawLocation()));
        }
    }

    /**
     * Schedule a task for the beacon drop
     *
     * @param name name of the beacon drop location
     * @param beacon beacon drop of the name
     * @param location location of the beacon drop
     */
    public static void scheduleTask(final String name, final Beacon beacon, final Location location) {
        final Block block = location.clone().add(0.0, 2, 0.0).getBlock();

        beaconTasks.put(name, new FoliaRunnable(plugin.getServer().getRegionScheduler(), location) {
            @Override
            public void run() {
                final Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();

                final int requirement = config.getProperty(Config.beacon_drop_party_required_players);

                if (requirement != -1) {
                    if (players.size() < requirement) {
                        Messages.beacon_drop_party_not_enough_players.broadcast();

                        return;
                    }
                }

                //todo() calender task

                runAnimation(block, beacon, location);
            }
        }.runAtFixedRate(plugin, 0, 20));
    }

    public static Calendar getBeaconCooldown(final String name) {
        return MiscUtils.getTimeFromString(getBeacon(name).getTime());
    }

    public static void runAnimation(final Block block, final Beacon beacon, final Location location) {
        final Block water = block.getLocation().clone().add(0.0, 1, 0.0).getBlock();

        final BlockData blockData = block.getBlockData();

        final List<ItemDrop> drops = beacon.getDrop().getItemDrops();

        new FoliaRunnable(plugin.getServer().getRegionScheduler(), location) {
            int counter = 0;

            @Override
            public void run() {
                // Event is cancelled.
                if (isCancelled()) {
                    return;
                }

                // cancel if drops empty.
                //todo() reset calendar timer
                if (drops.isEmpty()) {
                    cancel();
                }

                // 0 seconds.
                if (this.counter == 0) {
                    MiscUtils.playSound(location, Sound.ENTITY_GENERIC_EXPLODE);

                    Messages.beacon_drop_party_started.broadcast();
                }

                // less than 1 second.
                if (this.counter <= 10) {
                    MiscUtils.playSound(location, Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);

                    MiscUtils.getDrop(location, drops);
                }

                // 1.1 second.
                if (this.counter == 11) {
                    MiscUtils.playSound(location, Sound.ENTITY_GENERIC_EXPLODE);

                    water.setType(Material.WATER, true);
                }

                // 1.5 seconds.
                if (this.counter == 15) {
                    water.setType(Material.AIR, true);

                    if (blockData instanceof Waterlogged waterlogged) {
                        waterlogged.setWaterlogged(false);
                    }
                }

                // start phase 2 at 3 seconds.
                if (this.counter >= 30 && this.counter <= 40) {
                    MiscUtils.playSound(location, Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);

                    MiscUtils.getDrop(location, drops);
                }

                // spawn water on top of the slab to push items out when it reaches 4.1 seconds.
                if (this.counter == 41) {
                    MiscUtils.playSound(location, Sound.ENTITY_GENERIC_EXPLODE);

                    water.setType(Material.WATER, true);
                }

                // remove water at 4.5 seconds.
                if (this.counter == 45) {
                    water.setType(Material.AIR, true);

                    if (blockData instanceof Waterlogged waterlogged) {
                        waterlogged.setWaterlogged(false);
                    }
                }

                // start phase 2 at 5.5 seconds.
                if (this.counter >= 55 && this.counter <= 65) {
                    MiscUtils.playSound(location, Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);

                    MiscUtils.getDrop(location, drops);
                }

                // spawn water on top of the slab to push items out when it reaches 6.6 seconds.
                if (this.counter == 66) {
                    MiscUtils.playSound(location, Sound.ENTITY_GENERIC_EXPLODE);

                    water.setType(Material.WATER, true);
                }

                if (this.counter >= 69) {
                    water.setType(Material.AIR, true);

                    if (blockData instanceof Waterlogged waterlogged) {
                        waterlogged.setWaterlogged(false);
                    }

                    Messages.beacon_drop_party_stopped.broadcast();

                    cancel();
                }

                this.counter++;
            }
        }.runAtFixedRate(plugin, 0, 10);
    }

    /**
     * Gets a beacon drop object
     *
     * @param name the name of the drop location
     * @return the {@link Beacon}
     */
    public static Beacon getBeacon(final String name) {
        return beaconDrops.get(name);
    }

    /**
     * Checks if the name already taken.
     *
     * @param name the name of the location.
     * @return true or false
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasBeacon(final String name) {
        return beaconDrops.containsKey(name);
    }

    /**
     * Checks if a location already exists.
     *
     * @param location the location to check
     * @return true or false
     */
    public static boolean hasLocation(final String location) {
        for (Beacon drop : beaconDrops.values()) {
            if (location.equalsIgnoreCase(drop.getRawLocation())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets a list of currently available beacon locations.
     *
     * @return the map of beacon locations.
     */
    public static Map<String, Beacon> getBeaconData() {
        return Collections.unmodifiableMap(beaconDrops);
    }
}