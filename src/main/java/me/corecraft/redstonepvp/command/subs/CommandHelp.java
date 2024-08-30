package me.corecraft.redstonepvp.command.subs;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.api.enums.Messages;
import com.ryderbelserion.vital.paper.api.commands.Command;
import com.ryderbelserion.vital.paper.api.commands.CommandData;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class CommandHelp extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandData data) {
        Messages.command_help.sendMessage(data.getCommandSender());
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.help";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("help").requires(source -> source.getSender().hasPermission(getPermission()));

        return root.executes(context -> {
            execute(new CommandData(context));

            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        }).build();
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