package com.ryderbelserion.redstonepvp.api.keys;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemKeys {

    private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ItemKeys.class);

    public static NamespacedKey item_key = new NamespacedKey(plugin, "item_key");

}