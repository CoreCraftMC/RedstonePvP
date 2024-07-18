package com.ryderbelserion.redstonepvp.command.subs.beacons;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.core.command.objects.Command;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.UUID;

import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandBeaconAdd extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandContext<CommandSourceStack> stack) {
        final CommandSender sender = stack.getSource().getSender();

        if (!(sender instanceof Player player)) {
            Messages.not_a_player.sendMessage(sender);

            return;
        }

        final Block block = player.getTargetBlock(null, 5);

        if (block.isEmpty()) {
            Messages.not_a_block.sendMessage(sender);

            return;
        }

        player.sendMessage("Type: " + block.getType());

        final String location = MiscUtils.location(block.getLocation());

        player.sendMessage("Location: " + location);

        //final String location = MiscUtils.location(block.getLocation());

        //if (BeaconManager.hasLocation(location, false)) {
        //    Messages.beacon_drop_exists.sendMessage(player);

        //    return;
        //}

        //Messages.beacon_drop_added.sendMessage(player);

        //BeaconManager.addLocation(UUID.randomUUID(), location, stack.getArgument("time", Integer.class));
    }

    @Override
    public final String getPermission() {
        return "redstonepvp.beacon.add";
    }

    @Override
    public final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("add")
                .requires(source -> source.getSender().hasPermission(getPermission()))
                .then(argument("time", IntegerArgumentType.integer(5, 60))
                .suggests((context, builder) -> {
                    for (int count = 1; count < 60; count++) {
                        builder.suggest(count);
                    }

                    return builder.buildFuture();
                })
                .executes(context -> {
                    execute(context);

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })).build();
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