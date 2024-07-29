package com.ryderbelserion.redstonepvp.api.core.v2.enums;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class GuiKeys {

    private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(GuiKeys.class);

    public static NamespacedKey key = new NamespacedKey(plugin, "mf-gui");

    public static @NotNull String getName(final ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
    }
}