package com.ryderbelserion.redstonepvp.api.core.builders.types;

import com.ryderbelserion.redstonepvp.api.core.builders.InventoryBuilder;
import com.ryderbelserion.redstonepvp.api.enums.PersistentKeys;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public class MainMenu extends InventoryBuilder {

    public MainMenu(@NotNull final Player player) {
        super(player, "<red>Main Menu</red>", 27);
    }

    public MainMenu() {}

    @Override
    public InventoryBuilder build() {
        final Inventory inventory = getInventory();

        inventory.setItem(10, new ItemBuilder().withType(Material.BEACON).setPersistentString(PersistentKeys.beacon_item.getNamespacedKey(), "1").setDisplayName("<red>Current Locations</red>").getStack());
        inventory.setItem(14, new ItemBuilder().withType(Material.PLAYER_HEAD).setSkull(getPlayer().getUniqueId()).setDisplayName("<green>Online Players</green>").getStack());
        inventory.setItem(12, new ItemBuilder().withType(Material.EMERALD).setDisplayName("<yellow>Plugin Settings</yellow>").getStack());
        inventory.setItem(16, new ItemBuilder().withType(Material.ENDER_CHEST).setDisplayName("<blue>Plugin Reload</blue>").getStack());

        return this;
    }

    @Override
    public void run(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        final Inventory inventory = event.getInventory();

        if (!(inventory.getHolder(false) instanceof MainMenu)) return;

        final ItemStack itemStack = event.getCurrentItem();

        if (itemStack == null || !itemStack.hasItemMeta()) return;

        final ItemMeta itemMeta = itemStack.getItemMeta();

        final PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (container.has(PersistentKeys.beacon_item.getNamespacedKey())) {
            player.openInventory(MiscUtils.buildBeaconMenu(player).build().getInventory());
        }

        event.setCancelled(true);
    }
}