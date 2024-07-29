package com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.components.InteractionComponent;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiItem;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.types.IPaginatedGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void addItem(@NotNull final GuiItem guiItem) {
        this.pageItems.add(guiItem);
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