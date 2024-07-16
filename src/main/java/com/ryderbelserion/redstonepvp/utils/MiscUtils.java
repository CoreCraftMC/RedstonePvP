package com.ryderbelserion.redstonepvp.utils;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class MiscUtils {

    public static @NotNull String location(@NotNull final Location location, boolean getName) {
        String name = getName ? location.getWorld().getName() : String.valueOf(location.getWorld().getUID());

        return name + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

    public static @NotNull String location(@NotNull final Location location) {
        return location(location, false);
    }
}