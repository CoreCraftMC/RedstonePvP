package com.ryderbelserion.redstonepvp.managers.config.beans;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.simpleyaml.configuration.ConfigurationSection;

public class SoundProperty {

    private final ConfigurationSection section;

    /**
     * Builds a sound property
     *
     * @param section the configuration section
     */
    public SoundProperty(final ConfigurationSection section) {
        this.section = section;
    }

    /**
     * @return true or false
     */
    public final boolean isSoundEnabled() {
        return this.section.getBoolean("toggle", false);
    }

    /**
     * @return the name of the sound
     */
    public final String getSoundName() {
        return this.section.getString("sound", "");
    }

    /**
     * @return the volume of the sound
     */
    public final float getSoundVolume() {
        return (float) this.section.getDouble("volume", 1.0);
    }

    /**
     * @return the direction the sound will play
     */
    public final float getSoundPitch() {
        return (float) this.section.getDouble("pitch", 1.0);
    }

    /**
     * Fetches the sound source from the config file!
     *
     * @return {@link Sound.Source}
     */
    public final Sound.Source getSoundSource() {
        return Sound.Source.valueOf(this.section.getString("source", "player").toUpperCase());
    }

    /**
     * Plays a sound to a player at their location.
     *
     * @param player {@link Player}
     */
    public void playSound(final Player player) {
        playSound(player, player.getLocation());
    }

    /**
     * Plays a sound to a player at a specific location.
     *
     * @param player {@link Player}
     * @param location {@link Location}
     */
    public void playSound(final Player player, final Location location) {
        if (!isSoundEnabled() || getSoundName().isEmpty()) return;

        final Sound sound = Sound.sound(Key.key(getSoundName()), getSoundSource(), getSoundVolume(), getSoundPitch());

        player.playSound(sound, location.x(), location.y(), location.z());
    }
}