package com.ryderbelserion.redstonepvp.command.types;

import com.ryderbelserion.redstonepvp.api.core.command.objects.Command;
import com.ryderbelserion.redstonepvp.api.core.command.objects.CommandContext;

public class CommandReload extends Command {

    public CommandReload() {
        this.aliases.add("reload");
    }

    @Override
    public void execute(CommandContext context, String[] args) {
        context.getSender().sendMessage("This is the reload command.");
    }
}