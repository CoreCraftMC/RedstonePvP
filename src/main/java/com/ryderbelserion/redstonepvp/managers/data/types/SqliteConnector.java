package com.ryderbelserion.redstonepvp.managers.data.types;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class SqliteConnector implements Connector {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    private HikariDataSource source;

    private File file;

    @Override
    public final Connector init(File file) {
        try {
            file.createNewFile();
        } catch (IOException exception) {
            this.plugin.getComponentLogger().error("Failed to create {}", file.getName());
        } finally {
           this.file = file;
        }

        start();

        return this;
    }

    @Override
    public void start() {
        final HikariConfig config = new HikariConfig();

        config.setJdbcUrl(url());
        config.setMaximumPoolSize(5); // 5 is enough for flat file.
        config.setConnectionInitSql("PRAGMA foreign_keys = ON;");

        this.source = new HikariDataSource(config);

        // run off the main thread.
        CompletableFuture.runAsync(() -> {
            try (final Connection connection = getConnection()) {
                if (connection == null) return;

                try (final PreparedStatement statement = connection.prepareStatement("create table if not exists beacon_locations(id varchar(32) primary key, location varchar(64), time int)")) {
                    statement.executeUpdate();
                } catch (SQLException exception) {
                    this.plugin.getComponentLogger().warn("Failed to create beacon locations table!", exception);
                }

                try (final PreparedStatement statement = connection.prepareStatement("create table if not exists beacon_items(position integer primary key autoincrement, id varchar(32), item varchar(64), weight float, foreign key (id) references beacon_locations(id) on delete cascade)")) {
                    statement.executeUpdate();
                } catch (SQLException exception) {
                    this.plugin.getComponentLogger().warn("Failed to create beacon items table!", exception);
                }
            } catch (SQLException exception) {
                this.plugin.getComponentLogger().warn("Failed to execute statement.", exception);
            }
        });
    }

    @Override
    public void stop() {
        if (!isRunning()) return;

        try {
            final Connection connection = getConnection();

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            this.plugin.getComponentLogger().warn("Failed to close connection!", exception);
        }
    }

    @Override
    public final boolean isRunning() {
        try {
            final Connection connection = getConnection();

            return connection != null && !connection.isClosed();
        } catch (SQLException exception) {
            return false;
        }
    }

    @Override
    public final String url() {
        return "jdbc:sqlite:" + getFile().getAbsolutePath();
    }

    @Override
    public final Connection getConnection() {
        try {
            return this.source.getConnection();
        } catch (SQLException exception) {
            return null;
        }
    }

    @Override
    public final File getFile() {
        return this.file;
    }

    @Override
    public final boolean tableExists(final Connection connection, final String table) {
        try (ResultSet resultSet = connection.getMetaData().getTables(
                null,
                null,
                table,
                null
        )) {
            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();

            return false;
        }
    }
}