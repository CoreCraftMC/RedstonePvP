package com.ryderbelserion.redstonepvp.api.objects.beacons;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.redstonepvp.managers.data.types.Connector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BeaconDrop {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();
    private final Connector connector = this.plugin.getDataManager().getConnector();

    private final Map<String, Double> items = new HashMap<>();

    private final String name;

    public BeaconDrop(final String name) {
        this.name = name;
    }

    public void addItem(final String item, final double weight) {
        addItem(item, 1, weight, false);
    }

    public void addItem(final String item, final int position, final double weight, final boolean insertData) {
        this.items.put(item, weight);

        if (insertData) {
            CompletableFuture.runAsync(() -> {
                try (Connection connection = this.connector.getConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("update beacon_items set item = ?, weight = ? where position = ?")) {
                        statement.setString(1, item);
                        statement.setDouble(2, weight);
                        statement.setInt(3, position);

                        statement.executeUpdate();
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });
        }
    }

    public void setItem(final String name, final String item, final double weight, final boolean insertData) {
        this.items.put(item, weight);

        if (insertData) {
            CompletableFuture.runAsync(() -> {
                try (Connection connection = this.connector.getConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("insert into beacon_items(id, item, weight) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                        statement.setString(1, name);
                        statement.setString(2, item);
                        statement.setDouble(3, weight);

                        statement.executeUpdate();

                        final ResultSet generatedKeys = statement.getGeneratedKeys();

                        if (generatedKeys.next()) {
                            BeaconManager.addPosition(name, generatedKeys.getInt(1));
                        }
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });
        }
    }

    public boolean containsItem(final String item) {
        return this.items.containsKey(item);
    }

    public void removeItem(final String item) {
        this.items.remove(item);

        CompletableFuture.runAsync(() -> {
            try (Connection connection = this.connector.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("delete from beacon_items where item = ? returning position,id")) {
                    statement.setString(1, item);

                    try (ResultSet generatedKeys = statement.executeQuery()) {
                        if (generatedKeys.next()) {
                            BeaconManager.removePosition(generatedKeys.getString("id"), generatedKeys.getInt("position"));
                        }
                    }
                }
            } catch (SQLException exception) {
                plugin.getComponentLogger().warn("Failed to delete item {}", item);

                exception.printStackTrace();
            }
        });
    }

    public final Map<String, Double> getItems() {
        return Collections.unmodifiableMap(this.items);
    }

    public final String getName() {
        return this.name;
    }
}