package com.ryderbelserion.redstonepvp.api.core.v2.builders;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.BaseGuiBuilder;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.GuiBuilder;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.PaginatedGui;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class PaginatedBuilder extends BaseGuiBuilder<PaginatedGui, PaginatedBuilder> {

    private int pageSize = 0;

    /**
     * Sets the desirable page size, most of the time this isn't needed
     *
     * @param pageSize the amount of free slots that page items should occupy
     * @return {@link PaginatedBuilder}
     */
    @NotNull
    @Contract("_ -> this")
    public final PaginatedBuilder pageSize(final int pageSize) {
        this.pageSize = pageSize;

        return this;
    }

    /**
     * Creates a new {@link GuiBuilder}
     *
     * @return a new {@link GuiBuilder}
     */
    @Override
    public final @NotNull PaginatedGui create() {
        final PaginatedGui gui = new PaginatedGui(getTitle(), this.pageSize, getRows(), getInteractionComponents());

        final Consumer<PaginatedGui> consumer = getConsumer();
        if (consumer != null) consumer.accept(gui);

        return gui;
    }
}