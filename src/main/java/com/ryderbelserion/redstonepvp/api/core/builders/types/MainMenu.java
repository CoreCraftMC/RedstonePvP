package com.ryderbelserion.redstonepvp.api.core.builders.types;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.redstonepvp.api.core.builders.InventoryBuilder;
import com.ryderbelserion.redstonepvp.api.enums.PersistentKeys;
import com.ryderbelserion.redstonepvp.managers.ConfigManager;
import com.ryderbelserion.redstonepvp.managers.PaginationManager;
import com.ryderbelserion.redstonepvp.managers.config.Config;
import com.ryderbelserion.redstonepvp.managers.config.beans.ButtonProperty;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public class MainMenu extends InventoryBuilder {

    private final SettingsManager config = ConfigManager.getConfig();

    public MainMenu(@NotNull final Player player) {
        super(player, "<red>Main Menu</red>", 27);
    }

    public MainMenu() {}

    @Override
    public InventoryBuilder build() {
        final Inventory inventory = getInventory();

        final ButtonProperty drop_item = this.config.getProperty(Config.beacon_drop_item);

        inventory.setItem(drop_item.getSlot(),
                new ItemBuilder().withType(drop_item.getMaterial())
                        .setPersistentString(PersistentKeys.beacon_item.getNamespacedKey(), "1")
                        .setDisplayName(drop_item.getName()).getStack());

        final ButtonProperty player_item = this.config.getProperty(Config.online_players_item);

        inventory.setItem(player_item.getSlot(),
                new ItemBuilder().withType(player_item.getMaterial())
                        .setPersistentString(PersistentKeys.player_item.getNamespacedKey(), "2")
                        .setDisplayName(player_item.getName()).getStack());

        final ButtonProperty settings_item = this.config.getProperty(Config.plugin_settings_item);

        inventory.setItem(settings_item.getSlot(),
                new ItemBuilder().withType(settings_item.getMaterial())
                        .setPersistentString(PersistentKeys.setting_item.getNamespacedKey(), "3")
                        .setDisplayName(settings_item.getName()).getStack());

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

        if (container.has(PersistentKeys.player_item.getNamespacedKey())) {
            player.openInventory(MiscUtils.buildPlayerMenu(player).build().getInventory());
        }

        if (container.has(PersistentKeys.setting_item.getNamespacedKey())) {
            PaginationManager.buildInventory(player, 1);
        }

        event.setCancelled(true);
    }
}