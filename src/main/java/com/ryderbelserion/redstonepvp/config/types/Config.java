package com.ryderbelserion.redstonepvp.config.types;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import java.util.List;
import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class Config implements SettingsHolder {

    @Comment("What to set the attack frequency to, 8 is similar to 1.8.8. -1 is disabled!")
    public static final Property<Integer> hit_delay = newProperty("attack-frequency.player-delay", 8);

    @Comment("What to set the attack speed to, 40 means no cooldown. -1 is disabled!")
    public static final Property<Integer> attack_cooldown = newProperty("attack-cooldown.generic-attack-speed", 40);

    @Comment("The sounds blocked through packets, if the list is empty. that is considered disabled!")
    public static final Property<List<String>> sound_names = newListProperty("attack-sounds.blocked-sounds", List.of());

}