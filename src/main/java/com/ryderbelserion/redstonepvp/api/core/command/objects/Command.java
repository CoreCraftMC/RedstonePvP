package com.ryderbelserion.redstonepvp.api.core.command.objects;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    protected List<String> aliases = new ArrayList<>();

    public abstract void execute(CommandContext context, String[] args);

    public final List<String> getAliases() {
        return this.aliases;
    }
}