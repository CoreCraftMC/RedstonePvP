package com.ryderbelserion.redstonepvp.api.enums;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Files {

    player_drops("player-drops.yml");

    private final String fileName;

    Files(final String fileName) {
        this.fileName = fileName;
    }

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    public final YamlConfiguration getConfiguration() {
        return this.plugin.getFileManager().getFile(this.fileName).getConfiguration();
    }
}