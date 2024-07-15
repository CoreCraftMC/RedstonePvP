package com.ryderbelserion.redstonepvp.cache;

import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CacheManager {

    private static final List<UUID> cache = new ArrayList<>();

    public static void addPlayer(final Player player) {
        cache.add(player.getUniqueId());
    }

    public static void removePlayer(final Player player) {
        cache.remove(player.getUniqueId());
    }

    public static boolean containsPlayer(final Player player) {
        return cache.contains(player.getUniqueId());
    }
}