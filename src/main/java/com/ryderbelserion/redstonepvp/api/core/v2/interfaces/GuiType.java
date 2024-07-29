package com.ryderbelserion.redstonepvp.api.core.v2.interfaces;

import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public enum GuiType {

    CHEST(InventoryType.CHEST, 9),

    WORKBENCH(InventoryType.WORKBENCH, 9),

    HOPPER(InventoryType.HOPPER, 5),

    DISPENSER(InventoryType.DISPENSER, 8),

    BREWING(InventoryType.BREWING, 4);

    private @NotNull final InventoryType inventoryType;
    private final int limit;

    /**
     * Creates an inventory type with a size limit.
     *
     * @param inventoryType {@link InventoryType}
     * @param limit the limit
     */
    GuiType(@NotNull final InventoryType inventoryType, final int limit) {
        this.inventoryType = inventoryType;
        this.limit = limit;
    }

    /**
     * Gets the {@link InventoryType}.
     *
     * @return {@link InventoryType}
     */
    public @NotNull final InventoryType getInventoryType() {
        return this.inventoryType;
    }

    /**
     * @return the gui size limitation
     */
    public final int getLimit() {
        return this.limit;
    }
}