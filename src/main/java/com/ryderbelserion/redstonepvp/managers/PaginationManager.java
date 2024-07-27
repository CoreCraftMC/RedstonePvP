package com.ryderbelserion.redstonepvp.managers;

import ch.jalu.configme.SettingsManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PaginationManager {

    private final SettingsManager config = ConfigManager.getConfig();

    private final Map<UUID, Integer> pages = new HashMap<>();

    /**
     * Goes to the next page
     *
     * @param player {@link Player}
     * @param currentPage page number
     * @param perPage max per page
     * @param size list size
     */
    public void nextPage(final Player player, final int currentPage, final int perPage, final int size) {
        setPage(player, currentPage, perPage, size);
    }

    /**
     * Goes back a page
     *
     * @param player {@link Player}
     * @param currentPage page number
     * @param perPage max per page
     * @param size list size
     */
    public void backPage(final Player player, final int currentPage, final int perPage, final int size) {
        setPage(player, currentPage, perPage, size);
    }

    /**
     * Remove the person viewing the gui.
     *
     * @param player {@link Player}
     */
    public void remove(final Player player) {
        this.pages.remove(player.getUniqueId());
    }

    /**
     * Get the page
     *
     * @param player {@link Player}
     * @return the page
     */
    public int getPage(final Player player) {
        return this.pages.getOrDefault(player.getUniqueId(), 1);
    }

    /**
     * Sets the page based on the size of the list.
     *
     * @param player {@link Player}
     * @param currentPage page number
     * @param perPage max per page
     * @param size list size
     */
    public void setPage(final Player player, int currentPage, final int perPage, final int size) {
        int max = getMaxPages(size, perPage);

        if (currentPage > max) {
            currentPage = max;
        }

        this.pages.put(player.getUniqueId(), currentPage);
    }

    public static int getMaxPages(final int size, final int perPage) {
        return (int) Math.ceil((double) size / (double) perPage);
    }

    public static List<ItemStack> getPageItems(final List<ItemStack> items, final int currentPage, final int perPage) {
        final int size = items.size();

        int startIndex = getStartIndex(currentPage, perPage);
        int endIndex = getEndIndex(startIndex, perPage, size);

        final List<ItemStack> availableItems = new ArrayList<>();

        for (;startIndex < endIndex; startIndex++) {
            if (startIndex < size) {
                availableItems.add(items.get(startIndex));
            }
        }

        return availableItems;
    }

    public static int getEndIndex(final int startIndex, final int size, final int perPage) {
        return Math.min(startIndex + perPage, size);
    }

    public static int getStartIndex(final int currentPage, final int perPage) {
        return currentPage * perPage - perPage;
    }
}