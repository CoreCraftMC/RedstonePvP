package com.ryderbelserion.redstonepvp.managers.config.beans;

import com.ryderbelserion.redstonepvp.api.keys.ItemKeys;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import java.util.List;

public class ButtonProperty {

    private final ConfigurationSection section;

    /**
     * Constructs the button
     *
     * @param section {@link ConfigurationSection} for the button
     */
    public ButtonProperty(ConfigurationSection section) {
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

    /**
     * @return the custom tag to apply to the item
     */
    public final String getCustomTag() {
        return this.section.getString("custom_tag", "");
    }

    /**
     * @return {@link SoundProperty}
     */
    public final SoundProperty getSoundProperty() {
        return new SoundProperty(this.section.getConfigurationSection("sound"));
    }

    /**
     * @return {@link ItemBuilder}
     */
    public ItemBuilder build() {
        final ItemBuilder item = new ItemBuilder().withType(getDisplayMaterial()).setDisplayName(getDisplayName()).setDisplayLore(getDisplayLore());

        final String tag = getCustomTag();

        if (!tag.isEmpty()) {
            final String[] split = tag.split(";");

            final NamespacedKey key = ItemKeys.build(split[1]);

            switch (split[0]) {
                case "string" -> item.setPersistentString(key, "1");
                case "integer" -> item.setPersistentInteger(key, 1);
                case "boolean" -> item.setPersistentBoolean(key, true);
                case "double" -> item.setPersistentDouble(key, 1.0);
            }
        }

        return item;
    }
}