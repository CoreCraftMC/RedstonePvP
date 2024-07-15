package com.ryderbelserion.redstonepvp.api.core.command.modules;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class EventRegistry {

    private final JavaPlugin plugin = RedstonePvP.getPlugin();

    private final List<Listener> listeners = new ArrayList<>();

    public void addListener(final Listener listener) {
        if (this.listeners.contains(listener)) return;

        this.listeners.add(listener);

        this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
    }

    public void removeListener(final Listener listener) {
        if (!this.listeners.contains(listener)) return;

        this.listeners.remove(listener);

        HandlerList.unregisterAll(listener);
    }
}