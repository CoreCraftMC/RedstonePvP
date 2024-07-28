package com.ryderbelserion.redstonepvp.utils;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.core.builders.InventoryBuilder;
import com.ryderbelserion.redstonepvp.api.core.builders.types.settings.beacon.BeaconMenu;
import com.ryderbelserion.redstonepvp.api.core.builders.types.settings.PlayersMenu;
import com.ryderbelserion.redstonepvp.api.core.builders.types.settings.SettingsMenu;
import com.ryderbelserion.redstonepvp.managers.ConfigManager;
import com.ryderbelserion.redstonepvp.managers.config.Config;
import com.ryderbelserion.redstonepvp.managers.config.beans.GuiProperty;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class MiscUtils {

    private static final RedstonePvP plugin = RedstonePvP.getPlugin();

    public static String location(@NotNull final Location location, boolean getName) {
        String name = getName ? location.getWorld().getName() : String.valueOf(location.getWorld().getUID());

        return name + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

    public static String location(@NotNull final Location location) {
        return location(location, false);
    }

    public static Location location(@NotNull final String location) {
        final String[] split = location.split(",");

        return new Location(plugin.getServer().getWorld(UUID.fromString(split[0])), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    public static InventoryBuilder buildBeaconMenu(@NotNull final Player player) {
        final GuiProperty gui = ConfigManager.getConfig().getProperty(Config.beacon_drop_menu);

        return new BeaconMenu(player, gui.getTitle(), gui.getSize()).build();
    }

    public static InventoryBuilder buildPlayerMenu(@NotNull final Player player) {
        final GuiProperty gui = ConfigManager.getConfig().getProperty(Config.players_menu);

        return new PlayersMenu(player, gui.getTitle(), gui.getSize()).build();
    }
}