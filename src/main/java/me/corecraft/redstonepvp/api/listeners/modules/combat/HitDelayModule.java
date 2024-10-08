package me.corecraft.redstonepvp.api.listeners.modules.combat;

import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.managers.config.ConfigManager;
import me.corecraft.redstonepvp.managers.config.impl.Config;
import com.ryderbelserion.vital.paper.modules.interfaces.IPaperModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

public class HitDelayModule implements IPaperModule {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public @NotNull final String getName() {
        return "Combat Module";
    }

    @Override
    public final boolean isEnabled() {
        return ConfigManager.getConfig().getProperty(Config.hit_delay) != -1;
    }

    @Override
    public void reload() {
        this.plugin.getServer().getOnlinePlayers().forEach(this::adjustHitDelay);
    }

    @Override
    public void disable() {
        reload();
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

    private void adjustHitDelay(@NotNull final Player player) {
        if (!isEnabled()) {
            if (player.getMaximumNoDamageTicks() < 20) {
                player.setMaximumNoDamageTicks(20);
            }

            return;
        }

        player.setMaximumNoDamageTicks(ConfigManager.getConfig().getProperty(Config.hit_delay));
    }
}