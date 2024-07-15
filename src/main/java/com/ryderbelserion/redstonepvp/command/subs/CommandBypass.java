package com.ryderbelserion.redstonepvp.command.subs;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.core.command.objects.Command;
import com.ryderbelserion.redstonepvp.api.cache.CacheManager;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class CommandBypass extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandContext<CommandSourceStack> stack) {
        if (!(stack.getSource().getSender() instanceof Player player)) return;

        final boolean hasPlayer = CacheManager.containsPlayer(player);

        final String toggle = hasPlayer ? "enabled" : "disabled";

        if (hasPlayer) {
            CacheManager.removePlayer(player);
        } else {
            CacheManager.addPlayer(player);
        }

        Messages.item_frame_bypass.sendMessage(player, "{toggle}", toggle);
    }

    @Override
    public final String getPermission() {
        return "redstonepvp.frame.bypass";
    }

    @Override
    public final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("bypass")
                .requires(source -> source.getSender().hasPermission(getPermission()))
                .executes(context -> {
                    execute(context);

                    return 1;
                }).build();
    }

    @Override
    public Command registerPermission() {
        final Permission permission = this.plugin.getServer().getPluginManager().getPermission(getPermission());

        if (permission == null) {
            this.plugin.getServer().getPluginManager().addPermission(new Permission(getPermission(), PermissionDefault.OP));
        }

        return this;
    }
}