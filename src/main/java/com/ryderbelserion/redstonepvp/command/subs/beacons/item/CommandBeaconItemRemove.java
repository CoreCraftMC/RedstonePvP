package com.ryderbelserion.redstonepvp.command.subs.beacons.item;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.api.objects.beacons.BeaconDrop;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.vital.paper.commands.Command;
import com.ryderbelserion.vital.paper.commands.CommandData;
import com.ryderbelserion.vital.paper.util.ItemUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandBeaconItemRemove extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandData data) {
        if (!data.isPlayer()) {
            Messages.not_a_player.sendMessage(data.getCommandSender());

            return;
        }

        final Player player = data.getPlayer();

        final String name = data.getStringArgument("name");

        if (!BeaconManager.hasValue(name)) {
            Messages.beacon_location_doesnt_exist.sendMessage(player, "{name}", name);

            return;
        }

        final int position = data.getIntegerArgument("position");

        if (!BeaconManager.getPositionsById(name).contains(position)) {
            Messages.beacon_drop_doesnt_exist.sendMessage(player, new HashMap<>() {{
                put("{position}", String.valueOf(position));
                put("{name}", name);
            }});

            return;
        }

        //BeaconManager.getBeacon(name).getDrop().removeItem(position);

        Messages.beacon_drop_removed.sendMessage(player, new HashMap<>() {{
            put("{position}", String.valueOf(position));
            put("{name}", name);
        }});
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.beacon.item.remove";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("remove");

        return root.requires(source -> source.getSender().hasPermission(getPermission()))
                .then(argument("name", StringArgumentType.string())
                        .suggests((ctx, builder) -> {
                            BeaconManager.getBeaconData().keySet().forEach(builder::suggest);

                            return builder.buildFuture();
                        })
                .then(argument("position", IntegerArgumentType.integer())
                        .suggests((ctx, builder) -> {
                           final String name = ctx.getLastChild().getArgument("name", String.class);

                           BeaconManager.getPositionsById(name).forEach(builder::suggest);

                           return builder.buildFuture();
                        })
                .executes(context -> {
                   execute(new CommandData(context));

                   return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                }))).build();
    }

    @Override
    public @NotNull final Command registerPermission() {
        final Permission permission = this.plugin.getServer().getPluginManager().getPermission(getPermission());

        if (permission == null) {
            this.plugin.getServer().getPluginManager().addPermission(new Permission(getPermission(), PermissionDefault.OP));
        }

        return this;
    }
}