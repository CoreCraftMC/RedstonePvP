package com.ryderbelserion.redstonepvp.api.core.command;

import com.ryderbelserion.redstonepvp.api.core.command.objects.Command;
import com.ryderbelserion.redstonepvp.api.core.command.objects.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler {

    private final Map<String, Command> commands = new HashMap<>();

    public void execute(CommandSender sender, String label, String[] args) {
        this.commands.get(label).execute(new CommandContext(
                sender,
                label,
                List.of(args)
        ), args);
    }

    //todo() add permission check which filters out unwanted args.
    //todo() add subcommand support for args above the label.
    public final Collection<String> feedback(CommandSender sender, Map<String, Command> cache, @NotNull String[] args) {
        switch (args.length) {
            case 0 -> {
                return cache.keySet();
            }

            //todo() add multiple arg support.
            case 1 -> {
                return Collections.emptyList();
            }

            default -> {
                return Collections.emptyList();
            }
        }
    }

    public void registerCommand(final String label, final Command command) {
        this.commands.put(label, command);
    }

    public void unregisterCommand(final String label) {
        this.commands.remove(label);
    }

    public final Command getCommand(final String label) {
        return this.commands.get(label);
    }

    public final Map<String, Command> getCommands() {
        return Collections.unmodifiableMap(this.commands);
    }
}