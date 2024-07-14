package com.ryderbelserion.redstonepvp.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.core.command.objects.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class BaseCommand extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(CommandContext<CommandSourceStack> stack) {
        stack.getSource().getSender().sendMessage("This is the base command.");
    }

    @Override
    public String getPermission() {
        return "redstonepvp.access";
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("redstonepvp").executes(context -> {
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