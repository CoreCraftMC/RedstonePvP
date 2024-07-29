package com.ryderbelserion.redstonepvp.api.core.v2.interfaces;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.components.InteractionComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IBaseGui {

    /**
     * @return {@link String}
     */
    String getTitle();

    /**
     * Sets the new title.
     *
     * @param title the new title
     */
    void setTitle(final String title);

    /**
     * @return {@link Component}
     */
    Component title();

    /**
     * @return the amount of rows
     */
    int getRows();

    /**
     * Set how many rows a gui should have.
     *
     * @param rows the number of rows
     */
    void setRows(final int rows);

    /**
     * @return the inventory size
     */
    int getSize();

    /**
     * Close an inventory for a player.
     *
     * @param player {@link Player}
     */
    void close(final Player player);

    /**
     * Update the inventory title for a single player.
     *
     * @param player {@link Player}
     */
    void updateTitle(final Player player);

    /**
     * Update the inventory for a single player.
     *
     * @param player {@link Player}
     */
    void updateInventory(final Player player);

    /**
     * Update the inventory titles for all players!
     */
    void updateTitles();

    /**
     * Update the inventories for all players!
     */
    void updateInventories();

    /**
     * @return true or false
     */
    boolean isUpdating();

    /**
     * Sets the updating status of the gui.
     *
     * @param isUpdating true or false
     */
    void setUpdating(final boolean isUpdating);

    /**
     * Adds multiple interaction components
     *
     * @param components {@link InteractionComponent}
     */
    void addInteractionComponent(final InteractionComponent... components);

    /**
     * Removes an interaction component
     *
     * @param component {@link InteractionComponent}
     */
    void removeInteractionComponent(final InteractionComponent component);

    /**
     * @return true or false
     */
    boolean canPerformOtherActions();

    /**
     * @return true or false
     */
    boolean isInteractionsDisabled();

    /**
     * @return true or false
     */
    boolean canPlaceItems();

    /**
     * @return true or false
     */
    boolean canTakeItems();

    /**
     * @return true or false
     */
    boolean canSwapItems();

    /**
     * @return true or false
     */
    boolean canDropItems();

    /**
     * Gives an item to a player while stripping the pdc tag.
     *
     * @param player {@link Player}
     * @param itemStack {@link ItemStack}
     */
    void giveItem(final Player player, final ItemStack itemStack);

    /**
     * Gives multiple items to a player while stripping the pdc tag.
     *
     * @param player {@link Player}
     * @param itemStacks {@link ItemStack}
     */
    void giveItem(final Player player, final ItemStack... itemStacks);

    /**
     * Sets an item to a specific slot.
     *
     * @param slot the slot number
     * @param guiItem {@link GuiItem}
     */
    void setItem(final int slot, final GuiItem guiItem);

    /**
     * Alternative {@link #setItem(int, GuiItem)} to set item that uses <i>ROWS</i> and <i>COLUMNS</i> instead of slots.
     *
     * @param row the GUI row number
     * @param col the GUI column number
     * @param guiItem the {@link GuiItem} to add to the slot
     */
    void setItem(final int row, final int col, @NotNull final GuiItem guiItem);

    /**
     * Alternative {@link #setItem(int, GuiItem)} to set item that takes a {@link List} of slots instead.
     *
     * @param slots the slots in which the item should go
     * @param guiItem the {@link GuiItem} to add to the slots
     */
    void setItem(@NotNull final List<Integer> slots, @NotNull final GuiItem guiItem);

    /**
     * Adds {@link GuiItem}s to the GUI without specific slot.
     * It'll set the item to the next empty slot available.
     *
     * @param items varargs for specifying the {@link GuiItem}'s
     */
    void addItem(@NotNull final GuiItem... items);

    /**
     * Adds {@link GuiItem}s to the GUI without specific slot.
     * It'll set the item to the next empty slot available.
     *
     * @param expandIfFull if true, expands the gui if it is full and there are more items to be added
     * @param items varargs for specifying the {@link GuiItem}'s
     */
    void addItem(final boolean expandIfFull, @NotNull final GuiItem... items);

    /**
     * Removes an item from the gui.
     *
     * @param itemStack {@link ItemStack}
     */
    void removeItem(final ItemStack itemStack);

    /**
     * Alternative {@link #removeItem(int)} with cols and rows.
     *
     * @param row the row
     * @param col the column
     */
    void removeItem(final int row, final int col);

    /**
     * Removes an item from the gui.
     *
     * @param guiItem {@link GuiItem}
     */
    void removeItem(final GuiItem guiItem);

    /**
     * Removes an item from the inventory based on the slot.
     *
     * @param slot the slot number
     */
    void removeItem(final int slot);

    /**
     * Sets the {@link GuiAction} of a default click on any item.
     * See {@link InventoryClickEvent}.
     *
     * @param defaultClickAction {@link GuiAction} to resolve when any item is clicked
     */
    void setDefaultClickAction(final @Nullable GuiAction<@NotNull InventoryClickEvent> defaultClickAction);

    /**
     * Gets a specific {@link GuiItem} on the slot.
     *
     * @param slot the slot of the item.
     * @return the {@link GuiItem} on the introduced slot or {@code null} if it doesn't exist
     */
    @Nullable GuiItem getGuiItem(final int slot);

    /**
     * Adds a {@link GuiAction} for when clicking on a specific slot.
     * See {@link InventoryClickEvent}.
     *
     * @param slot the slot that will trigger the {@link GuiAction}.
     * @param slotAction {@link GuiAction} to resolve when clicking on specific slots.
     */
    void addSlotAction(final int slot, @Nullable final GuiAction<@NotNull InventoryClickEvent> slotAction);

    /**
     * Gets the action for the specified slot.
     *
     * @param slot the slot clicked.
     */
    @Nullable GuiAction<InventoryClickEvent> getSlotAction(final int slot);

    /**
     * Gets the default top click resolver.
     */
    GuiAction<InventoryClickEvent> getDefaultTopClickAction();

    /**
     * Sets the {@link GuiAction} of a default click on any item on the top part of the GUI.
     * Top inventory being for example chests etc., instead of the {@link Player} inventory.
     * See {@link InventoryClickEvent}.
     *
     * @param defaultTopClickAction {@link GuiAction} to resolve when clicking on the top inventory
     */
    void setDefaultTopClickAction(final @Nullable GuiAction<@NotNull InventoryClickEvent> defaultTopClickAction);

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
    void setOpenGuiAction(final @Nullable GuiAction<@NotNull InventoryOpenEvent> openGuiAction);

    /**
     * Gets the outside click resolver.
     */
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
    void setDragAction(final @Nullable GuiAction<@NotNull InventoryDragEvent> dragAction);

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
    void setCloseGuiAction(final @Nullable GuiAction<@NotNull InventoryCloseEvent> closeGuiAction);

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
    void setPlayerInventoryAction(final @Nullable GuiAction<@NotNull InventoryClickEvent> playerInventoryAction);

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
    void setOutsideClickAction(final @Nullable GuiAction<@NotNull InventoryClickEvent> outsideClickAction);

    /**
     * Opens an inventory for {@link Player}
     *
     * @param player {@link Player}
     */
    void open(final Player player);

    /**
     * Safely copies an enum set.
     *
     * @param components {@link Set<InteractionComponent>}
     * @return {@link Set<InteractionComponent>}
     */
    default Set<InteractionComponent> safeCopy(final Set<InteractionComponent> components) {
        return components.isEmpty() ? EnumSet.noneOf(InteractionComponent.class) : EnumSet.copyOf(components);
    }

    /**
     * Populates an inventory with items.
     *
     * @param inventory {@link Inventory}
     * @param guiItems {@link Map}
     */
    default void populate(final Inventory inventory, final Map<Integer, GuiItem> guiItems) {
        for (final Map.Entry<Integer, GuiItem> entry : guiItems.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().getItemStack());
        }
    }
}