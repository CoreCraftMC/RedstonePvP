package com.ryderbelserion.redstonepvp.api.core.v2.interfaces;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.components.InteractionComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

public interface IBaseGui {

    String getTitle();

    void setTitle(String title);

    Component title();

    int getRows();

    void setRows(int rows);

    int getSize();

    void close(Player player);

    void updateTitle(Player player);

    void updateTitles();

    void addInteractionComponent(InteractionComponent... components);

    void removeInteractionComponent(InteractionComponent component);

    boolean canPerformOtherActions();

    boolean isInteractionsDisabled();

    boolean canPlaceItems();

    boolean canTakeItems();

    boolean canSwapItems();

    boolean canDropItems();

    /**
     * Sets the {@link GuiAction} of a default click on any item.
     * See {@link InventoryClickEvent}.
     *
     * @param defaultClickAction {@link GuiAction} to resolve when any item is clicked
     */
    void setDefaultClickAction(@Nullable GuiAction<@NotNull InventoryClickEvent> defaultClickAction);

    /**
     * Gets the default top click resolver.
     */
    GuiAction<InventoryClickEvent> getDefaultTopClickAction();

    /**
     * Sets the {@link GuiAction} of a default click on any item on the top part of the GUI.
     * Top inventory being for example chests etc, instead of the {@link Player} inventory.
     * See {@link InventoryClickEvent}.
     *
     * @param defaultTopClickAction {@link GuiAction} to resolve when clicking on the top inventory
     */
    void setDefaultTopClickAction(@Nullable GuiAction<@NotNull InventoryClickEvent> defaultTopClickAction);

    /**
     * Gets the player inventory action.
     */
    GuiAction<InventoryClickEvent> getPlayerInventoryAction();

    /**
     * Sets the {@link GuiAction} to run when the GUI opens.
     * See {@link InventoryOpenEvent}.
     *
     * @param openGuiAction {@link GuiAction} to resolve when opening the inventory
     */
    void setOpenGuiAction(@Nullable GuiAction<@NotNull InventoryOpenEvent> openGuiAction);

    GuiAction<InventoryClickEvent> getOutsideClickAction();

    /**
     * Gets the default click resolver.
     */
    GuiAction<InventoryClickEvent> getDefaultClickAction();

    /**
     * Sets the {@link GuiAction} of a default drag action.
     * See {@link InventoryDragEvent}.
     *
     * @param dragAction {@link GuiAction} to resolve
     */
    void setDragAction(@Nullable GuiAction<@NotNull InventoryDragEvent> dragAction);

    /**
     * Gets the close gui resolver.
     */
    GuiAction<InventoryCloseEvent> getCloseGuiAction();

    /**
     * Sets the {@link GuiAction} to run once the inventory is closed.
     * See {@link InventoryCloseEvent}.
     *
     * @param closeGuiAction {@link GuiAction} to resolve when the inventory is closed
     */
    void setCloseGuiAction(@Nullable GuiAction<@NotNull InventoryCloseEvent> closeGuiAction);

    /**
     * Gets the open gui resolver.
     */
    GuiAction<InventoryOpenEvent> getOpenGuiAction();

    /**
     * Sets the {@link GuiAction} to run when clicking in the inventory.
     * See {@link InventoryClickEvent}.
     *
     * @param playerInventoryAction {@link GuiAction} to resolve when clicking in the inventory
     */
    void setPlayerInventoryAction(@Nullable GuiAction<@NotNull InventoryClickEvent> playerInventoryAction);

    /**
     * Gets the default drag resolver.
     */
    GuiAction<InventoryDragEvent> getDragAction();

    /**
     * Sets the {@link GuiAction} to run when clicking on the outside of the inventory.
     * See {@link InventoryClickEvent}.
     *
     * @param outsideClickAction {@link GuiAction} to resolve when clicking outside the inventory.
     */
    void setOutsideClickAction(@Nullable GuiAction<@NotNull InventoryClickEvent> outsideClickAction);

    default Set<InteractionComponent> safeCopy(final Set<InteractionComponent> components) {
        return components.isEmpty() ? EnumSet.noneOf(InteractionComponent.class) : EnumSet.copyOf(components);
    }
}