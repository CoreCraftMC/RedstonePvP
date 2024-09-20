package me.corecraft.redstonepvp.api.objects;

import org.bukkit.configuration.ConfigurationSection;
import java.util.Map;

public class ItemProperty {

    private final Map<String, String> placeholders;
    private final ConfigurationSection section;

    public ItemProperty(final ConfigurationSection section, final Map<String, String> placeholders) {
        this.placeholders = placeholders;
        this.section = section;
    }

    public final Map<String, String> getPlaceholders() {
        return this.placeholders;
    }

    public final ConfigurationSection getSection() {
        return this.section;
    }
}