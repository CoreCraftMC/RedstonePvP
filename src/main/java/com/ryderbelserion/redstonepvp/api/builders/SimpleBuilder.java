package com.ryderbelserion.redstonepvp.api.builders;

import com.ryderbelserion.redstonepvp.api.interfaces.Gui;
import com.ryderbelserion.redstonepvp.api.interfaces.GuiType;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

/**
 * @author Matt
 */
public final class SimpleBuilder extends BaseGuiBuilder<Gui, SimpleBuilder> {

    private GuiType guiType;

    /**
     * Main constructor
     *
     * @param guiType the {@link GuiType} to default to
     */
    public SimpleBuilder(@NotNull final GuiType guiType) {
        this.guiType = guiType;
    }

    /**
     * Creates a new {@link Gui}
     *
     * @return a new {@link Gui}
     */
    @Override
    public @NotNull Gui create() {
        final Gui gui;

        gui = this.guiType == null || this.guiType == GuiType.CHEST ? new Gui(getTitle(), getRows(), getInteractionComponents())
                : new Gui(getTitle(), this.guiType, getInteractionComponents());

        final Consumer<Gui> consumer = getConsumer();
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