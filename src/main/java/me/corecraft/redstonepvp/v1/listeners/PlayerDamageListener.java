package me.corecraft.redstonepvp.v1.listeners;

import me.corecraft.redstonepvp.v1.managers.config.ConfigManager;
import me.corecraft.redstonepvp.v1.managers.config.types.Config;
import me.corecraft.redstonepvp.v1.utils.MiscUtils;
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

        // don't spawn blood particle if they can't damage the target.
        if (MiscUtils.cantDamage(player, target)) return;

        final int count = ConfigManager.getConfig().getProperty(Config.blood_effect);

        if (count != -1) {
            player.spawnParticle(Particle.BLOCK, target.getLocation(), count, Material.REDSTONE_BLOCK.createBlockData());
        }
    }
}