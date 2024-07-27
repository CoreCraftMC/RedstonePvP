package com.ryderbelserion.redstonepvp.api.core.builders.types.settings;

import com.ryderbelserion.redstonepvp.api.core.builders.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class SettingsMenu extends InventoryBuilder {

    public SettingsMenu(final Player player, final String guiName, final int guiSize) {
        super(player, guiName, guiSize);
    }

    public SettingsMenu() {}

    @Override
    public InventoryBuilder build() {
        final Inventory inventory = getInventory();

        return this;
    }

    @Override
    public void run(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        final Inventory inventory = event.getInventory();

        if (!(inventory.getHolder(false) instanceof SettingsMenu)) return;
    }
}