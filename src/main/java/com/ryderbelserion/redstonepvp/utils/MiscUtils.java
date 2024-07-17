package com.ryderbelserion.redstonepvp.utils;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class MiscUtils {

    private static final RedstonePvP plugin = RedstonePvP.getPlugin();

    public static @NotNull String location(@NotNull final Location location, final boolean getName) {
        String name = getName ? location.getWorld().getName() : String.valueOf(location.getWorld().getUID());

        return name + "," + location.getX() + "," + location.getY() + "," + location.getX();
    }

    public static @NotNull String location(@NotNull final Location location) {
        return location(location, false);
    }

    public static Location location(@NotNull final String location) {
        final String[] split = location.split(",");

        return new Location(plugin.getServer().getWorld(UUID.fromString(split[0])), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }
}