package com.ryderbelserion.redstonepvp.api.core.command;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandData {

    private final CommandContext<CommandSourceStack> context;

    public CommandData(final CommandContext<CommandSourceStack> context) {
        this.context = context;
    }

    public final CommandSourceStack getSource() {
        return this.context.getSource();
    }

    public final CommandSender getCommandSender() {
        return getSource().getSender();
    }

    public final Player getPlayer() {
        return (Player) getCommandSender();
    }

    public final boolean isPlayer() {
        return getCommandSender() instanceof Player;
    }
}