package me.corecraft.redstonepvp.api.objects.drops;

import me.corecraft.redstonepvp.api.builders.ItemBuilder;
import me.corecraft.redstonepvp.api.objects.property.ItemProperty;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ItemDrop extends ItemProperty {

    private ItemBuilder itemBuilder;

    private final List<String> commands;
    private final List<String> messages;
    private final double weight;
    private final int max;
    private final int min;

    public ItemDrop(final ConfigurationSection section, final Map<String, String> placeholders) {
        super(section, placeholders);

        this.itemBuilder = new ItemBuilder().setNamePlaceholders(placeholders).setLorePlaceholders(placeholders).withType(section.getString("material", "gold_ingot"));

        this.commands = section.getStringList("commands");
        this.messages = section.getStringList("messages");
        this.weight = section.getDouble("weight", 5.0);
        this.max = section.getInt("max", 1);
        this.min = section.getInt("min", 2);
    }

    public ItemDrop(final Material material, final int min, final int max, final double weight) {
        super(null, null);

        this.itemBuilder = new ItemBuilder().withType(material).setAmount(ThreadLocalRandom.current().nextInt(min, max));

        this.commands = null;
        this.messages = null;

        this.weight = weight;
        this.min = min;
        this.max = max;
    }

    public ItemDrop(@Nullable final String item, final int min, final int max, final double weight) {
        super(null, null);

        if (item != null) {
            this.itemBuilder = new ItemBuilder().fromBase64(item).setAmount(ThreadLocalRandom.current().nextInt(min, max));
        }

        this.commands = null;
        this.messages = null;

        this.weight = weight;
        this.min = min;
        this.max = max;
    }

    public final ItemStack getItemStack() {
        if (getMin() == getMax() || this.itemBuilder == null) return null;

        return this.itemBuilder.asItemStack();
    }

    public final ItemBuilder getItemBuilder() {
        if (getMin() == getMax() || this.itemBuilder == null) return null;

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