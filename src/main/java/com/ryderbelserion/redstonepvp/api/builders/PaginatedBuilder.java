package com.ryderbelserion.redstonepvp.api.builders;

import com.ryderbelserion.redstonepvp.api.interfaces.Gui;
import com.ryderbelserion.redstonepvp.api.builders.gui.PaginatedGui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author Matt
 */
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
     * Creates a new {@link Gui}
     *
     * @return a new {@link Gui}
     */
    @Override
    public final @NotNull PaginatedGui create() {
        final PaginatedGui gui = new PaginatedGui(getTitle(), this.pageSize, getRows(), getInteractionComponents());

        final Consumer<PaginatedGui> consumer = getConsumer();
        if (consumer != null) consumer.accept(gui);

        return gui;
    }
}