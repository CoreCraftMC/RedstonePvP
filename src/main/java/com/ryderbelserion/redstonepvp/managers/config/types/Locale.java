package com.ryderbelserion.redstonepvp.managers.config.types;

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
                "",
                "All messages allow the use of {prefix} unless stated otherwise.",
                ""
        };

        conf.setComment("root", header);
    }

    @Comment("A list of available placeholders: {command}")
    public static final Property<String> unknown_command = newProperty("root.unknown-command", "{prefix}<red>{command} <white>is not a known command.");

    @Comment("A list of available placeholders: {usage}")
    public static final Property<String> correct_usage = newProperty("root.correct-usage", "{prefix}<white>The correct usage for this command is <red>{usage}");

    public static final Property<String> no_permission = newProperty("root.no-permission", "{prefix}<white>You do not have permission to use that command!");

    public static final Property<String> reloaded_plugin = newProperty("root.reload-plugin", "{prefix}<white>You have reloaded <red>Redstone<white>PvP");

    public static final Property<String> must_be_console_sender = newProperty("root.not-console-sender", "{prefix}<white>You must be using console to use this command.");

    public static final Property<String> not_a_player = newProperty("root.not-a-player", "{prefix}<white>You must be a player to run this command.");

    @Comment("A list of available placeholders: {name}")
    public static final Property<String> player_not_found = newProperty("root.player-not-found", "{prefix}<white>The player <red>{name} <white>was not found.");

    public static final Property<String> not_a_block = newProperty("root.not-a-block", "{prefix}<white>That is not a block.");

    public static final Property<String> no_item_in_hand = newProperty("root.no-item-in-hand", "{prefix}<white>You have no item or valid item in hand.");

    @Comment("A list of available placeholders: {toggle}")
    public static final Property<String> item_frame_bypass = newProperty("root.item-frame-bypass", "{prefix}<white> Item Frame Bypass: <red>{toggle}");

    @Comment("A list of available placeholders: {type}, {amount}")
    public static final Property<String> anvil_repair_not_enough = newProperty("root.anvil-repair.not-enough", "{prefix}<white>Not enough <red>{type},<white> you need <red>{amount} {type}");

    public static final Property<String> anvil_repair_no_damage = newProperty("root.anvil-repair.no-damage", "{prefix}<white>The item you are trying to repair has no damage.");

    public static final Property<String> anvil_repair_not_valid = newProperty("root.anvil-repair.not-valid", "{prefix}<white>Cannot repair this item as it has no durability.");

    @Comment("A list of available placeholders: {name}")
    public static final Property<String> menu_not_found = newProperty("root.menu-not-found", "{prefix}<white>Menu named <red>{name} <white>could not be found.");

    @Comment("A list of available placeholders: {name}")
    public static final Property<String> beacon_drop_party_added = newProperty("root.feature.drop-party.location.added", "{prefix}<white>You have added a new beacon drop location with the name <red>{name}.");

    @Comment("A list of available placeholders: {name}")
    public static final Property<String> beacon_drop_party_exists = newProperty("root.feature.drop-party.location.exists", "{prefix}<red>{name} <white>is already a beacon drop location.");

    @Comment("A list of available placeholders: {name}")
    public static final Property<String> beacon_drop_party_removed = newProperty("root.feature.drop-party.location.removed", "{prefix}<white>You have removed a beacon drop location with the name <red>{name}.");

    @Comment("A list of available placeholders: {time}, {name}")
    public static final Property<String> beacon_drop_party_time_updated = newProperty("root.feature.drop-party.location.time-updated", "{prefix}<white>The time has been updated to <red>{time} <white>for <red>{name}");

    @Comment("A list of available placeholders: {name}")
    public static final Property<String> beacon_drop_party_doesnt_exist = newProperty("root.feature.drop-party.location.doesnt-exist", "{prefix}<white>There is no beacon drop with the name {name}.");

    @Comment("A list of available placeholders: {x}, {z}")
    public static final Property<String> beacon_drop_party_started = newProperty("root.feature.drop-party.started", "{prefix}<white>A drop party has started @ {x},{z}!");

    @Comment("A list of available placeholders: {time}")
    public static final Property<String> beacon_drop_party_stopped = newProperty("root.feature.drop-party.stopped", "{prefix}<white>The drop party has ended! Come back in {time}.");

    @Comment("A list of available placeholders: {x}, {z}")
    public static final Property<String> beacon_drop_party_countdown = newProperty("root.feature.drop-party.countdown", "{prefix}<white>The drop party will start shortly @ {x},{z}!");

    public static final Property<String> beacon_drop_party_not_enough_players = newProperty("root.feature.drop-party.not-enough-players", "{prefix}<white>Not enough players online to start a drop party.");

    public static final Property<String> beacon_drop_exists = newProperty("root.feature.drop-party.drops.exists", "{prefix}<white>The item you are holding is already added.");

    @Comment("A list of available placeholders: {name}, {position}")
    public static final Property<String> beacon_drop_doesnt_exist = newProperty("root.feature.drop-party.drops.doesnt-exist", "{prefix}<white>Position <red>#{position}<white> that you trying to update doesn't exist for the drop location <red>{name}.");

    @Comment("A list of available placeholders: {name}, {position}")
    public static final Property<String> beacon_drop_added = newProperty("root.feature.drop-party.drops.added", "{prefix}<white>You have updated the item with the position <red>{position}<white> for the drop location <red>{name}");

    @Comment("A list of available placeholders: {name}")
    public static final Property<String> beacon_drop_set = newProperty("root.feature.drop-party.drops.set", "{prefix}<white>You have added a new item to the drop location <red>{name}");

    @Comment("A list of available placeholders: {name}, {position}")
    public static final Property<String> beacon_drop_removed = newProperty("root.feature.drop-party.drops.removed", "{prefix}<white>You have removed the item from the drop location <red>{name}");
}