package com.ryderbelserion.redstonepvp.command.subs;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.managers.MenuManager;
import com.ryderbelserion.vital.paper.commands.Command;
import com.ryderbelserion.vital.paper.commands.CommandData;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.managers.config.ConfigManager;
import com.ryderbelserion.vital.paper.commands.modules.ModuleLoader;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class CommandReload extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandData data) {
        // Refresh the config.
        ConfigManager.refresh();

        // Reload modules.
        this.plugin.getRedstone().apply(redstone -> {
            final ModuleLoader loader = redstone.getModuleLoader();

            loader.reload();
        });

        // Send the message.
        Messages.reloaded_plugin.sendMessage(data.getCommandSender());
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.reload";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("reload")
                .requires(source -> source.getSender().hasPermission(getPermission()))
                .executes(context -> {
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