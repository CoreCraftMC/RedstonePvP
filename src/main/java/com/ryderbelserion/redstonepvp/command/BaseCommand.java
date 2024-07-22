package com.ryderbelserion.redstonepvp.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.core.builders.types.MainMenu;
import com.ryderbelserion.redstonepvp.api.core.command.Command;
import com.ryderbelserion.redstonepvp.api.core.command.CommandData;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class BaseCommand extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(CommandData data) {
        if (!data.isPlayer()) {
            Messages.not_a_player.sendMessage(data.getCommandSender());

            return;
        }

        final Player player = data.getPlayer();

        player.openInventory(new MainMenu(player).build().getInventory());
    }

    @Override
    public String getPermission() {
        return "redstonepvp.access";
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("redstonepvp")
                .requires(source -> source.getSender().hasPermission(getPermission()))
                .executes(context -> {
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