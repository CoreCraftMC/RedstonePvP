package com.ryderbelserion.redstonepvp.api.core.v2;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.IBaseGui;
import com.ryderbelserion.redstonepvp.api.core.v2.listeners.GuiListener;
import com.ryderbelserion.vital.paper.util.AdvUtil;
import com.ryderbelserion.vital.paper.util.scheduler.FoliaRunnable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class BaseGui implements InventoryHolder, Listener, IBaseGui {

    private static final RedstonePvP plugin = RedstonePvP.getPlugin();

    static {
        plugin.getServer().getPluginManager().registerEvents(new GuiListener(), plugin);
    }

    private Inventory inventory;

    private String title;
    private int rows;

    public BaseGui(final String title, final int rows) {
        this.title = title;
        this.rows = rows;

        this.inventory = plugin.getServer().createInventory(this, this.rows * 9, title());
    }

    public BaseGui() {}

    public abstract BaseGui build();

    public abstract void click(InventoryClickEvent event);

    public abstract void close(InventoryCloseEvent event);

    @Override
    public final String getTitle() {
        return this.title;
    }

    @Override
    public final Component title() {
        return AdvUtil.parse(getTitle());
    }

    @Override
    public final int getRows() {
        return this.rows;
    }

    @Override
    public final int getSize() {
        return getRows() * 9;
    }

    @Override
    public void close(Player player) {
        new FoliaRunnable(plugin.getServer().getGlobalRegionScheduler()) {
            @Override
            public void run() {
                player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            }
        }.runDelayed(plugin, 2);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}