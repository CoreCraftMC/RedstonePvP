package com.ryderbelserion.redstonepvp.managers.data;

import java.io.File;
import java.sql.Connection;

public interface Connector {

    Connector init(File file);

    void start();

    void stop();

    boolean isRunning();

    String url();

    Connection getConnection();

    File getFile();

    boolean tableExists(Connection connection, String table);

}