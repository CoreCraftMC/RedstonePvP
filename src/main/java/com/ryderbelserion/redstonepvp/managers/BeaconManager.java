package com.ryderbelserion.redstonepvp.managers;

import com.ryderbelserion.redstonepvp.RedstonePvP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BeaconManager {

    private static final RedstonePvP plugin = RedstonePvP.getPlugin();

    private static final DataManager dataManager = plugin.getDataManager();

    public static void addLocation(final UUID uuid, final String location) {
        // run off the main thread.
        CompletableFuture.runAsync(() -> {
            try (Connection connection = dataManager.getConnector().getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("insert into beacon_locations(id, location) values (?, ?)");

                statement.setString(1, String.valueOf(uuid));
                statement.setString(2, location);

                statement.executeUpdate();
            } catch (SQLException exception) {
                plugin.getComponentLogger().warn("Failed to add {}, {}", uuid, location);

                exception.printStackTrace();
            }
        });
    }

    public static void removeLocation(final String location) {
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

    public static List<UUID> getLocations() {
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

    public static boolean hasLocation(final String location) {
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
}