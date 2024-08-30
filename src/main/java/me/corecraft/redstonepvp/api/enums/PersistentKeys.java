package me.corecraft.redstonepvp.api.enums;

import me.corecraft.redstonepvp.RedstonePvP;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("rawtypes")
public enum PersistentKeys {

    beacon_item("beacon_item", PersistentDataType.STRING),
    player_item("players_item", PersistentDataType.STRING),
    setting_item("settings_item", PersistentDataType.STRING),
    beacon_nuke("beacon_nuke", PersistentDataType.STRING),
    beacon_drop("beacon_drop", PersistentDataType.STRING),
    beacon_location("beacon_location", PersistentDataType.STRING),
    beacon_uuid("beacon_uuid", PersistentDataType.STRING),

    next_page("next_page", PersistentDataType.INTEGER),
    back_page("back_page", PersistentDataType.INTEGER);

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