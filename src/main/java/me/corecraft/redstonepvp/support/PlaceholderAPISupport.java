package me.corecraft.redstonepvp.support;

import ch.jalu.configme.SettingsManager;
import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.api.objects.beacons.Beacon;
import me.corecraft.redstonepvp.managers.BeaconManager;
import me.corecraft.redstonepvp.managers.config.ConfigManager;
import me.corecraft.redstonepvp.managers.config.impl.Config;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.corecraft.redstonepvp.utils.MiscUtils;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPISupport extends PlaceholderExpansion {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    private final SettingsManager config = ConfigManager.getConfig();

    @Override
    public @NotNull final String onRequest(@Nullable final OfflinePlayer player, @NotNull final String identifier) {
        if (player == null || identifier.isEmpty()) return "N/A";

        String lower = identifier.toLowerCase();

        final int requirement = this.config.getProperty(Config.beacon_drop_party_required_players);
        final int onlinePlayers = this.plugin.getServer().getOnlinePlayers().size();

        for (final Beacon beacon : BeaconManager.getBeaconData().values()) {
            if (lower.equalsIgnoreCase(beacon.getName() + "_timer")) {
                if (onlinePlayers == 0 || onlinePlayers < requirement) {
                    return this.config.getProperty(Config.beacon_drop_party_not_enough_players);
                }

                if (beacon.isActive()) {
                    return this.config.getProperty(Config.beacon_drop_party_on_going);
                }

                if (!this.config.getProperty(Config.beacon_drop_party_toggle) || beacon.isBroken()) {
                    return this.config.getProperty(Config.beacon_drop_party_out_of_order);
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