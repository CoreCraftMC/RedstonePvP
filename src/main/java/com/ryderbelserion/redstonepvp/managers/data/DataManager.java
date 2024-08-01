package com.ryderbelserion.redstonepvp.managers.data;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.managers.data.types.Connector;
import com.ryderbelserion.redstonepvp.managers.data.types.SqliteConnector;
import java.io.File;

public class DataManager {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    private Connector connector;

    public final DataManager init() {
        this.connector = new SqliteConnector().init(new File(this.plugin.getDataFolder(), "redstonepvp.db"));

        return this;
    }

    public final Connector getConnector() {
        return this.connector;
    }
}