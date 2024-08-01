package com.ryderbelserion.redstonepvp.managers.config.beans;

import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ButtonProperty {

    private final ConfigurationSection section;

    /**
     * Constructs the button
     *
     * @param section {@link ConfigurationSection} for the button
     */
    public ButtonProperty(final ConfigurationSection section) {
        this.section = section;
    }

    /**
     * Gets the material from the config file!
     *
     * @return the material to build {@link org.bukkit.Material}
     */
    public final String getDisplayMaterial() {
        return this.section.getString("preview.display_material", "compass");
    }

    /**
     * Gets the display name from the config file!
     *
     * @return {@link String} display name
     */
    public final String getDisplayName() {
        return this.section.getString("preview.display_name", "");
    }

    /**
     * Gets the display lore from the config file!
     *
     * @return {@link List<String>} list of strings
     */
    public final List<String> getDisplayLore() {
        return this.section.getStringList("preview.display_lore");
    }

    /**
     * Gets the slot of the button for the gui.
     *
     * @return the slot number
     */
    public final int getDisplaySlot() {
        return this.section.getInt("preview.display_slot");
    }

    /**
     * @return list of commands to send
     */
    public final List<String> getCommands() {
        return this.section.getStringList("commands");
    }

    /**
     * @return list of messages to send
     */
    public final List<String> getMessages() {
        return this.section.getStringList("messages");
    }

    /*
     * @return the nbt tag
    public final String getNbtTag() {
        return this.section.getString("nbt-tag", "");
    }*/

    /**
     * @return {@link SoundProperty}
     */
    public final SoundProperty getSoundProperty() {
        return new SoundProperty(this.section.getConfigurationSection("sound"));
    }

    /**
     * Builds an {@link ItemBuilder} with optionally placeholders
     *
     * @param placeholders a map of placeholders
     * @return {@link ItemBuilder}
     */
    public final ItemBuilder build(@Nullable Map<String, String> placeholders) {
        final ItemBuilder itemBuilder = new ItemBuilder();

        if (placeholders != null) {
            placeholders.forEach((key, pair) -> {
                itemBuilder.addNamePlaceholder(key, pair);
                itemBuilder.addLorePlaceholder(key, pair);
            });
        }

        return itemBuilder.withType(getDisplayMaterial()).setDisplayName(getDisplayName()).setDisplayLore(getDisplayLore());
    }

    /**
     * @return {@link ItemBuilder}
     */
    public final ItemBuilder build() {
        return build(null);
    }
}