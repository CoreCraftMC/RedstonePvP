package me.corecraft.redstonepvp.v1.command.subs.beacons;

import com.mojang.brigadier.tree.LiteralCommandNode;
import me.corecraft.redstonepvp.v1.RedstonePvP;
import me.corecraft.redstonepvp.v1.managers.MenuManager;
import com.ryderbelserion.vital.paper.api.commands.Command;
import com.ryderbelserion.vital.paper.api.commands.CommandData;
import me.corecraft.redstonepvp.v1.api.enums.Messages;
import me.corecraft.redstonepvp.v1.command.subs.beacons.item.CommandBeaconItem;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class CommandBeacon extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandData data) {
        if (!data.isPlayer()) {
            Messages.not_a_player.sendMessage(data.getCommandSender());

            return;
        }

        MenuManager.openMenu(data.getPlayer(), "beacon-menu");
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.beacon.access";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("beacon")
                .requires(source -> source.getSender().hasPermission(getPermission()))
                .executes(context -> {
                    execute(new CommandData(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })

                .then(new CommandBeaconSet().registerPermission().literal())
                .then(new CommandBeaconRemove().registerPermission().literal())
                .then(new CommandBeaconTIme().registerPermission().literal())

                // the item subcommand
                .then(new CommandBeaconItem().registerPermission().literal())
                .build();
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