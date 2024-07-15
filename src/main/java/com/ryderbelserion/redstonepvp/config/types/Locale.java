package com.ryderbelserion.redstonepvp.config.types;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class Locale implements SettingsHolder {

    @Override
    public void registerComments(@NotNull CommentsConfiguration conf) {
        String[] header = {
                "Github: https://github.com/CoreCraftMC",
                "",
                "Issues: https://github.com/CoreCraftMC/RedstonePvP/issues",
                "Features: https://github.com/CoreCraftMC/RedstonePvP/issues",
                ""
        };

        conf.setComment("root", header);
    }

    @Comment("A list of available placeholders: {prefix}")
    public static final Property<String> reloaded_plugin = newProperty("root.reload-plugin", "{prefix}<white>You have reloaded <red>Redstone<white>PvP");

    @Comment("A list of available placeholders: {prefix}, {toggle}")
    public static final Property<String> item_frame_bypass = newProperty("root.item-frame-bypass", "{prefix}<white> Item Frame Bypass: <red>{toggle}");
}