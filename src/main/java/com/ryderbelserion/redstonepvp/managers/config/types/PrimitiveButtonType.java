package com.ryderbelserion.redstonepvp.managers.config.types;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.properties.types.PropertyType;
import com.ryderbelserion.redstonepvp.managers.config.beans.ButtonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrimitiveButtonType implements PropertyType<ButtonProperty> {

    @Override
    public @Nullable ButtonProperty convert(@Nullable final Object object, @NotNull final ConvertErrorRecorder errorRecorder) {
        if (object instanceof ButtonProperty button) {
            return button;
        }

        return null;
    }

    @Override
    public @Nullable Object toExportValue(@Nullable final ButtonProperty value) {
        return value;
    }
}