package com.ryderbelserion.redstonepvp.api.enums;

import com.ryderbelserion.redstonepvp.managers.config.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

public enum Files {

    player_drops("player-drops.yml");

    private final String fileName;

    Files(final String fileName) {
        this.fileName = fileName;
    }

    public final FileConfiguration getConfiguration() {
        return ConfigManager.getFileManager().getFile(this.fileName);
    }
}