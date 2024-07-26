package com.ryderbelserion.redstonepvp.managers;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.objects.beacons.Beacon;
import com.ryderbelserion.redstonepvp.api.objects.beacons.BeaconDrop;
import com.ryderbelserion.redstonepvp.managers.data.Connector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.Statement;
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

    private static Map<String, ArrayList<Integer>> positions = new HashMap<>();

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

                try (PreparedStatement statement = connection.prepareStatement("insert into beacon_items(id) values (?)", Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, name);

                    statement.executeUpdate();

                    final ResultSet generatedKeys = statement.getGeneratedKeys();

                    if (generatedKeys.next()) {
                        BeaconManager.addPosition(name, generatedKeys.getInt(1));
                    }
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

                            try (PreparedStatement next = connection.prepareStatement("select weight,item from beacon_items where id = ?")) {
                                next.setString(1, drop.getName());

                                final ResultSet query = next.executeQuery();

                                while (query.next()) {
                                    float weight = query.getFloat("weight");
                                    String item = query.getString("item");

                                    final BeaconDrop beaconDrop = drop.getDrop();

                                    beaconDrop.addItem(item, weight);
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

        positions = CompletableFuture.supplyAsync(() -> {
            final Map<String, ArrayList<Integer>> positions = new HashMap<>();

            final Connector connector = dataManager.getConnector();

            try (Connection connection = connector.getConnection()) {
                if (connector.tableExists(connection, "beacon_items")) {
                    try (PreparedStatement statement = connection.prepareStatement("select position,id from beacon_items")) {
                        final ResultSet query = statement.executeQuery();

                        while (query.next()) {
                            final String id = query.getString("id");

                            final boolean hasName = positions.containsKey(id);

                            if (hasName) {
                                final ArrayList<Integer> numbers = positions.get(id);

                                numbers.add(query.getInt("position"));

                                positions.put(id, numbers);
                            } else {
                                positions.put(id, new ArrayList<>() {{
                                    add(query.getInt("position"));
                                }});
                            }
                        }
                    }
                }
            } catch (SQLException exception) {
                plugin.getComponentLogger().warn("Failed to fetch the position ids.", exception);
            }

            return positions;
        }).join();

        //todo() run tasks to start drops.
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
            positions.remove(name);

            CompletableFuture.runAsync(() -> {
                try (Connection connection = dataManager.getConnector().getConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("delete from beacon_locations where id = ?")) {
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

    /**
     * Gets a list of currently available beacon locations.
     *
     * @return the map of beacon locations.
     */
    public static Map<String, Beacon> getBeaconData() {
        return Collections.unmodifiableMap(beaconDrops);
    }

    /**
     * Checks if a position exists.
     *
     * @param id the name of the position.
     * @return true or false
     */
    public static boolean hasPosition(final String id) {
        return positions.containsKey(id);
    }

    /**
     * Adds a position to the cache.
     *
     * @param id the name of the position
     * @param position the integer we actually care about
     */
    public static void addPosition(final String id, final int position) {
        plugin.getLogger().warning("Position: " + position);
        plugin.getLogger().warning("Id: " + id);

        if (hasPosition(id)) {
            ArrayList<Integer> numbers = positions.get(id);

            numbers.add(position);

            positions.put(id, numbers);

            return;
        }

        positions.put(id, new ArrayList<>() {{
            add(position);
        }});
    }

    /**
     * Get a list of positions by id.
     *
     * @param name the name of the location
     * @return the list of positions
     */
    public static List<Integer> getPositionsById(final String name) {
        if (!hasPosition(name)) return Collections.emptyList();

        return positions.get(name);
    }

    /**
     * Remove a position from the cache.
     *
     * @param id the name of the location associated with the position
     * @param position the position to remove
     */
    public static void removePosition(final String id, final int position) {
        if (!hasPosition(id)) return;

        final List<Integer> numbers = positions.get(id);

        if (numbers.contains(position)) {
            numbers.remove(position);
        }
    }

    /**
     * Get a map of positions.
     *
     * @return the map of positions
     */
    public static Map<String, ArrayList<Integer>> getPositions() {
        return Collections.unmodifiableMap(positions);
    }
}