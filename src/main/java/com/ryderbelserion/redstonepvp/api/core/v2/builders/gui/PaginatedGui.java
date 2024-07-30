package com.ryderbelserion.redstonepvp.api.core.v2.builders.gui;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.components.InteractionComponent;
import com.ryderbelserion.redstonepvp.api.core.v2.keys.GuiKeys;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiItem;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.types.IPaginatedGui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Matt
 */
public class PaginatedGui extends BaseGui implements IPaginatedGui {

    private final List<GuiItem> pageItems = new ArrayList<>();
    private final Map<Integer, GuiItem> currentPage;

    private int pageSize;
    private int pageNumber;

    /**
     * Main constructor to provide a way to create PaginatedGui
     *
     * @param rows the amount of rows the GUI should have
     * @param pageSize the page size.
     * @param title the GUI's title using {@link String}
     * @param components a set containing what {@link InteractionComponent} this GUI should have
     * @author SecretX
     */
    public PaginatedGui(@NotNull final String title, final int pageSize, final int rows, @NotNull final Set<InteractionComponent> components) {
        super(title, rows, components);

        this.pageSize = pageSize;

        int size = rows * 9;

        this.currentPage = new LinkedHashMap<>(size);
    }

    /**
     * {@inheritDoc}
     *
     * @param pageSize {@inheritDoc}
     */
    @Override
    public final PaginatedGui setPageSize(final int pageSize) {
        this.pageSize = pageSize;

        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param guiItem {@inheritDoc}
     */
    @Override
    public void addPageItem(@NotNull final GuiItem guiItem) {
        this.pageItems.add(guiItem);
    }

    /**
     * {@inheritDoc}
     *
     * @param guiItem {@inheritDoc}
     */
    @Override
    public void removePageItem(@NotNull final GuiItem guiItem) {
        this.pageItems.remove(guiItem);
    }

    /**
     * {@inheritDoc}
     *
     * @param itemStack {@inheritDoc}
     */
    @Override
    public void removePageItem(@NotNull final ItemStack itemStack) {
        final String key = GuiKeys.getUUID(itemStack);

        final Optional<GuiItem> guiItem = this.pageItems.stream().filter(it -> {
            final String pair = it.getUuid().toString();

            return key.equalsIgnoreCase(pair);
        }).findFirst();

        guiItem.ifPresent(this::removePageItem);
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @param itemStack {@inheritDoc}
     */
    @Override
    public void updatePageItem(final int slot, @NotNull final ItemStack itemStack) {
        if (!this.currentPage.containsKey(slot)) return;

        final GuiItem guiItem = this.currentPage.get(slot);

        guiItem.setItemStack(itemStack);

        getInventory().setItem(slot, guiItem.getItemStack());
    }

    /**
     *
     * @param row {@inheritDoc}
     * @param col {@inheritDoc}
     * @param itemStack {@inheritDoc}
     */
    @Override
    public void updatePageItem(final int row, final int col, @NotNull final ItemStack itemStack) {
        updatePageItem(getSlotFromRowColumn(row, col), itemStack);
    }

    /**
     * {@inheritDoc}
     *
     * @param row {@inheritDoc}
     * @param col {@inheritDoc}
     * @param guiItem {@inheritDoc}
     */
    @Override
    public void updatePageItem(final int row, final int col, @NotNull final GuiItem guiItem) {
        updatePageItem(getSlotFromRowColumn(row, col), guiItem);
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @param guiItem {@inheritDoc}
     */
    @Override
    public void updatePageItem(final int slot, @NotNull final GuiItem guiItem) {
        if (!this.currentPage.containsKey(slot)) return;

        final int index = this.pageItems.indexOf(this.currentPage.get(slot));

        // Updates both lists and inventory
        this.currentPage.put(slot, guiItem);
        this.pageItems.set(index, guiItem);

        getInventory().setItem(slot, guiItem.getItemStack());
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @param openPage {@inheritDoc}
     */
    @Override
    public void open(@NotNull final Player player, final int openPage) {
        if (player.isSleeping()) return;

        if (openPage <= getMaxPages() || openPage > 0) this.pageNumber = openPage;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final int getNextPageNumber() {
        if (this.pageNumber + 1 > getMaxPages()) return this.pageNumber;

        return pageNumber + 1;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final int getPreviousPageNumber() {
        if (this.pageNumber - 1 == 0) return this.pageNumber;

        return this.pageNumber - 1;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final boolean next() {
        if (this.pageNumber + 1 > getMaxPages()) return false;

        this.pageNumber++;

        updatePage();

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final boolean previous() {
        if (this.pageNumber - 1 == 0) return false;

        this.pageNumber--;

        updatePage();

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public final GuiItem getPageItem(final int slot) {
        return currentPage.get(slot);
    }

    /**
     * {@inheritDoc}
     *
     * @param pageNumber {@inheritDoc}
     */
    @Override
    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final int getPageNumber() {
        return this.pageNumber;
    }

    /**
     * {@inheritDoc}
     *
     * @param givenPage {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public final List<GuiItem> getItemsFromPage(final int givenPage) {
        final int page = givenPage - 1;

        final List<GuiItem> guiPage = new ArrayList<>();

        int max = ((page * this.pageSize) + this.pageSize);
        if (max > this.pageItems.size()) max = this.pageItems.size();

        for (int i = page * this.pageSize; i < max; i++) {
            guiPage.add(this.pageItems.get(i));
        }

        return guiPage;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final Map<Integer, GuiItem> getCurrentPageItems() {
        return this.currentPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearPageContents() {
        for (Map.Entry<Integer, GuiItem> entry : this.currentPage.entrySet()) {
            getInventory().setItem(entry.getKey(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param update {@inheritDoc}
     */
    @Override
    public void clearPageItems(boolean update) {
        this.pageItems.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearPageItems() {
        clearPageItems(false);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public final int getMaxPages() {
        return (int) Math.ceil((double) this.pageItems.size() / this.pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populatePage() {
        int slot = 0;

        for (GuiItem item : getItemsFromPage(this.pageNumber)) {
            if (getGuiItem(slot) != null || getInventory().getItem(slot) != null) {
                slot++;

                continue;
            }

            this.currentPage.put(slot, item);

            getInventory().setItem(slot, item.getItemStack());

            slot++;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePage() {
        clearPageContents();
        populatePage();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final int calculatePageSize() {
        int counter = 0;

        for (int slot = 0; slot < getRows() * 9; slot++) {
            if (getInventory().getItem(slot) == null) counter++;
        }

        return counter;
    }

    // Overridden methods from the BaseGui class

    /**
     * {@inheritDoc}
     *
     * @param items {@inheritDoc}
     */
    @Override
    public void addItem(@NotNull final GuiItem... items) {
        this.pageItems.addAll(Arrays.asList(items));
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     */
    @Override
    public void updateInventory(final Player player) {
        getInventory().clear();
        populate();
    }
}