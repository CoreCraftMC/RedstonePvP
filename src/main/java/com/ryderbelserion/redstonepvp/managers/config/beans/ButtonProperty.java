package com.ryderbelserion.redstonepvp.managers.config.beans;

import com.ryderbelserion.redstonepvp.api.objects.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.List;
import java.util.Map;

public class ButtonProperty {

    private final CommentedConfigurationNode section;

    /**
     * Constructs the button
     *
     * @param section {@link ConfigurationSection} for the button
     */
    public ButtonProperty(final CommentedConfigurationNode section) {
        this.section = section;
    }

    /**
     * Gets the material from the config file!
     *
     * @return the material to build {@link org.bukkit.Material}
     */
    public final String getDisplayMaterial() {
        return this.section.node("preview", "display_material").getString("compass");
    }

    /**
     * Gets the display name from the config file!
     *
     * @return {@link String} display name
     */
    public final String getDisplayName() {
        return this.section.node("preview", "display_name").getString("");
    }

    /**
     * Gets the display lore from the config file!
     *
     * @return {@link List<String>} list of strings
     */
    public final List<String> getDisplayLore() {
        return this.section.node("preview", "display_lore").getList();
    }

    /**
     * Gets the row of the button for the gui.
     *
     * @return the row number
     */
    public final int getDisplayRow() {
        return this.section.node("preview", "display_position", "row").getInt(-1);
    }

    /**
     * Gets the column to place the button in.
     *
     * @return the column number
     */
    public final int getDisplayColumn() {
        return this.section.node("preview", "display_position", "column").getInt(-1);
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
     * @param hasMaterial true or false
     * @param base64 the item string
     * @return {@link ItemBuilder}
     */
    public final ItemBuilder build(@Nullable final Map<String, String> placeholders, final boolean hasMaterial, @NotNull final String base64) {
        ItemBuilder itemBuilder = new ItemBuilder();

        if (hasMaterial && !this.section.node("preview", "display_material").virtual()) {
            itemBuilder.withType(getDisplayMaterial());
        } else {
            itemBuilder = itemBuilder.fromBase64(base64);
        }

        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                itemBuilder.addNamePlaceholder(entry.getKey(), entry.getValue());
                itemBuilder.addLorePlaceholder(entry.getKey(), entry.getValue());
            }
        }

        return itemBuilder.setDisplayName(getDisplayName()).setDisplayLore(getDisplayLore());
    }

    /**
     * Builds an {@link ItemBuilder} with no placeholders.
     *
     * @param hasMaterial true or false
     * @param base64 the item string
     * @return {@link ItemBuilder}
     */
    public final ItemBuilder build(final String base64, final boolean hasMaterial) {
        return build(null, hasMaterial, base64);
    }

    /**
     * Builds an {@link ItemBuilder} with optionally placeholders
     *
     * @param placeholders a map of placeholders
     * @return {@link ItemBuilder}
     */
    public final ItemBuilder build(@Nullable Map<String, String> placeholders) {
        return build(placeholders, true, "");
    }

    /**
     * @return {@link ItemBuilder}
     */
    public final ItemBuilder build() {
        return build(null);
    }
}