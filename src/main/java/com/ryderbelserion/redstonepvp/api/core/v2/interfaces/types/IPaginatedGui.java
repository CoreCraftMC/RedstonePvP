package com.ryderbelserion.redstonepvp.api.core.v2.interfaces.types;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.PaginatedGui;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author ryderbelserion
 */
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
    void addPageItem(@NotNull final GuiItem guiItem);

    /**
     * Removes a given {@link GuiItem} from the page.
     *
     * @param guiItem the {@link GuiItem} to remove
     */
    void removePageItem(@NotNull final GuiItem guiItem);

    /**
     * Removes a given {@link ItemStack} from the page.
     *
     * @param itemStack the {@link ItemStack} to remove
     */
    void removePageItem(@NotNull final ItemStack itemStack);

    /**
     * Updates the page {@link GuiItem} on the slot in the page
     * Can get the slot from {@link InventoryClickEvent#getSlot()}
     *
     * @param slot the slot of the item to update
     * @param itemStack the new {@link ItemStack}
     */
    void updatePageItem(final int slot, @NotNull final ItemStack itemStack);

    /**
     * Alternative {@link #updatePageItem(int, ItemStack)} that uses <i>ROWS</i> and <i>COLUMNS</i> instead
     *
     * @param row the row of the slot
     * @param col the columns of the slot
     * @param itemStack the new {@link ItemStack}
     */
    void updatePageItem(final int row, final int col, @NotNull final ItemStack itemStack);

    /**
     * Alternative {@link #updatePageItem(int, GuiItem)} that uses <i>ROWS</i> and <i>COLUMNS</i> instead
     *
     * @param row the row of the slot
     * @param col the columns of the slot
     * @param guiItem the new {@link GuiItem}
     */
    void updatePageItem(final int row, final int col, @NotNull final GuiItem guiItem);

    /**
     * Alternative {@link #updatePageItem(int, ItemStack)} that uses {@link GuiItem} instead
     *
     * @param slot the slot of the item to update
     * @param guiItem the new ItemStack
     */
    void updatePageItem(final int slot, @NotNull final GuiItem guiItem);

    /**
     * Specific open method for the Paginated GUI
     * Uses {@link #populatePage()}
     *
     * @param player the {@link Player} to open it to
     * @param openPage the specific page to open at
     */
    void open(@NotNull final Player player, final int openPage);

    /**
     * Gets the next page number.
     *
     * @return the next page number or {@link #getPageNumber()} if no next is present
     */
    int getNextPageNumber();

    /**
     * Gets the previous page number.
     *
     * @return the previous page number or {@link #getPageNumber()} if no previous is present
     */
    int getPreviousPageNumber();

    /**
     * Goes to the next page.
     *
     * @return false if there is no next page
     */
    boolean next();

    /**
     * Goes to the previous page if possible.
     *
     * @return false if there is no previous page
     */
    boolean previous();

    /**
     * Gets the page item for the GUI listener.
     *
     * @param slot the slot to get
     * @return the GuiItem on that slot
     */
    GuiItem getPageItem(final int slot);

    /**
     * Sets the page number,
     *
     * @param pageNumber sets the current page to be the specified number
     */
    void setPageNumber(final int pageNumber);

    /**
     * Gets the page number,
     *
     * @return the current page number
     */
    int getPageNumber();

    /**
     * Gets the items in the page
     *
     * @param givenPage the page to get
     * @return a list with all the page items
     */
    List<GuiItem> getItemsFromPage(final int givenPage);

    /**
     * Gets the current page items to be used on other gui types
     *
     * @return the {@link Map} with all the current items
     */
    Map<Integer, GuiItem> getCurrentPageItems();

    /**
     * Clears the page content.
     */
    void clearPageContents();

    /**
     * Clears all previously added page items.
     *
     * @param update true or false
     */
    void clearPageItems(final boolean update);

    /**
     * Clears all previously added page items.
     */
    void clearPageItems();

    /**
     * Gets the max number of pages the GUI has.
     *
     * @return the max pages
     */
    int getMaxPages();

    /**
     * Populates the inventory with the page items.
     */
    void populatePage();

    /**
     * Updates the inventory with items.
     */
    void updatePage();

    /**
     * Calculates the size of the give page.
     *
     * @return the page size
     */
    int calculatePageSize();

}