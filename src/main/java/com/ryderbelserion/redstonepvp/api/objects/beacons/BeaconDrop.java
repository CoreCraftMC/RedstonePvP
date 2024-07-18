package com.ryderbelserion.redstonepvp.api.objects.beacons;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.managers.data.Connector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        this.items.put(item, weight);

        CompletableFuture.runAsync(() -> {
            try (Connection connection = this.connector.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("update beacon_items set item = ?, weight = ? where id = ?")) {
                    statement.setString(1, item);
                    statement.setDouble(2, weight);
                    statement.setString(3, this.name);

                    statement.executeUpdate();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public boolean containsItem(final String item) {
        return this.items.containsKey(item);
    }

    public void removeItem(final String item) {
        this.items.remove(item);

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

    public Map<String, Double> getItems() {
        return Collections.unmodifiableMap(this.items);
    }
}