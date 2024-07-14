package com.ryderbelserion.redstonepvp.api.core.command.objects;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandContext { ;

    private final List<String> args = new ArrayList<>();

    private final CommandSender sender;

    private String label;

    public CommandContext(CommandSender sender, String label, List<String> args) {
        this.args.addAll(args);

        this.sender = sender;
        this.label = label;
    }

    public final CommandSender getSender() {
        return this.sender;
    }

    public final List<String> getArgs() {
        return this.args;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public final String getLabel() {
        return this.label;
    }
}