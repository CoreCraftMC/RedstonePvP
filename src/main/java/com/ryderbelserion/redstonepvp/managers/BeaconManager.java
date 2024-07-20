package com.ryderbelserion.redstonepvp.managers;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.objects.beacons.Beacon;
import com.ryderbelserion.redstonepvp.managers.data.Connector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BeaconManager {

    private static final RedstonePvP plugin = RedstonePvP.getPlugin();

    private static final DataManager dataManager = plugin.getDataManager();

    private static Map<String, Beacon> beaconDrops = new HashMap<>();

    /**
     * Adds a location to the cache and the database.
     *
     * @param name the name of the location.
     * @param location the location to add
     */
    public static void addLocation(final String name, final String location, final int time) {
        // add to cache to ensure better performance in future checks. use a random uuid for the hashmap as we don't care what's there.
        beaconDrops.put(name, new Beacon(name, location, time));

        // run off the main thread.
        CompletableFuture.runAsync(() -> {
            try (Connection connection = dataManager.getConnector().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("insert into beacon_locations(id, location, time) values (?, ?, ?)")) {
                    statement.setString(1, name);
                    statement.setString(2, location);
                    statement.setInt(3, time);

                    statement.executeUpdate();
                }

                try (PreparedStatement statement = connection.prepareStatement("insert into beacon_items(id) values (?)")) {
                    statement.setString(1, name);

                    statement.executeUpdate();
                }
            } catch (SQLException exception) {
                plugin.getComponentLogger().warn("Failed to add {}, {}, {}", name, location, time);

                exception.printStackTrace();
            }
        });
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
                            final Beacon drop = new Beacon(resultSet.getString("id"), resultSet.getString("location"), resultSet.getInt("time"));

                            beaconDrops.put(drop.getName(), drop);
                        }
                    }
                }
            } catch (SQLException exception) {
                plugin.getComponentLogger().warn("Failed to populate the cache on startup.", exception);
            }

            return beaconDrops;
        }).join();

        //todo() run tasks to start drops.
    }

    /**
     * Removes a location from the cache and the database.
     *
     * @param location the location to remove
     */
    public static void removeLocation(final String location) {
        String name = null;

        // Loop through current beacon drops, compare if the key matches the location. if yes, grab uuid.
        for (Beacon drop : beaconDrops.values()) {
            if (drop.getRawLocation().equalsIgnoreCase(location)) {
                name = drop.getName();

                break;
            }
        }

        // if uuid is not null, we remove from the cache.
        if (name != null) {
            beaconDrops.remove(name);

            final String finalName = name;

            CompletableFuture.runAsync(() -> {
                try (Connection connection = dataManager.getConnector().getConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("delete from beacon_locations where id = ?")) {
                        statement.setString(1, finalName);

                        statement.executeUpdate();
                    }
                } catch (SQLException exception) {
                    plugin.getComponentLogger().warn("Failed to delete location {}", finalName);

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
                    try (final PreparedStatement statement = connection.prepareStatement("select * from beacon_locations")) {
                        final ResultSet resultSet = statement.executeQuery();

                        while (resultSet.next()) {
                            names.add(resultSet.getString("id"));
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
    public static boolean hasValue(final String name) {
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

    public static Map<String, Beacon> getBeaconData() {
        return Collections.unmodifiableMap(beaconDrops);
    }
}