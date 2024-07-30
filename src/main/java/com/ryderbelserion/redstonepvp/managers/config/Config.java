package com.ryderbelserion.redstonepvp.managers.config;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.ListProperty;
import ch.jalu.configme.properties.MapProperty;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.PropertyBuilder;
import ch.jalu.configme.properties.types.PrimitivePropertyType;
import ch.jalu.configme.properties.types.PropertyType;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiType;
import com.ryderbelserion.redstonepvp.managers.config.beans.ButtonProperty;
import com.ryderbelserion.redstonepvp.managers.config.beans.GuiProperty;
import com.ryderbelserion.redstonepvp.managers.config.types.PrimitiveButtonType;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import static ch.jalu.configme.properties.PropertyInitializer.newBeanProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class Config implements SettingsHolder {

    @Override
    public void registerComments(@NotNull CommentsConfiguration conf) {
        conf.setComment("combat", """
                A plethora of tweaks to change how combat feels on the server.
                """.trim());
    }

    @Comment("The menu opened via /redstonepvp")
    public static final Property<GuiProperty> main_menu = newBeanProperty(GuiProperty.class, "root.menus.main-menu", new GuiProperty(GuiType.CHEST, "<red>Redstone PvP</red>", 3));

    @Comment("The buttons for the menu opened via /redstonepvp")
    public static final MapProperty<ButtonProperty> main_menu_buttons = new PropertyBuilder.MapPropertyBuilder<>(new PrimitiveButtonType())
            .path("root.menus.buttons.main-menu")
            .defaultEntry("reload_plugin", new ButtonProperty("<yellow>Reload Plugin</yellow>", "compass", 3))
            .defaultEntry("view_beacon_drops", new ButtonProperty("<yellow>View Drops</yellow>", "beacon", 5)).build();

    @Comment("The prefix used in commands")
    public static final Property<String> command_prefix = newProperty("root.prefix", "<dark_red><b>[</b></dark_red><red>Redstone<white>PvP<dark_red><b>]</b></dark_red> <reset>");

    @Comment("Do you want verbose logs in console?")
    public static final Property<Boolean> verbose_logging = newProperty("root.verbose-logging", false);

    @Comment("The amount of particles to show. -1 is disabled!")
    public static final Property<Integer> blood_effect = newProperty("combat.blood-effect.count", 20);

    @Comment("What to set the attack frequency to, -1 is disabled!")
    public static final Property<Integer> hit_delay = newProperty("combat.attack-frequency.player-delay", 12);

    @Comment("What to set the attack speed to, 40 means no cooldown. -1 is disabled!")
    public static final Property<Integer> attack_cooldown = newProperty("combat.attack-cooldown.generic-attack-speed", 40);

    @Comment("The sounds blocked through packets, if the list is empty. that is considered disabled!")
    public static final Property<List<String>> sound_names = newListProperty("combat.attack-sounds.blocked-sounds", List.of(
            "entity.player.attack.nodamage",
            "entity.player.attack.strong",
            "entity.player.attack.sweep"
    ));

    @Comment("The material required to repair items.")
    public static final Property<String> anvil_repair_material = newProperty("feature.anvil-repair.material", "gold_ingot");

    @Comment("The amount of the material required to repair items. -1 is disabled!")
    public static final Property<Integer> anvil_repair_cost = newProperty("feature.anvil-repair.cost", 32);

    /*@Comment("The beacon manager menu.")
    public static final Property<GuiProperty> beacon_drop_menu = newBeanProperty(GuiProperty.class, "menus.beacons", new GuiProperty("<red>Beacon Manager</red>", 27));*/

    /*@Comment("The button in the main menu to view active locations.")
    public static final Property<ButtonProperty> beacon_drop_item = newBeanProperty(ButtonProperty.class, "menu.buttons.beacons",
            new ButtonProperty("<red>Current Locations</red>", "beacon", 11));*/

    /*@Comment("The online players menu.")
    public static final Property<GuiProperty> players_menu = newBeanProperty(GuiProperty.class, "menus.online-players",
            new GuiProperty("<green>Online Players</green>", 27));

    @Comment("The button in the main menu to view online players.")
    public static final Property<ButtonProperty> online_players_item = newBeanProperty(ButtonProperty.class, "menu.buttons.online-players",
            new ButtonProperty("<green>Online Players</green>", "player_head", 13));

    @Comment("The plugin settings menu.")
    public static final Property<GuiProperty> plugin_settings_menu = newBeanProperty(GuiProperty.class, "menus.plugin-settings",
            new GuiProperty("<yellow>Plugin Settings</yellow>", 27));

    @Comment("The button in the main menu to handle plugin settings.")
    public static final Property<ButtonProperty> plugin_settings_item = newBeanProperty(ButtonProperty.class, "menu.buttons.plugin-settings",
            new ButtonProperty("<yellow>Plugin Settings</yellow>", "emerald_block", 15));*/
}