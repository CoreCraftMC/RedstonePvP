package com.ryderbelserion.redstonepvp.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.interfaces.Gui;
import com.ryderbelserion.redstonepvp.api.interfaces.GuiItem;
import com.ryderbelserion.redstonepvp.managers.MenuManager;
import com.ryderbelserion.redstonepvp.managers.config.ConfigManager;
import com.ryderbelserion.redstonepvp.managers.config.beans.ButtonProperty;
import com.ryderbelserion.redstonepvp.managers.config.beans.GuiProperty;
import com.ryderbelserion.redstonepvp.managers.config.types.Config;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.vital.paper.api.commands.Command;
import com.ryderbelserion.vital.paper.api.commands.CommandData;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BaseCommand extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();
    private final Server server = this.plugin.getServer();

    @Override
    public void execute(CommandData data) {
        if (!data.isPlayer()) {
            Messages.command_help.sendMessage(data.getCommandSender());

            return;
        }

        final GuiProperty property = MenuManager.getGui(ConfigManager.getConfig().getProperty(Config.main_menu_name));
        final Gui gui = Gui.gui().disableItemDrop().disableItemPlacement().disableItemSwap().disableItemTake()
                .setType(property.getGuiType())
                .setTitle(property.getGuiTitle())
                .setRows(property.getGuiRows())
                .create();

        final List<ButtonProperty> buttons = property.getButtons();

        buttons.forEach(button -> {
            final GuiItem item = gui.asGuiItem(button.build().getStack(), action -> {
                if (!(action.getWhoClicked() instanceof Player player)) return;

                button.getCommands().forEach(command -> this.server.dispatchCommand(this.server.getConsoleSender(), command.replaceAll("\\{player}", player.getName())));
                button.getMessages().forEach(message -> MiscUtils.message(player, message));

                button.getSoundProperty().playSound(player);
            });

            gui.setItem(button.getDisplayRow(), button.getDisplayColumn(), item);
        });

        gui.open(data.getPlayer());
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.access";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("redstonepvp")
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