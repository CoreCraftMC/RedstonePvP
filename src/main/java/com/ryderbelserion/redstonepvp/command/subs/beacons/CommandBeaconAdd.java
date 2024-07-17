package com.ryderbelserion.redstonepvp.command.subs.beacons;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.core.command.objects.Command;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.managers.data.Connector;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
            //todo() not looking at block.

            return;
        }

        // Convert block location to String.
        final String location = MiscUtils.location(block.getLocation());

        // Get the instance.
        final Connector instance = this.plugin.getDataManager().getConnector();

        //todo() make database query to check for location.

        // run off the main thread.
        CompletableFuture.runAsync(() -> {
            try (Connection connection = instance.getConnection()) {
                // it could be null for some reason, you never know!
                if (connection == null) return;

                final PreparedStatement statement = connection.prepareStatement("insert into beacon_locations(id, location) values (?, ?)");

                statement.setString(1, UUID.randomUUID().toString());
                statement.setString(2, location);

                statement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public final String getPermission() {
        return "redstonepvp.beacon.add";
    }

    @Override
    public final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("add")
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