package me.corecraft.redstonepvp.command.subs;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.paper.commands.PaperCommand;
import com.ryderbelserion.vital.paper.commands.context.PaperCommandInfo;
import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.managers.BeaconManager;
import me.corecraft.redstonepvp.managers.MenuManager;
import me.corecraft.redstonepvp.api.enums.Messages;
import me.corecraft.redstonepvp.managers.config.ConfigManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class CommandReload extends PaperCommand {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final PaperCommandInfo data) {
        this.plugin.getFileManager().reloadFiles();

        // Refresh the config.
        ConfigManager.refresh();

        // Refresh the gui's.
        MenuManager.populate();

        // Restart the tasks
        BeaconManager.startTasks(true);

        // Reload modules.
        this.plugin.getLoader().reload();

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
                    execute(new PaperCommandInfo(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final PaperCommand registerPermission() {
        final Permission permission = this.plugin.getServer().getPluginManager().getPermission(getPermission());

        if (permission == null) {
            this.plugin.getServer().getPluginManager().addPermission(new Permission(getPermission(), PermissionDefault.OP));
        }

        return this;
    }
}