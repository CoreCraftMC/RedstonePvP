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

    public final String getStringArgument(final String key) {
        return this.context.getArgument(key, String.class);
    }

    public final int getIntegerArgument(final String key) {
        return this.context.getArgument(key, Integer.class);
    }

    public final float getFloatArgument(final String key) {
        return this.context.getArgument(key, Float.class);
    }

    public final double getDoubleArgument(final String key) {
        return this.context.getArgument(key, Double.class);
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