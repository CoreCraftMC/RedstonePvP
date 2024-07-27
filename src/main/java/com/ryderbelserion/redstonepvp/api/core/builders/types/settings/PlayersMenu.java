package com.ryderbelserion.redstonepvp.api.core.builders.types.settings;

import com.ryderbelserion.redstonepvp.api.core.builders.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class PlayersMenu extends InventoryBuilder {

    public PlayersMenu(final Player player, final String guiName, final int guiSize) {
        super(player, guiName, guiSize);
    }

    public PlayersMenu() {}

    @Override
    public InventoryBuilder build() {
        final Inventory inventory = getInventory();

        return this;
    }

    @Override
    public void run(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        final Inventory inventory = event.getInventory();

        if (!(inventory.getHolder(false) instanceof PlayersMenu)) return;
    }
}