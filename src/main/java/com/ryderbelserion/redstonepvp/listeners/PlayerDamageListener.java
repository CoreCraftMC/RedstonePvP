package com.ryderbelserion.redstonepvp.listeners;

import com.ryderbelserion.redstonepvp.managers.ConfigManager;
import com.ryderbelserion.redstonepvp.managers.config.Config;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player) || !(event.getEntity() instanceof Player target)) return;

        final int count = ConfigManager.getConfig().getProperty(Config.blood_effect);

        if (count != -1) {
            player.spawnParticle(Particle.BLOCK, target.getLocation(), count, Material.REDSTONE_BLOCK.createBlockData());
        }
    }
}