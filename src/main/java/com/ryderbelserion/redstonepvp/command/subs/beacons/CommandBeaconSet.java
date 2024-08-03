package com.ryderbelserion.redstonepvp.command.subs.beacons;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.vital.paper.commands.Command;
import com.ryderbelserion.vital.paper.commands.CommandData;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandBeaconSet extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandData data) {
        if (!data.isPlayer()) {
            Messages.not_a_player.sendMessage(data.getCommandSender());

            return;
        }

        final Player player = data.getPlayer();

        final Block block = player.getTargetBlock(null, 5);

        if (block.isEmpty()) {
            Messages.not_a_block.sendMessage(player);

            return;
        }

        final String location = MiscUtils.location(block.getLocation());

        final String name = data.getStringArgument("name");

        if (BeaconManager.hasBeacon(name) || BeaconManager.hasLocation(location)) {
            Messages.beacon_location_exists.sendMessage(player, "{name}", name);

            return;
        }

        Messages.beacon_location_added.sendMessage(player, "{name}", name);

        BeaconManager.addLocation(name, location, data.getIntegerArgument("time"));
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.beacon.set";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("set")
                .requires(source -> source.getSender().hasPermission(getPermission()))
                .then(argument("name", StringArgumentType.string())
                        .suggests((ctx, builder) -> suggestNames(builder))
                .then(argument("time", IntegerArgumentType.integer())
                        .suggests((ctx, builder) -> suggestIntegers(builder))
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