package com.ryderbelserion.redstonepvp.api.modules;

import org.bukkit.event.Listener;

public abstract class ModuleHandler implements Listener {

    public abstract String getName();

    public abstract boolean isEnabled();

    public abstract void reload();

}