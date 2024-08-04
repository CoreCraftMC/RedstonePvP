package com.ryderbelserion.redstonepvp.support;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.objects.beacons.Beacon;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.redstonepvp.managers.config.ConfigManager;
import com.ryderbelserion.redstonepvp.managers.config.types.Config;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPISupport extends PlaceholderExpansion {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    private static final SettingsManager config = ConfigManager.getConfig();

    @Override
    public @NotNull final String onRequest(@Nullable final OfflinePlayer player, @NotNull final String identifier) {
        if (player == null || identifier.isEmpty()) return "N/A";

        String lower = identifier.toLowerCase();

        final int requirement = config.getProperty(Config.beacon_drop_party_required_players);
        final int onlinePlayers = this.plugin.getServer().getOnlinePlayers().size();

        for (final Beacon beacon : BeaconManager.getBeaconData().values()) {
            if (lower.equalsIgnoreCase(beacon.getName() + "_timer")) {
                if (onlinePlayers == 0 || onlinePlayers < requirement) {
                    return "<red>Not enough players</red>";
                }

                if (beacon.isActive()) {
                    return "<green>On Going</green>";
                }

                if (beacon.isBroken()) {
                    return "<red>Out of Order</red>";
                }

                return MiscUtils.convertTimeToString(beacon.getCalendar());
            }
        }

        return "N/A";
    }
    
    @Override
    public final boolean persist() {
        return true;
    }

    @Override
    public final boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull final String getIdentifier() {
        return this.plugin.getName().toLowerCase();
    }
    
    @Override
    public @NotNull final String getAuthor() {
        return "ryderbelserion";
    }
    
    @Override
    public @NotNull final String getVersion() {
        return this.plugin.getPluginMeta().getVersion();
    }
}