package com.ryderbelserion.redstonepvp.managers;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.objects.BeaconDrop;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BeaconManager {

    private static final RedstonePvP plugin = RedstonePvP.getPlugin();

    private static final DataManager dataManager = plugin.getDataManager();

    private static Map<UUID, BeaconDrop> beaconDrops = new HashMap<>();

    /**
     * Adds a location to the cache and the database.
     *
     * @param uuid the uuid to identify a location
     * @param location the location to add
     */
    public static void addLocation(final UUID uuid, final String location, final int time) {
        // add to cache to ensure better performance in future checks. use a random uuid for the hashmap as we don't care what's there.
        beaconDrops.put(uuid, new BeaconDrop(uuid, location, time));

        // run off the main thread.
        CompletableFuture.runAsync(() -> {
            try (Connection connection = dataManager.getConnector().getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("insert into beacon_locations(id, location, time) values (?, ?, ?)");

                statement.setString(1, String.valueOf(uuid));
                statement.setString(2, location);
                statement.setInt(3, time);

                statement.executeUpdate();
                statement.close();
            } catch (SQLException exception) {
                plugin.getComponentLogger().warn("Failed to add {}, {}, {}", uuid, location, time);

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
            final Map<UUID, BeaconDrop> beaconDrops = new HashMap<>();

            try (Connection connection = dataManager.getConnector().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("select * from beacon_locations")) {
                    final ResultSet resultSet = statement.executeQuery();

                    while (resultSet.next()) {
                        // Get all the data from the database.
                        final BeaconDrop drop = new BeaconDrop(UUID.fromString(resultSet.getString("id")), resultSet.getString("location"), resultSet.getInt("time"));

                        // Use a random uuid() for the hashmap.
                        beaconDrops.put(drop.getUUID(), drop);
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
        UUID uuid = null;

        // Loop through current beacon drops, compare if the key matches the location. if yes, grab uuid.
        for (BeaconDrop drop : beaconDrops.values()) {
            if (drop.getKey().equalsIgnoreCase(location)) {
                uuid = drop.getUUID();

                break;
            }
        }

        // if uuid is not null, we remove from the cache.
        if (uuid != null) {
            beaconDrops.remove(uuid);
        }

        // Remove from the database as well.
        CompletableFuture.runAsync(() -> {
            try (Connection connection = dataManager.getConnector().getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("delete from beacon_locations where location = ?");

                statement.setString(1, location);
                statement.executeUpdate();

                // Close statement to clean up resources.
                statement.close();
            } catch (SQLException exception) {
                plugin.getComponentLogger().warn("Failed to delete location {}", location);

                exception.printStackTrace();
            }
        });
    }

    /**
     * Gets the active locations from either the cache or the database.
     *
     * @param queryDirectly true or false, decides whether to query the database or use the cache
     * @return a list of uuids
     */
    public static List<UUID> getLocations(final boolean queryDirectly) {
        if (!queryDirectly) {
            return beaconDrops.keySet().stream().toList();
        }

        return CompletableFuture.supplyAsync(() -> {
            List<UUID> uuids = new ArrayList<>();

            try (Connection connection = dataManager.getConnector().getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("select * from beacon_locations");

                final ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    uuids.add(UUID.fromString(resultSet.getString("id")));
                }

                // Close statement to clean up resources.
                statement.close();
            } catch (SQLException exception) {
                plugin.getComponentLogger().warn("Failed to fetch locations", exception);
            }

            return uuids;
        }).join();
    }

    /**
     * Checks if a location already exists.
     *
     * @param location the location to check
     * @param queryDirectly true or false, decides whether to query the database or use the cache
     * @return true or false
     */
    public static boolean hasLocation(final String location, final boolean queryDirectly) {
        if (!queryDirectly) {
            UUID uuid = null;

            // Loop through current beacon drops, compare if the key matches the location. if yes, grab uuid.
            for (BeaconDrop drop : beaconDrops.values()) {
                if (drop.getKey().equalsIgnoreCase(location)) {
                    uuid = drop.getUUID();

                    break;
                }
            }

            return beaconDrops.containsKey(uuid);
        }

        // Only query the database directly if in a command.
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataManager.getConnector().getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("select id from beacon_locations where location = ?");

                statement.setString(1, location);

                final ResultSet resultSet = statement.executeQuery();

                return resultSet.next();
            } catch (SQLException exception) {
                plugin.getComponentLogger().warn("Failed to fetch locations", exception);

                return false;
            }
        }).join();
    }

    public static Map<UUID, BeaconDrop> getBeaconDrops() {
        return Collections.unmodifiableMap(beaconDrops);
    }
}