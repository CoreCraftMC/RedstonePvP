package com.ryderbelserion.redstonepvp.command;

import com.ryderbelserion.redstonepvp.api.core.command.CommandHandler;
import com.ryderbelserion.redstonepvp.command.types.CommandReload;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Collections;

public class BaseCommand implements BasicCommand {

    private final CommandHandler commandHandler;

    public BaseCommand() {
        this.commandHandler = new CommandHandler();
        this.commandHandler.registerCommand("reload", new CommandReload());
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (args.length == 0) {
            return;
        }

        this.commandHandler.execute(stack.getSender(), args[0], args);
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        return this.commandHandler.feedback(stack.getSender(), this.commandHandler.getCommands(), args);
    }
}