package com.ryderbelserion.redstonepvp.api.objects;

import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import java.util.concurrent.ThreadLocalRandom;

public class ItemDrop {

    private final ItemBuilder itemBuilder;
    private final int min;
    private final int max;
    private final double weight;

    public ItemDrop(final ConfigurationSection section) {
        this.itemBuilder = new ItemBuilder().withType(section.getString("material", "gold_ingot"));

        this.min = section.getInt("min", 1);
        this.max = section.getInt("max", 3);

        this.weight = section.getDouble("weight", 10.0);
    }

    public ItemDrop(Material material, int min, int max, double weight) {
        this.itemBuilder = new ItemBuilder().withType(material);

        this.min = min;
        this.max = max;

        this.weight = weight;
    }

    /**
     * @return {@link ItemStack}
     */
    public final ItemStack getItem() {
        return this.itemBuilder.setAmount(ThreadLocalRandom.current().nextInt(this.min, this.max)).getStack();
    }

    /**
     * @return the min amount
     */
    public final int getMin() {
        return this.min;
    }

    /**
     * @return the max amount
     */
    public final int getMax() {
        return this.max;
    }

    /**
     * @return the weight
     */
    public final double getWeight() {
        return this.weight;
    }
}