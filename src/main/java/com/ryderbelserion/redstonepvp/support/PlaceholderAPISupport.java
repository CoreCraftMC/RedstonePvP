package com.ryderbelserion.redstonepvp.support;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.objects.beacons.Beacon;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPISupport extends PlaceholderExpansion {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public @NotNull final String onRequest(@Nullable final OfflinePlayer player, @NotNull final String identifier) {
        if (player == null || identifier.isEmpty()) return "N/A";

        String lower = identifier.toLowerCase();

        for (final Beacon beacon : BeaconManager.getBeaconData().values()) {
            if (lower.equalsIgnoreCase(beacon.getName() + "_timer")) {
                if (beacon.isActive()) {
                    return "On Going";
                }

                if (beacon.isBroken()) {
                    return "Out of Order";
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