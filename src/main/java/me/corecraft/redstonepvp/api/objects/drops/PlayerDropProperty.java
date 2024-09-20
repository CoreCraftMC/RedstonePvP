package me.corecraft.redstonepvp.api.objects.drops;

import me.corecraft.redstonepvp.api.builders.ItemBuilder;
import me.corecraft.redstonepvp.api.objects.ItemProperty;
import org.bukkit.configuration.ConfigurationSection;
import java.util.List;
import java.util.Map;

public class PlayerDropProperty extends ItemProperty {

    private final ItemBuilder itemBuilder;

    private final List<String> commands;
    private final List<String> messages;
    private final double weight;
    private final int max;
    private final int min;

    public PlayerDropProperty(final ConfigurationSection section, final Map<String, String> placeholders) {
        super(section, placeholders);

        this.itemBuilder = new ItemBuilder()
                .setNamePlaceholders(placeholders) // set name placeholders
                .setLorePlaceholders(placeholders) // set lore placeholders
                .withType(section.getString("material", "gold_ingot"));

        this.commands = section.getStringList("commands");
        this.messages = section.getStringList("messages");
        this.weight = section.getDouble("weight", 5.0);
        this.max = section.getInt("max", 1);
        this.min = section.getInt("min", 2);
    }

    public final ItemBuilder getItemBuilder() {
        //todo() add debug
        if (getMin() == getMax()) return null;

        return this.itemBuilder;
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public final double getWeight() {
        return this.weight;
    }

    public final int getMax() {
        return this.max;
    }

    public final int getMin() {
        return this.min;
    }
}