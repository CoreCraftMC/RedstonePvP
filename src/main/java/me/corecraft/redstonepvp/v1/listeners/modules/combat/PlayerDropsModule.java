package me.corecraft.redstonepvp.v1.listeners.modules.combat;

import me.corecraft.redstonepvp.v1.utils.MiscUtils;
import me.corecraft.redstonepvp.v1.api.enums.Files;
import me.corecraft.redstonepvp.v1.api.objects.ItemDrop;
import com.ryderbelserion.vital.paper.api.commands.modules.interfaces.IPaperModule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PlayerDropsModule implements IPaperModule {

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

        final YamlConfiguration configuration = Files.player_drops.getConfiguration();

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