package com.ryderbelserion.redstonepvp.listeners.modules.combat;

import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import com.ryderbelserion.vital.paper.api.commands.modules.ModuleHandler;
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
        this.drops.clear();
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player) || !(event.getEntity() instanceof Player target)) return;

        if (!isEnabled()) return;

        // Don't spawn drops if they can't attack.
        if (MiscUtils.cantDamage(player, target)) return;

        MiscUtils.getDrop(target.getLocation(), this.drops);
    }
}