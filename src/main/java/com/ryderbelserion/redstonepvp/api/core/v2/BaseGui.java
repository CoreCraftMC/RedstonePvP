package com.ryderbelserion.redstonepvp.api.core.v2;

import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.IBaseGui;
import com.ryderbelserion.vital.paper.util.AdvUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class BaseGui implements InventoryHolder, Listener, IBaseGui {

    private final Inventory inventory;

    private final String title;
    private final int rows;

    public BaseGui(final String title, final int rows) {
        this.title = title;
        this.rows = rows;

        this.inventory = Bukkit.getServer().createInventory(this, this.rows * 9, title());
    }

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
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}