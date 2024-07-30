package com.ryderbelserion.redstonepvp.api.core.v2.builders.gui;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.SimpleBuilder;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.BaseGui;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.components.InteractionComponent;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiType;
import java.util.Set;

/**
 * @author Matt
 */
public class GuiBuilder extends BaseGui {

    public GuiBuilder(final String title, final int rows, final Set<InteractionComponent> components) {
        super(title, rows, components);
    }

    public GuiBuilder(final String title, final GuiType guiType, final Set<InteractionComponent> components) {
        super(title, guiType, components);
    }

    public static SimpleBuilder gui() {
        return new SimpleBuilder();
    }
}