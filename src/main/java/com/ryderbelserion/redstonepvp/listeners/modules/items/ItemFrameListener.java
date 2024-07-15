package com.ryderbelserion.redstonepvp.listeners.modules.items;

import com.ryderbelserion.redstonepvp.cache.CacheManager;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

public class ItemFrameListener implements Listener {

    @EventHandler
    public void onItemFrameDamage(EntityDamageByEntityEvent event) {
        // Check if it's an item frame.
        if (!(event.getEntity() instanceof ItemFrame)) return;

        // Check if player.
        if (event.getDamager() instanceof Player player) {
            // If the cache contains a player, we are editing the frames.
            if (CacheManager.containsPlayer(player)) return;
        }

        // If it's a projectile, remove it.
        if (event.getDamager() instanceof Projectile) {
            event.getDamager().remove();
        }

        // Cancel the event.
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemFrameRotate(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame)) return;

        // If the cache contains a player, we are editing the frames.
        if (CacheManager.containsPlayer(event.getPlayer())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onItemFrameInteract(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame itemFrame)) return;

        final Player player = event.getPlayer();
        final Inventory inventory = player.getInventory();

        // If inventory is not empty, we return.
        if (inventory.firstEmpty() == -1) {
            player.spawnParticle(Particle.DUST, itemFrame.getLocation(), 1, new Particle.DustOptions(Color.RED, 1));

            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);

            return;
        }

        inventory.addItem(itemFrame.getItem());

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
    }
}