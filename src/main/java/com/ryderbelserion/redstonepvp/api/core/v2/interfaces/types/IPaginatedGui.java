package com.ryderbelserion.redstonepvp.api.core.v2.interfaces.types;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.PaginatedGui;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiItem;
import org.jetbrains.annotations.NotNull;

public interface IPaginatedGui {

    /**
     * Sets the page size of {@link PaginatedGui}.
     *
     * @param pageSize the page size
     * @return {@link PaginatedGui}
     */
    PaginatedGui setPageSize(final int pageSize);

    /**
     * Adds an item to the {@link PaginatedGui}
     *
     * @param guiItem {@link GuiItem}
     */
    void addItem(@NotNull final GuiItem guiItem);

}