package com.ryderbelserion.redstonepvp.command.subs;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import dev.triumphteam.cmd.core.annotations.Command;

@Command(value = "redstonepvp")
public abstract class BaseCommand {

    protected final RedstonePvP plugin = RedstonePvP.getPlugin();

}