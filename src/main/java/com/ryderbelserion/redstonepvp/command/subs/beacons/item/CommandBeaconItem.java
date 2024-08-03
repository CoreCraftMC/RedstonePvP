package com.ryderbelserion.redstonepvp.command.subs.beacons.item;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.builders.gui.PaginatedGui;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.api.interfaces.Gui;
import com.ryderbelserion.redstonepvp.managers.MenuManager;
import com.ryderbelserion.redstonepvp.managers.config.beans.ButtonProperty;
import com.ryderbelserion.redstonepvp.managers.config.beans.GuiProperty;
import com.ryderbelserion.redstonepvp.utils.ItemUtils;
import com.ryderbelserion.vital.paper.commands.Command;
import com.ryderbelserion.vital.paper.commands.CommandData;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class CommandBeaconItem extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandData data) {
        if (!data.isPlayer()) {
            Messages.not_a_player.sendMessage(data.getCommandSender());

            return;
        }

        final Player player = data.getPlayer();

        final GuiProperty property = MenuManager.getGui("item-menu");
        final @NotNull PaginatedGui item_gui = Gui.paginated().disableItemDrop().disableItemPlacement().disableItemSwap().disableItemTake()
                .setTitle(property.getGuiTitle())
                .setRows(property.getGuiRows())
                .create();

        ItemUtils.addButtons(property, player, item_gui);

        final ButtonProperty button = property.getButtons().getFirst();
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.beacon.item.access";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("item")
                .requires(source -> source.getSender().hasPermission(getPermission()))
                .executes(context -> {
                    execute(new CommandData(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
                .then(new CommandBeaconItemRemove().registerPermission().literal())
                .then(new CommandBeaconItemUpdate().registerPermission().literal())
                .then(new CommandBeaconItemSet().registerPermission().literal())
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