package me.corecraft.redstonepvp.api.cache;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CacheListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Make sure to remove on quit just in case.
        CacheManager.removePlayer(event.getPlayer());
    }
}