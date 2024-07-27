package com.ryderbelserion.redstonepvp.api.core.builders.types.settings;

import com.ryderbelserion.redstonepvp.api.core.builders.InventoryBuilder;
import com.ryderbelserion.redstonepvp.api.enums.PersistentKeys;
import com.ryderbelserion.redstonepvp.managers.PaginationManager;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class SettingsMenu extends InventoryBuilder {

    public SettingsMenu(final Player player, final String guiName, final int guiSize, final int page) {
        super(player, guiName, guiSize, page);
    }

    public SettingsMenu() {}

    @Override
    public InventoryBuilder build() {
        final Inventory inventory = getInventory();

        final int page = PaginationManager.getPage(getPlayer());

        if (page > 1) {
            inventory.setItem(21, new ItemBuilder().withType(Material.ARROW).getStack());
        }

        final int maxPages = PaginationManager.getMaxPages(this.plugin.getItems().size(), 15);

        if (page != maxPages) {
            inventory.setItem(23, new ItemBuilder().withType(Material.ARROW).getStack());
        }

        //for (ItemStack item : PaginationManager.getPageItems(this.plugin.getItems(), getPage(), 15)) {
        //    inventory.setItem(inventory.firstEmpty(), item);
        //}

        return this;
    }

    @Override
    public void run(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        final Inventory inventory = event.getInventory();

        if (!(inventory.getHolder(false) instanceof SettingsMenu)) return;

        final ItemStack item = event.getCurrentItem();

        if (item == null) return;

        if (!item.hasItemMeta()) return;

        final ItemMeta itemMeta = item.getItemMeta();

        final PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (container.has(PersistentKeys.next_page.getNamespacedKey())) {

        }

        if (container.has(PersistentKeys.back_page.getNamespacedKey())) {

        }
    }
}