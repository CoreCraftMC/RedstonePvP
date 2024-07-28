package com.ryderbelserion.redstonepvp.managers;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.redstonepvp.api.core.builders.types.settings.SettingsMenu;
import com.ryderbelserion.redstonepvp.managers.config.Config;
import com.ryderbelserion.redstonepvp.managers.config.beans.GuiProperty;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PaginationManager {

    private final SettingsManager config = ConfigManager.getConfig();

    private static final Map<UUID, Integer> pages = new HashMap<>();

    /**
     * Goes to the next page
     *
     * @param player {@link Player}
     * @param currentPage page number
     * @param perPage max per page
     * @param size list size
     */
    public static void nextPage(final Player player, final int currentPage, final int perPage, final int size) {
        setPage(player, currentPage, perPage, size);

        buildInventory(player, currentPage);
    }

    /**
     * Goes back a page
     *
     * @param player {@link Player}
     * @param currentPage page number
     * @param perPage max per page
     * @param size list size
     */
    public static void backPage(final Player player, final int currentPage, final int perPage, final int size) {
        setPage(player, currentPage, perPage, size);

        buildInventory(player, currentPage);
    }

    /**
     * Build the tier menu
     *
     * @param player {@link Player}
     * @param page page number
     */
    public static void buildInventory(final Player player, final int page) {
        final GuiProperty gui = ConfigManager.getConfig().getProperty(Config.plugin_settings_menu);

        player.openInventory(new SettingsMenu(player, gui.getTitle(), gui.getSize(), page <= 0 ? getPage(player) : page).build().getInventory());
    }

    /**
     * Remove the person viewing the gui.
     *
     * @param player {@link Player}
     */
    public static void remove(final Player player) {
        pages.remove(player.getUniqueId());
    }

    /**
     * Get the page
     *
     * @param player {@link Player}
     * @return the page
     */
    public static int getPage(final Player player) {
        return pages.getOrDefault(player.getUniqueId(), 1);
    }

    /**
     * Sets the page based on the size of the list.
     *
     * @param player {@link Player}
     * @param currentPage page number
     * @param perPage max per page
     * @param size list size
     */
    public static void setPage(final Player player, int currentPage, final int perPage, final int size) {
        int max = getMaxPages(size, perPage);

        if (currentPage > max) {
            currentPage = max;
        }

        pages.put(player.getUniqueId(), currentPage);
    }

    public static int getMaxPages(final int size, final int perPage) {
        return (int) Math.ceil((double) size / (double) perPage);
    }

    public static List<ItemStack> getPageItems(final List<ItemStack> items, int currentPage, int maxSlots) {
        final int size = items.size();

        if (currentPage <= 0) currentPage = 1;

        int startIndex = getStartIndex(currentPage, maxSlots);
        int endIndex = getEndIndex(startIndex, maxSlots, size);

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