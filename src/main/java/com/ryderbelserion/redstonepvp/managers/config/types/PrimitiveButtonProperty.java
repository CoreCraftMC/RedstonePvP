package com.ryderbelserion.redstonepvp.managers.config.types;

import ch.jalu.configme.properties.TypeBasedProperty;
import ch.jalu.configme.properties.types.PropertyType;
import org.jetbrains.annotations.NotNull;

public class PrimitiveButtonProperty extends TypeBasedProperty<PrimitiveButtonType> {

    public PrimitiveButtonProperty(@NotNull String path, @NotNull PrimitiveButtonType defaultValue, @NotNull PropertyType<PrimitiveButtonType> type) {
        super(path, defaultValue, type);
    }
}