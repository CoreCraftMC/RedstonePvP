package com.ryderbelserion.redstonepvp.api.objects.beacons;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.objects.ItemDrop;
import com.ryderbelserion.redstonepvp.managers.data.types.Connector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BeaconDrop {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();
    private final Connector connector = this.plugin.getDataManager().getConnector();

    private final Map<String, ItemDrop> items = new HashMap<>();

    private final Map<String, Integer> positions = new HashMap<>();

    private final String name;

    /**
     * Creates a new beacon drop which represents the items.
     *
     * @param name the name of the beacon drop
     */
    public BeaconDrop(final String name) {
        this.name = name;
    }

    /**
     * Checks if the cache contains the position
     *
     * @param position the position number
     * @return true or false
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public final boolean hasPosition(final int position) {
        return this.positions.containsValue(position);
    }

    /**
     * Updates an item in the cache/database.
     *
     * @param position position of the item
     * @param item date for the item
     * @param weight weight of the item
     * @param insertData true or false
     */
    public void addItem(final String item, final int position, final int min, final int max, final double weight, final boolean insertData) {
        final ItemDrop itemDrop = new ItemDrop(item, min, max, weight);

        this.items.put(item, itemDrop);
        this.positions.put(item, position);

        if (insertData) {
            CompletableFuture.runAsync(() -> {
                try (Connection connection = this.connector.getConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("update beacon_items set item = ?, weight = ?, min = ?, max = ? where position = ?")) {
                        statement.setString(1, item);
                        statement.setDouble(2, weight);
                        statement.setInt(3, min);
                        statement.setInt(4, max);
                        statement.setInt(5, position);

                        statement.executeUpdate();
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });
        }
    }

    /**
     * Sets an item to the cache/database.
     *
     * @param name name of the drop location
     * @param item data for the item
     * @param weight weight of the item
     * @param insertData true or false
     */
    public void setItem(final String name, final String item, final int min, final int max, final double weight, final boolean insertData) {
        this.items.put(item, new ItemDrop(item, min, max, weight));

        if (insertData) {
            CompletableFuture.runAsync(() -> {
                try (Connection connection = this.connector.getConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("insert into beacon_items(id, item, weight) values (?, ?, ?) returning position")) {
                        statement.setString(1, name);
                        statement.setString(2, item);
                        statement.setDouble(3, weight);

                        try (final ResultSet generatedKeys = statement.executeQuery()) {
                            if (generatedKeys.next()) {
                                this.positions.put(item, generatedKeys.getInt(1));
                            }
                        }
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });
        }
    }

    /**
     * Checks if the cache contains an item.
     *
     * @param item the item to remove
     * @return true or false
     */
    public final boolean hasItem(final String item) {
        return this.items.containsKey(item);
    }

    /**
     * Removes an item from the hashmaps and database.
     *
     * @param item the item to remove
     */
    public void removeItem(final String item) {
        this.items.remove(item);
        this.positions.remove(item);

        CompletableFuture.runAsync(() -> {
            try (Connection connection = this.connector.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("delete from beacon_items where item = ?")) {
                    statement.setString(1, item);

                    statement.executeUpdate();
                }
            } catch (SQLException exception) {
                plugin.getComponentLogger().warn("Failed to delete item {}", item);

                exception.printStackTrace();
            }
        });
    }

    /**
     * Remove item by position
     *
     * @param position the position
     */
    public void removeItem(final int position) {
        String item = "";

        for (Map.Entry<String, Integer> entry : this.positions.entrySet()) {
            if (position == entry.getValue()) {
                item = entry.getKey();

                break;
            }
        }

        removeItem(item);
    }

    /**
     * Gets the position by using the item
     *
     * @param item the item string
     * @return a number
     */
    public final int getPosition(final String item) {
        return this.positions.get(item);
    }

    /**
     * An unmodifiable map of the current positions.
     *
     * @return map of the current positions
     */
    public final Map<String, Integer> getPositions() {
        return Collections.unmodifiableMap(this.positions);
    }

    /**
     * An unmodifiable map of the current items.
     *
     * @return map of the current items
     */
    public final Map<String, ItemDrop> getItems() {
        return Collections.unmodifiableMap(this.items);
    }

    /**
     * Gets the name of the drop location.
     *
     * @return the name of the location
     */
    public final String getName() {
        return this.name;
    }
}