package com.ryderbelserion.redstonepvp.managers.data;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SqliteConnector implements Connector {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    private HikariDataSource source;

    private File file;

    @Override
    public Connector init(File file) {
        try {
            file.createNewFile();
        } catch (IOException exception) {
            this.plugin.getComponentLogger().error("Failed to create {}", file.getName());
        } finally {
           this.file = file;
        }

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + getFile().getAbsolutePath());

        this.source = new HikariDataSource(config);

        return this;
    }

    @Override
    public void start() {

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
            this.plugin.getComponentLogger().warn("Failed to close connection!");
        }
    }

    @Override
    public final boolean isRunning() {
        try {
            return getConnection() != null && getConnection().isClosed();
        } catch (SQLException exception) {
            return false;
        }
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
}