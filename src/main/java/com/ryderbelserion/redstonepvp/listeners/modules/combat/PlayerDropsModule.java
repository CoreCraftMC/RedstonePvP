package com.ryderbelserion.redstonepvp.listeners.modules.combat;

import com.ryderbelserion.vital.paper.commands.modules.ModuleHandler;
import com.ryderbelserion.redstonepvp.api.enums.Files;
import com.ryderbelserion.redstonepvp.api.objects.ItemDrop;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerDropsModule extends ModuleHandler {

    private final List<ItemDrop> drops = new ArrayList<>();

    public PlayerDropsModule() {
        enable();
    }

    @Override
    public @NotNull final String getName() {
        return "Player Drops Module";
    }

    @Override
    public final boolean isEnabled() {
        return Files.player_drops.getConfiguration().getBoolean("settings.player-drops", false);
    }

    @Override
    public void enable() {
        reload();
    }

    @Override
    public void reload() {
        this.drops.clear();

        if (!isEnabled()) return;

        final FileConfiguration configuration = Files.player_drops.getConfiguration();

        final ConfigurationSection section = configuration.getConfigurationSection("drops");

        if (section == null) return;

        section.getKeys(false).forEach(key -> {
            final ConfigurationSection subSection = section.getConfigurationSection(key);

            if (subSection == null) return;

            this.drops.add(new ItemDrop(subSection));
        });
    }

    @Override
    public void disable() {
        reload();
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player) || !(event.getEntity() instanceof Player target)) return;

        if (!isEnabled()) return;

        final ItemDrop itemDrop = getDrop();

        if (itemDrop == null) return;

        player.getWorld().dropItem(target.getLocation(), itemDrop.getItem());
    }

    private ItemDrop getDrop() {
        double weight = 0.0;

        for (ItemDrop itemDrop : this.drops) {
            weight += itemDrop.getWeight();
        }

        int index = 0;

        for (double random = ThreadLocalRandom.current().nextDouble() * weight; index < this.drops.size() - 1; index++) {
            random -= this.drops.get(index).getWeight();

            if (random < 0.0) break;
        }

        return this.drops.get(index);
    }
}