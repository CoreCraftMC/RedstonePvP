package com.ryderbelserion.redstonepvp.config;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.redstonepvp.config.types.Config;
import com.ryderbelserion.redstonepvp.config.types.Locale;
import java.io.File;

public class ConfigManager {

    private static SettingsManager config;

    private static SettingsManager messages;

    /**
     * Loads configuration files.
     */
    public static void load(final File dataFolder) {
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
    }

    /**
     * Refreshes configuration files.
     */
    public static void refresh() {
        config.reload();

        messages.reload();
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
}