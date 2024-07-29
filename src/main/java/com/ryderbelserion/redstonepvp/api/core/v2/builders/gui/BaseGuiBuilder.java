package com.ryderbelserion.redstonepvp.api.core.v2.builders.gui;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.BaseGui;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.components.InteractionComponent;
import org.jetbrains.annotations.NotNull;
import java.util.EnumSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public abstract class BaseGuiBuilder<G extends BaseGui, B extends BaseGuiBuilder<G, B>> {

    private final EnumSet<InteractionComponent> components = EnumSet.noneOf(InteractionComponent.class);
    private String title = "";
    private int rows = 1;

    public abstract @NotNull G create();

    public @NotNull B setRows(final int rows) {
        this.rows = rows;

        return (B) this;
    }

    public @NotNull B setTitle(@NotNull final String title) {
        this.title = title;

        return (B) this;
    }

    /**
     * Disables item placement in the gui.
     *
     * @return {@link B}
     */
    public B disableItemPlacement() {
        this.components.add(InteractionComponent.PREVENT_ITEM_PLACE);


        return (B) this;
    }

    /**
     * Disables items to be taken in inventories.
     *
     * @return {@link B}
     */
    public B disableItemTake() {
        this.components.add(InteractionComponent.PREVENT_ITEM_TAKE);

        return (B) this;
    }

    /**
     * Disables items to be swapped in the gui.
     *
     * @return {@link B}
     */
    public B disableItemSwap() {
        this.components.add(InteractionComponent.PREVENT_ITEM_SWAP);

        return (B) this;
    }

    /**
     * Disables item drops from inside the gui.
     *
     * @return {@link B}
     */
    public B disableItemDrop() {
        this.components.add(InteractionComponent.PREVENT_ITEM_DROP);

        return (B) this;
    }

    /**
     * Disables all interactions in the gui.
     *
     * @return {@link B}
     */
    public B disableInteractions() {
        this.components.addAll(InteractionComponent.VALUES);

        return (B) this;
    }

    /**
     * Enables all interactions in the gui.
     *
     * @return {@link B}
     */
    public B enableInteractions() {
        this.components.clear();

        return (B) this;
    }

    /**
     * Enables item placement in the gui.
     *
     * @return {@link B}
     */
    public B enableItemPlacement() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_PLACE);


        return (B) this;
    }

    /**
     * Enables items to be taken in inventories.
     *
     * @return {@link B}
     */
    public B enableItemTake() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_TAKE);

        return (B) this;
    }

    /**
     * Enables items to be swapped in the gui.
     *
     * @return {@link B}
     */
    public B enableItemSwap() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_SWAP);

        return (B) this;
    }

    /**
     * Enables item drops from inside the gui.
     *
     * @return {@link B}
     */
    public B enableItemDrop() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_DROP);

        return (B) this;
    }

    protected final Set<InteractionComponent> getInteractionComponents() {
        return this.components;
    }

    protected final String getTitle() {
        return this.title;
    }

    protected final int getRows() {
        return this.rows;
    }
}