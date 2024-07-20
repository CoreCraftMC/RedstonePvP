package com.ryderbelserion.redstonepvp.command.subs.root;

import com.ryderbelserion.redstonepvp.api.cache.CacheManager;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.command.subs.BaseCommand;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public class CommandBypass extends BaseCommand {

    @Command("bypass")
    @Permission(value = "redstonepvp.frame.bypass", def = PermissionDefault.OP)
    public void bypass(Player player) {
        final boolean hasPlayer = CacheManager.containsPlayer(player);

        final String toggle = hasPlayer ? "enabled" : "disabled";

        if (hasPlayer) {
            CacheManager.removePlayer(player);
        } else {
            CacheManager.addPlayer(player);
        }

        Messages.item_frame_bypass.sendMessage(player, "{toggle}", toggle);
    }
}