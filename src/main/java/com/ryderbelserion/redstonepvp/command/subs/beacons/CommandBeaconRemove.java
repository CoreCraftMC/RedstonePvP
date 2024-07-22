package com.ryderbelserion.redstonepvp.command.subs.beacons;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.core.command.Command;
import com.ryderbelserion.redstonepvp.api.core.command.CommandData;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandBeaconRemove extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandData data) {
        final CommandSender sender = data.getCommandSender();

        final String name = data.getStringArgument("name");

        if (!BeaconManager.hasValue(name)) {
            Messages.beacon_location_doesnt_exist.sendMessage(sender, "{name}", name);

            return;
        }

        Messages.beacon_location_removed.sendMessage(sender, "{name}", name);

        BeaconManager.removeLocation(name);
    }

    @Override
    public final String getPermission() {
        return "redstonepvp.beacon.remove";
    }

    @Override
    public final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("remove")
                .requires(source -> source.getSender().hasPermission(getPermission()))
                .then(argument("name", StringArgumentType.string())
                        .suggests((ctx, builder) -> {
                            BeaconManager.getLocations(false).forEach(builder::suggest);

                            return builder.buildFuture();
                        })
                ).executes(context -> {
                    execute(new CommandData(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
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