package com.ryderbelserion.redstonepvp.api.enums.keys;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author ryderbelserion
 */
public class GuiKeys {

    private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(GuiKeys.class);

    public static NamespacedKey key = new NamespacedKey(plugin, "mf-gui");

    /**
     * Get the uuid from the {@link ItemStack}.
     *
     * @param itemStack {@link ItemStack}
     * @return {@link String}
     */
    public static @NotNull String getUUID(final ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
    }

    /**
     * Builds a {@link NamespacedKey}
     *
     * @param key the key value
     * @return {@link NamespacedKey}
     */
    public static @NotNull NamespacedKey build(final String key) {
        return new NamespacedKey(plugin, key);
    }

    /**
     * Strip the {@link NamespacedKey} from the ItemStack.
     *
     * @param itemStack {@link ItemStack}
     * @return {@link ItemStack} without the {@link NamespacedKey}
     */
    public static @NotNull ItemStack strip(final ItemStack itemStack) {
        itemStack.editMeta(itemMeta -> {
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();

            if (container.has(key)) {
                container.remove(key);
            }
        });

        return itemStack;
    }
}