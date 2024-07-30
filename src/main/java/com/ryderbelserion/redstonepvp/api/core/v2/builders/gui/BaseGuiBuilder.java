package com.ryderbelserion.redstonepvp.api.core.v2.builders.gui;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.BaseGui;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.components.InteractionComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class BaseGuiBuilder<G extends BaseGui, B extends BaseGuiBuilder<G, B>> {

    private final EnumSet<InteractionComponent> components = EnumSet.noneOf(InteractionComponent.class);
    private String title = "";
    private int rows = 1;

    private Consumer<G> consumer;

    /**
     * Creates the given GuiBase
     * Has to be abstract because each GUI is different
     *
     * @return the new {@link BaseGui}
     */
    public abstract @NotNull G create();

    /**
     * Sets the rows for the GUI
     * This will only work on CHEST {@link com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiType}
     *
     * @param rows the amount of rows
     * @return the builder
     */
    public @NotNull final B setRows(final int rows) {
        this.rows = rows;

        return (B) this;
    }

    /**
     * Sets the title for the GUI
     * This will be either a Component or a String
     *
     * @param title the GUI title
     * @return the builder
     */
    public @NotNull final B setTitle(@NotNull final String title) {
        this.title = title;

        return (B) this;
    }

    /**
     * Disables item placement in the gui.
     *
     * @return {@link B}
     */
    public final B disableItemPlacement() {
        this.components.add(InteractionComponent.PREVENT_ITEM_PLACE);


        return (B) this;
    }

    /**
     * Disables items to be taken in inventories.
     *
     * @return {@link B}
     */
    public final B disableItemTake() {
        this.components.add(InteractionComponent.PREVENT_ITEM_TAKE);

        return (B) this;
    }

    /**
     * Disables items to be swapped in the gui.
     *
     * @return {@link B}
     */
    public final B disableItemSwap() {
        this.components.add(InteractionComponent.PREVENT_ITEM_SWAP);

        return (B) this;
    }

    /**
     * Disables item drops from inside the gui.
     *
     * @return {@link B}
     */
    public final B disableItemDrop() {
        this.components.add(InteractionComponent.PREVENT_ITEM_DROP);

        return (B) this;
    }

    /**
     * Disables all interactions in the gui.
     *
     * @return {@link B}
     */
    public final B disableInteractions() {
        this.components.addAll(InteractionComponent.VALUES);

        return (B) this;
    }

    /**
     * Enables all interactions in the gui.
     *
     * @return {@link B}
     */
    public final B enableInteractions() {
        this.components.clear();

        return (B) this;
    }

    /**
     * Enables item placement in the gui.
     *
     * @return {@link B}
     */
    public final B enableItemPlacement() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_PLACE);


        return (B) this;
    }

    /**
     * Enables items to be taken in inventories.
     *
     * @return {@link B}
     */
    public final B enableItemTake() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_TAKE);

        return (B) this;
    }

    /**
     * Enables items to be swapped in the gui.
     *
     * @return {@link B}
     */
    public final B enableItemSwap() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_SWAP);

        return (B) this;
    }

    /**
     * Enables item drops from inside the gui.
     *
     * @return {@link B}
     */
    public final B enableItemDrop() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_DROP);

        return (B) this;
    }

    /**
     * Applies anything to the GUI once it's created,
     * Can be pretty useful for setting up small things like default actions,
     *
     * @param consumer a {@link Consumer} that passes the built GUI
     * @return the builder
     */
    public @NotNull final B apply(@NotNull final Consumer<G> consumer) {
        this.consumer = consumer;

        return (B) this;
    }

    /**
     * Getter for the set of interaction modifiers.
     *
     * @return the set of {@link InteractionComponent}
     * @author SecretX
     */
    protected @NotNull final Set<InteractionComponent> getInteractionComponents() {
        return this.components;
    }

    /**
     * Getter for the consumer.
     *
     * @return the consumer
     */
    protected @Nullable final Consumer<G> getConsumer() {
        return this.consumer;
    }

    /**
     * Getter for the title.
     *
     * @return the current title
     */
    protected @NotNull final String getTitle() {
        return this.title;
    }

    /**
     * Getter for the rows,
     *
     * @return the amount of rows
     */
    protected final int getRows() {
        return this.rows;
    }
}