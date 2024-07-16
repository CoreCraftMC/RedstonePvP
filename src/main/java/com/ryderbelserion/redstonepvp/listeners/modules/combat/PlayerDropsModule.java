package com.ryderbelserion.redstonepvp.listeners.modules.combat;

import com.ryderbelserion.redstonepvp.api.core.command.modules.ModuleHandler;
import com.ryderbelserion.redstonepvp.api.enums.Files;
import com.ryderbelserion.redstonepvp.api.objects.PlayerDrop;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerDropsModule extends ModuleHandler {

    private final List<PlayerDrop> drops = new ArrayList<>();

    public PlayerDropsModule() {
        reload();
    }

    @Override
    public String getName() {
        return "Player Drops Module";
    }

    @Override
    public boolean isEnabled() {
        return Files.player_drops.getConfiguration().getBoolean("settings.player-drops", false);
    }

    @Override
    public void reload() {
        this.drops.clear();

        final FileConfiguration configuration = Files.player_drops.getConfiguration();

        final ConfigurationSection section = configuration.getConfigurationSection("drops");

        if (section == null) return;

        section.getKeys(false).forEach(key -> {
            final ConfigurationSection subSection = section.getConfigurationSection(key);

            if (subSection == null) return;

            this.drops.add(new PlayerDrop(subSection));
        });
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player) || !(event.getEntity() instanceof Player target)) return;

        final PlayerDrop playerDrop = getDrop();

        if (playerDrop == null) {
            return;
        }

        player.getWorld().dropItem(target.getLocation(), playerDrop.getItem());
    }

    private PlayerDrop getDrop() {
        double weight = 0.0;

        for (PlayerDrop playerDrop : this.drops) {
            weight += playerDrop.getWeight();
        }

        int index = 0;

        for (double random = ThreadLocalRandom.current().nextDouble() * weight; index < this.drops.size() - 1; index++) {
            random -= this.drops.get(index).getWeight();

            if (random < 0.0) break;
        }

        return this.drops.get(index);
    }
}