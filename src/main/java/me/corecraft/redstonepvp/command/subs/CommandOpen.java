package me.corecraft.redstonepvp.command.subs;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.paper.commands.PaperCommand;
import com.ryderbelserion.vital.paper.commands.context.PaperCommandInfo;
import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.api.enums.Messages;
import me.corecraft.redstonepvp.managers.MenuManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandOpen extends PaperCommand {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();
    private final Server server = this.plugin.getServer();

    @Override
    public void execute(final PaperCommandInfo data) {
        final String arg1 = data.getStringArgument("name");

        if (arg1.isEmpty() || !MenuManager.getGuis().containsKey(arg1)) {
            Messages.menu_not_found.sendMessage(data.getCommandSender(), "{name}", arg1);

            return;
        }

        final String arg2 = data.getStringArgument("player");

        final Player player = this.server.getPlayer(arg2);

        if (player == null) {
            Messages.menu_not_found.sendMessage(data.getCommandSender(), "{name}", arg2);

            return;
        }

        MenuManager.openMenu(player, arg1);
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.open";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("open").requires(source -> source.getSender().hasPermission(getPermission()));

        final RequiredArgumentBuilder<CommandSourceStack, String> arg1 = argument("player", StringArgumentType.string()).suggests((ctx, builder) -> {
            this.plugin.getServer().getOnlinePlayers().forEach(player -> builder.suggest(player.getName()));

            return builder.buildFuture();
        });

        final RequiredArgumentBuilder<CommandSourceStack, String> arg2 = argument("name", StringArgumentType.string()).suggests((ctx, builder) -> {
            MenuManager.getGuis().keySet().forEach(builder::suggest);

            return builder.buildFuture();
        }).executes(context -> {
            execute(new PaperCommandInfo(context));

            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });

        return root.then(arg1.then(arg2)).build();
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