package me.corecraft.redstonepvp.api.listeners.modules.combat;

import com.ryderbelserion.vital.paper.modules.interfaces.IPaperModule;
import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.managers.config.ConfigManager;
import me.corecraft.redstonepvp.managers.config.impl.Config;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

public class AttackCooldownModule implements IPaperModule {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public @NotNull final String getName() {
        return "Attack Cooldown Module";
    }

    @Override
    public final boolean isEnabled() {
        return ConfigManager.getConfig().getProperty(Config.attack_cooldown) != -1;
    }

    @Override
    public void reload() {
        this.plugin.getServer().getOnlinePlayers().forEach(this::adjustAttackSpeed);
    }

    @Override
    public void disable() {
        reload();
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        adjustAttackSpeed(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        adjustAttackSpeed(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        adjustAttackSpeed(event.getPlayer());
    }

    private void adjustAttackSpeed(@NotNull final Player player) {
        final AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);

        if (attribute == null) return;

        if (!isEnabled()) {
            final double baseValue = attribute.getBaseValue();

            if (baseValue > 4.0 || baseValue < 4.0) {
                attribute.setBaseValue(attribute.getDefaultValue());
            }

            return;
        }

        final int attackCooldown = ConfigManager.getConfig().getProperty(Config.attack_cooldown);

        attribute.setBaseValue(attackCooldown);
    }
}