package com.ryderbelserion.redstonepvp.command.subs;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.builders.gui.PaginatedGui;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.api.interfaces.Gui;
import com.ryderbelserion.redstonepvp.api.interfaces.GuiItem;
import com.ryderbelserion.redstonepvp.managers.MenuManager;
import com.ryderbelserion.redstonepvp.managers.config.ConfigManager;
import com.ryderbelserion.redstonepvp.managers.config.beans.ButtonProperty;
import com.ryderbelserion.redstonepvp.managers.config.beans.GuiProperty;
import com.ryderbelserion.redstonepvp.managers.config.types.Config;
import com.ryderbelserion.redstonepvp.utils.ItemUtils;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import com.ryderbelserion.vital.paper.commands.Command;
import com.ryderbelserion.vital.paper.commands.CommandData;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandOpen extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();
    private final Server server = this.plugin.getServer();

    @Override
    public void execute(final CommandData data) {
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

        final GuiProperty property = MenuManager.getGui(arg1);

        if (arg1.equalsIgnoreCase(ConfigManager.getConfig().getProperty(Config.main_menu_name))) {
            final Gui gui = Gui.gui().disableItemDrop().disableItemPlacement().disableItemSwap().disableItemTake()
                    .setTitle(property.getGuiTitle())
                    .setRows(property.getGuiRows())
                    .setType(property.getGuiType())
                    .create();

            final List<ButtonProperty> buttons = property.getButtons();

            buttons.forEach(button -> {
                final GuiItem item = gui.asGuiItem(button.build().getStack(), action -> {
                    if (!(action.getWhoClicked() instanceof Player clicker)) return;

                    button.getCommands().forEach(command -> this.server.dispatchCommand(this.server.getConsoleSender(), command.replaceAll("\\{player}", clicker.getName())));
                    button.getMessages().forEach(message -> MiscUtils.message(clicker, message));

                    button.getSoundProperty().playSound(clicker);
                });

                gui.setItem(button.getDisplayRow(), button.getDisplayColumn(), item);
            });

            gui.open(player);

            return;
        }

        final PaginatedGui gui = Gui.paginated().disableItemDrop().disableItemPlacement().disableItemSwap().disableItemTake()
                .setTitle(property.getGuiTitle())
                .setRows(property.getGuiRows())
                .create();

        ItemUtils.addButtons(property, player, gui);

        gui.open(player, 1);
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
            execute(new CommandData(context));

            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });

        return root.then(arg1.then(arg2)).build();
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