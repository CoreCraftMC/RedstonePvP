package com.ryderbelserion.redstonepvp.api.core.v2.builders.gui;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.PaginatedBuilder;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.SimpleBuilder;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.BaseGui;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.components.InteractionComponent;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiType;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

/**
 * @author Matt
 */
public class GuiBuilder extends BaseGui {

    /**
     * Main constructor for the GUI.
     *
     * @param rows the amount of rows the GUI should have
     * @param title the GUI's title using {@link String}
     * @param components a set containing the {@link InteractionComponent} this GUI should use
     * @author SecretX
     */
    public GuiBuilder(final String title, final int rows, final Set<InteractionComponent> components) {
        super(title, rows, components);
    }

    /**
     * Alternative constructor that takes both a {@link GuiType} and a set of {@link InteractionComponent}.
     *
     * @param guiType the {@link GuiType} to be used
     * @param title the GUI's title using {@link String}
     * @param components a set containing the {@link InteractionComponent} this GUI should use
     * @author SecretX
     */
    public GuiBuilder(final String title, final GuiType guiType, final Set<InteractionComponent> components) {
        super(title, guiType, components);
    }

    /**
     * Creates a {@link SimpleBuilder} to build a {@link GuiBuilder}
     *
     * @param type the {@link GuiType} to be used
     * @return a {@link SimpleBuilder}
     */
    public static SimpleBuilder gui(@NotNull final GuiType type) {
        return new SimpleBuilder(type);
    }

    /**
     * Creates a {@link SimpleBuilder} with CHEST as the {@link GuiType}.
     *
     * @return a chest {@link SimpleBuilder}
     */
    public static SimpleBuilder gui() {
        return gui(GuiType.CHEST);
    }

    /**
     * Creates a {@link PaginatedBuilder} to build a {@link com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.PaginatedGui}.
     *
     * @return a {@link PaginatedBuilder}
     */
    public static PaginatedBuilder paginated() {
        return new PaginatedBuilder();
    }
}