package com.ryderbelserion.redstonepvp.api.core.v2.builders;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.BaseGuiBuilder;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.GuiBuilder;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiType;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

/**
 * @author Matt
 */
public final class SimpleBuilder extends BaseGuiBuilder<GuiBuilder, SimpleBuilder> {

    private GuiType guiType;

    /**
     * Creates a new {@link GuiBuilder}
     *
     * @return a new {@link GuiBuilder}
     */
    @Override
    public @NotNull GuiBuilder create() {
        final GuiBuilder gui;

        gui = this.guiType == null || this.guiType == GuiType.CHEST ? new GuiBuilder(getTitle(), getRows(), getInteractionComponents())
                : new GuiBuilder(getTitle(), this.guiType, getInteractionComponents());

        final Consumer<GuiBuilder> consumer = getConsumer();
        if (consumer != null) consumer.accept(gui);

        return gui;
    }

    /**
     * Sets the {@link GuiType} to use on the GUI
     * This method is unique to the simple GUI
     *
     * @param guiType the {@link GuiType}
     * @return the current builder
     */
    public @NotNull SimpleBuilder type(final GuiType guiType) {
        this.guiType = guiType;

        return this;
    }
}