package com.ryderbelserion.redstonepvp.support;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.listeners.modules.PlayerPacketModule;
import com.ryderbelserion.vital.paper.plugins.interfaces.Plugin;

public class PacketEventsSupport implements Plugin {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public boolean isEnabled() {
        return this.plugin.getServer().getPluginManager().isPluginEnabled("packetevents");
    }

    @Override
    public String getName() {
        return "packetevents";
    }

    @Override
    public void add() {
        if (!isEnabled()) return;

        final PacketEventsAPI<?> packet = PacketEvents.getAPI();

        packet.getEventManager().registerListener(new PlayerPacketModule(), PacketListenerPriority.NORMAL);

        packet.init();
    }

    @Override
    public void remove() {
        if (!isEnabled()) return;

        PacketEvents.getAPI().terminate();
    }
}