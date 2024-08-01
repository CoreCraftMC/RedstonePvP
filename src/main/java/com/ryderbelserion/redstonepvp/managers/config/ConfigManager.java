package com.ryderbelserion.redstonepvp.managers.config;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.redstonepvp.managers.MenuManager;
import com.ryderbelserion.redstonepvp.managers.config.types.Config;
import com.ryderbelserion.redstonepvp.managers.config.types.Locale;
import com.ryderbelserion.vital.paper.files.config.FileManager;
import java.io.File;

public class ConfigManager {

    private static FileManager fileManager;

    private static SettingsManager config;

    private static SettingsManager messages;

    /**
     * Loads configuration files.
     */
    public static void load(final File dataFolder) {
        fileManager = new FileManager();

        YamlFileResourceOptions builder = YamlFileResourceOptions.builder().indentationSize(2).build();

        config = SettingsManagerBuilder
                .withYamlFile(new File(dataFolder, "config.yml"), builder)
                .useDefaultMigrationService()
                .configurationData(Config.class)
                .create();

        messages = SettingsManagerBuilder
                .withYamlFile(new File(dataFolder, "messages.yml"), builder)
                .useDefaultMigrationService()
                .configurationData(Locale.class)
                .create();

        fileManager.addFile("player-drops.yml").init();
    }

    /**
     * Refreshes configuration files.
     */
    public static void refresh() {
        config.reload();

        messages.reload();

        fileManager.reloadFiles().init();
    }

    /**
     * @return gets config.yml
     */
    public static SettingsManager getConfig() {
        return config;
    }

    /**
     * @return gets messages.yml
     */
    public static SettingsManager getMessages() {
        return messages;
    }

    /**
     * @return {@link FileManager}
     */
    public static FileManager getFileManager() {
        return fileManager;
    }
}