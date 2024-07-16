package com.ryderbelserion.redstonepvp.managers.config;

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

    @Comment("A list of available placeholders: {prefix}")
    public static final Property<String> not_a_player = newProperty("root.not-a-player", "{prefix}<white>You must be a player to run this command.");

    @Comment("A list of available placeholders: {prefix}")
    public static final Property<String> no_item_in_hand = newProperty("root.no-item-in-hand", "{prefix}<white>You have no item or valid item in hand.");

    @Comment("A list of available placeholders: {prefix}, {toggle}")
    public static final Property<String> item_frame_bypass = newProperty("root.item-frame-bypass", "{prefix}<white> Item Frame Bypass: <red>{toggle}");

    @Comment("A list of available placeholders: {prefix}, {type}, {amount}")
    public static final Property<String> anvil_repair_not_enough = newProperty("root.anvil-repair.not-enough", "{prefix}<white>Not enough {type}, you need {amount} {type}");

    @Comment("A list of available placeholders: {prefix}")
    public static final Property<String> anvil_repair_no_damage = newProperty("root.anvil-repair.no-damage", "{prefix}<white>The item you are trying to repair has no damage.");

    @Comment("A list of available placeholders: {prefix}")
    public static final Property<String> anvil_repair_not_valid = newProperty("root.anvil-repair.not-valid", "{prefix}<white>Cannot repair this item as it has no durability.");
}