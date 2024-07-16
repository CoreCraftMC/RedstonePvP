package com.ryderbelserion.redstonepvp.api.core.command.objects;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public abstract class Command {

    public abstract void execute(CommandContext<CommandSourceStack> stack);

    public abstract String getPermission();

    public abstract LiteralCommandNode<CommandSourceStack> literal();

    public abstract Command registerPermission();
}