package com.ryderbelserion.redstonepvp.api.core.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public abstract class Command {

    public abstract void execute(CommandData data);

    public abstract String getPermission();

    public abstract LiteralCommandNode<CommandSourceStack> literal();

    public abstract Command registerPermission();
}