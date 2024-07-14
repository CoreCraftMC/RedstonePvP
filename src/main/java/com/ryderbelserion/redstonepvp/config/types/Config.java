package com.ryderbelserion.redstonepvp.config.types;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class Config implements SettingsHolder {

    @Comment("The hit delay to apply.")
    public static final Property<Integer> hit_delay = newProperty("hit-delay", 8);

}