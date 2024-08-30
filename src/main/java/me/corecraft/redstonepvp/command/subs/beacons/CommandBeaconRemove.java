package me.corecraft.redstonepvp.command.subs.beacons;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.corecraft.redstonepvp.RedstonePvP;
import com.ryderbelserion.vital.paper.api.commands.Command;
import com.ryderbelserion.vital.paper.api.commands.CommandData;
import me.corecraft.redstonepvp.api.enums.Messages;
import me.corecraft.redstonepvp.managers.BeaconManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandBeaconRemove extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandData data) {
        final CommandSender sender = data.getCommandSender();

        final String name = data.getStringArgument("name");

        if (!BeaconManager.hasBeacon(name)) {
            Messages.beacon_drop_party_doesnt_exist.sendMessage(sender, "{name}", name);

            return;
        }

        Messages.beacon_drop_party_removed.sendMessage(sender, "{name}", name);

        BeaconManager.removeLocation(name);
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.beacon.remove";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("remove").requires(source -> source.getSender().hasPermission(getPermission()));

        final RequiredArgumentBuilder<CommandSourceStack, String> arg1 = argument("name", StringArgumentType.string()).suggests((ctx, builder) -> {
            BeaconManager.getBeaconData().keySet().forEach(builder::suggest);

            return builder.buildFuture();
        }).executes(context -> {
            execute(new CommandData(context));

            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });

        return root.then(arg1).build();
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