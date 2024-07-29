package com.ryderbelserion.redstonepvp.api.core.v2.builders;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.BaseGuiBuilder;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.GuiBuilder;
import org.jetbrains.annotations.NotNull;

public class SimpleBuilder extends BaseGuiBuilder<GuiBuilder, SimpleBuilder> {

    @Override
    public @NotNull GuiBuilder create() {
        return new GuiBuilder(getTitle(), getRows(), getInteractionComponents());
    }
}