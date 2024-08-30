package me.corecraft.redstonepvp.listeners.modules;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect;
import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.managers.config.ConfigManager;
import me.corecraft.redstonepvp.managers.config.types.Config;
import java.util.List;

public class PlayerPacketModule implements PacketListener {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    private boolean packetError;

    @Override
    public void onPacketSend(PacketSendEvent event) {
        final PacketTypeCommon type = event.getPacketType();

        if (type != PacketType.Play.Server.SOUND_EFFECT) return;

        if (this.packetError) {
            this.plugin.getComponentLogger().error("Sound Effect Packet Event failed to send.");

            return;
        }

        try {
            final List<String> sounds = ConfigManager.getConfig().getProperty(Config.sound_names);

            if (sounds.isEmpty()) return;

            final WrapperPlayServerSoundEffect effect = new WrapperPlayServerSoundEffect(event);

            final Sound sound = effect.getSound();

            if (sound == null) return;

            final String name = effect.getSound().getSoundId().getKey();

            if (sounds.contains(name)) {
                event.setCancelled(true);
            }
        } catch (Exception exception) {
            this.packetError = true;
        }
    }
}