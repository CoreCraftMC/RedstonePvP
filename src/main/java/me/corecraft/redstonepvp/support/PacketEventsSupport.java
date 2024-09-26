package me.corecraft.redstonepvp.support;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.api.listeners.modules.PlayerPacketModule;
import com.ryderbelserion.vital.common.api.interfaces.IPlugin;
import org.jetbrains.annotations.NotNull;

public class PacketEventsSupport implements IPlugin {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public final boolean isEnabled() {
        return this.plugin.getServer().getPluginManager().isPluginEnabled("packetevents");
    }

    @Override
    public @NotNull final String getName() {
        return "packetevents";
    }

    @Override
    public void init() {
        if (!isEnabled()) return;

        final PacketEventsAPI<?> packet = PacketEvents.getAPI();

        packet.getEventManager().registerListener(new PlayerPacketModule(), PacketListenerPriority.NORMAL);

        packet.init();
    }

    @Override
    public void stop() {
        if (!isEnabled()) return;

        PacketEvents.getAPI().terminate();
    }
}