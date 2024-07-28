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
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@SuppressWarnings("DataFlowIssue")
public class SettingsMenu extends InventoryBuilder {

    private final List<ItemStack> itemStacks;

    public SettingsMenu(final Player player, final String guiName, final int guiSize, final int page) {
        super(player, guiName, guiSize, page);

        this.itemStacks = this.plugin.getItems();
    }

    public SettingsMenu() {
        this.itemStacks = this.plugin.getItems();
    }

    @Override
    public InventoryBuilder build() {
        final Inventory inventory = getInventory();

        final int page = PaginationManager.getPage(getPlayer());

        if (page > 1) {
            inventory.setItem(21, new ItemBuilder().withType(Material.ARROW).setPersistentInteger(PersistentKeys.back_page.getNamespacedKey(), page - 1).getStack());
        }

        final int maxPages = PaginationManager.getMaxPages(this.itemStacks.size(), getMax());

        if (page != maxPages) {
            inventory.setItem(23, new ItemBuilder().withType(Material.ARROW).setPersistentInteger(PersistentKeys.next_page.getNamespacedKey(), page + 1).getStack());
        }

        for (ItemStack item : PaginationManager.getPageItems(this.itemStacks, page, getMax())) {
            final int nextSlot = inventory.firstEmpty();

            if (nextSlot >= 0) {
                inventory.setItem(nextSlot, item);
            } else {
                break;
            }
        }

        return this;
    }

    @Override
    public void run(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        final Inventory inventory = event.getInventory();

        if (!(inventory.getHolder(false) instanceof SettingsMenu)) return;

        event.setCancelled(true);

        final ItemStack item = event.getCurrentItem();

        if (item == null) return;

        if (!item.hasItemMeta()) return;

        final ItemMeta itemMeta = item.getItemMeta();

        final PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (container.has(PersistentKeys.next_page.getNamespacedKey())) {
            int currentPage = container.get(PersistentKeys.next_page.getNamespacedKey(), PersistentDataType.INTEGER);

            PaginationManager.nextPage(player, currentPage, 18, this.itemStacks.size());
        }

        if (container.has(PersistentKeys.back_page.getNamespacedKey())) {
            int currentPage = container.get(PersistentKeys.back_page.getNamespacedKey(), PersistentDataType.INTEGER);

            PaginationManager.backPage(player, currentPage, 18, this.itemStacks.size());
        }
    }
}