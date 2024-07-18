package com.ryderbelserion.redstonepvp.api.enums;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("rawtypes")
public enum PersistentKeys {

    beacon_item("beacon_item", PersistentDataType.BOOLEAN);

    private @NotNull final RedstonePvP plugin = RedstonePvP.getPlugin();

    private final String NamespacedKey;
    private final PersistentDataType type;

    PersistentKeys(@NotNull final String NamespacedKey, @NotNull final PersistentDataType type) {
        this.NamespacedKey = NamespacedKey;
        this.type = type;
    }

    public @NotNull final NamespacedKey getNamespacedKey() {
        return new NamespacedKey(this.plugin, this.plugin.getName().toLowerCase() + "_" + this.NamespacedKey);
    }

    public @NotNull final PersistentDataType getType() {
        return this.type;
    }
}