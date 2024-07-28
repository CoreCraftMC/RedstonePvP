package com.ryderbelserion.redstonepvp.api.core.v2.listeners;

import com.ryderbelserion.redstonepvp.api.core.v2.BaseGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof BaseGui gui)) return;

        gui.click(event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof BaseGui gui)) return;

        // Cancel the action for the time being.
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof BaseGui gui)) return;

        gui.close(event);
    }
}