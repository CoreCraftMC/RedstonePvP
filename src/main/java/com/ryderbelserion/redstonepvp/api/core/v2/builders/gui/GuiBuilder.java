package com.ryderbelserion.redstonepvp.api.core.v2.builders.gui;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.SimpleBuilder;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.BaseGui;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.components.InteractionComponent;
import java.util.Set;

public class GuiBuilder extends BaseGui {

    public GuiBuilder(final String title, final int rows, final Set<InteractionComponent> components) {
        super(title, rows, components);
    }

    public static SimpleBuilder gui() {
        return new SimpleBuilder();
    }
}