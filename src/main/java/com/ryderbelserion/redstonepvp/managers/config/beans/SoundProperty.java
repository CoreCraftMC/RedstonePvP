package com.ryderbelserion.redstonepvp.managers.config.beans;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class SoundProperty {

    private final boolean isSoundEnabled;
    private final float soundVolume;
    private final float soundPitch;
    private final String soundName;

    private final String soundSource;

    /**
     * Builds a sound property
     *
     * @param section the configuration section
     */
    public SoundProperty(final ConfigurationSection section) {
        this.isSoundEnabled = section.getBoolean("toggle", false);
        this.soundVolume = (float) section.getDouble("volume", 1.0);
        this.soundPitch = (float) section.getDouble("pitch", 1.0);
        this.soundName = section.getString("sound", "");
        this.soundSource = section.getString("source", "player").toUpperCase();
    }

    /**
     * @return true or false
     */
    public final boolean isSoundEnabled() {
        return this.isSoundEnabled;
    }

    /**
     * @return the name of the sound
     */
    public final String getSoundName() {
        return this.soundName;
    }

    /**
     * @return the volume of the sound
     */
    public final float getSoundVolume() {
        return this.soundVolume;
    }

    /**
     * @return the direction the sound will play
     */
    public final float getSoundPitch() {
        return this.soundPitch;
    }

    /**
     * Fetches the sound source from the config file!
     *
     * @return {@link Sound.Source}
     */
    public final Sound.Source getSoundSource() {
        return Sound.Source.valueOf(this.soundSource);
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