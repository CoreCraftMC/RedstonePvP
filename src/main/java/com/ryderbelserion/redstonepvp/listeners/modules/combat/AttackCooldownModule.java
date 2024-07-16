package com.ryderbelserion.redstonepvp.listeners.modules.combat;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.core.command.modules.ModuleHandler;
import com.ryderbelserion.redstonepvp.managers.ConfigManager;
import com.ryderbelserion.redstonepvp.managers.config.Config;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class AttackCooldownModule extends ModuleHandler {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public String getName() {
        return "Attack Cooldown Module";
    }

    @Override
    public boolean isEnabled() {
        return ConfigManager.getConfig().getProperty(Config.attack_cooldown) != -1;
    }

    @Override
    public void reload() {
        this.plugin.getServer().getOnlinePlayers().forEach(this::adjustAttackSpeed);
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

    private void adjustAttackSpeed(final Player player) {
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