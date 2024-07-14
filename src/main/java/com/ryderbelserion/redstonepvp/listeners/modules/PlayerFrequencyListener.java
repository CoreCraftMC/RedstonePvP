package com.ryderbelserion.redstonepvp.listeners.modules;

import com.ryderbelserion.redstonepvp.config.ConfigManager;
import com.ryderbelserion.redstonepvp.config.types.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerFrequencyListener implements Listener {

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        setHitDelay(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        setHitDelay(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        setHitDelay(event.getPlayer());
    }

    private void setHitDelay(final Player player) {
        player.setMaximumNoDamageTicks(ConfigManager.getConfig().getProperty(Config.hit_delay));
    }
}