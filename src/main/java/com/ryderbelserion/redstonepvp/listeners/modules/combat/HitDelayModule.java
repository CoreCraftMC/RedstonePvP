package com.ryderbelserion.redstonepvp.listeners.modules.combat;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.modules.ModuleHandler;
import com.ryderbelserion.redstonepvp.config.ConfigManager;
import com.ryderbelserion.redstonepvp.config.types.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class HitDelayModule extends ModuleHandler {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public String getName() {
        return "Combat Module";
    }

    @Override
    public boolean isEnabled() {
        return ConfigManager.getConfig().getProperty(Config.hit_delay) != -1;
    }

    @Override
    public void reload() {
        this.plugin.getServer().getOnlinePlayers().forEach(this::adjustHitDelay);
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        adjustHitDelay(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        adjustHitDelay(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        adjustHitDelay(event.getPlayer());
    }

    private void adjustHitDelay(final Player player) {
        if (!isEnabled()) {
            if (player.getMaximumNoDamageTicks() < 20) {
                player.setMaximumNoDamageTicks(20);
            }

            return;
        }

        player.setMaximumNoDamageTicks(ConfigManager.getConfig().getProperty(Config.hit_delay));
    }
}