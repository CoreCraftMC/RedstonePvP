package com.ryderbelserion.redstonepvp.api.core.builders.types;

import com.ryderbelserion.redstonepvp.api.core.builders.InventoryBuilder;
import com.ryderbelserion.redstonepvp.api.enums.PersistentKeys;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public class ItemMenu extends InventoryBuilder {

    private String location;

    public ItemMenu(@NotNull final Player player, String location) {
        super(player, "<red>Item Menu</red>", 45);

        this.location = location;
    }

    public ItemMenu() {}

    @Override
    public InventoryBuilder build() {
        final Inventory inventory = getInventory();

        inventory.setItem(40, new ItemBuilder().withType(Material.REDSTONE_BLOCK).setPersistentString(PersistentKeys.beacon_nuke.getNamespacedKey(), this.location).getStack());

        return this;
    }

    @Override
    public void run(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        final Inventory inventory = event.getClickedInventory();

        if (inventory == null) return;

        if (!(inventory.getHolder(false) instanceof ItemMenu)) return;

        final ItemStack itemStack = event.getCurrentItem();

        if (itemStack != null) {
            final ItemMeta itemMeta = itemStack.getItemMeta();

            final PersistentDataContainer container = itemMeta.getPersistentDataContainer();

            if (container.has(PersistentKeys.beacon_nuke.getNamespacedKey())) {
                player.sendMessage("Nuked the database.");
            }

            event.setCancelled(true);
        }
    }
}