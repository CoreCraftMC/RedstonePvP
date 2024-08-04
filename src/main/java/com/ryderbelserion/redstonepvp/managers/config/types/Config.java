package com.ryderbelserion.redstonepvp.managers.config.types;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class Config implements SettingsHolder {

    @Override
    public void registerComments(@NotNull CommentsConfiguration conf) {
        conf.setComment("combat", """
                A plethora of tweaks to change how combat feels on the server.
                """.trim());
    }

    @Comment("The prefix used in commands")
    public static final Property<String> command_prefix = newProperty("root.prefix", "<dark_red><b>[</b></dark_red><red>Redstone<white>PvP<dark_red><b>]</b></dark_red> <reset>");

    @Comment("Do you want verbose logs in console?")
    public static final Property<Boolean> verbose_logging = newProperty("root.verbose-logging", false);

    @Comment("The name of the main menu for /redstonepvp")
    public static final Property<String> main_menu_name = newProperty("root.main-menu-name", "main-menu");

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

    @Comment("How many players are required for a drop party to begin? -1 implies it will wait until at least one player is on.")
    public static final Property<Integer> beacon_drop_party_required_players = newProperty("feature.drop-party.required-players", 5);
}