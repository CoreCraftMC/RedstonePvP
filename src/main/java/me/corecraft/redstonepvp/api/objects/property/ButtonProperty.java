package me.corecraft.redstonepvp.api.objects.property;

import me.corecraft.redstonepvp.api.objects.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;

public class ButtonProperty {

    private final ConfigurationSection section;

    private final SoundProperty soundProperty;

    private final String displayMaterial;
    private final String displayName;
    private final List<String> displayLore;
    private final int displayRow;
    private final int displayColumn;

    private final List<String> commands;
    private final List<String> messages;

    /**
     * Constructs the button
     *
     * @param section {@link ConfigurationSection} for the button
     */
    public ButtonProperty(final ConfigurationSection section) {
        this.section = section;

        final ConfigurationSection sound = this.section.getConfigurationSection("sound");

        this.soundProperty = sound != null ? new SoundProperty(sound) : null;

        this.displayMaterial = this.section.getString("preview.display_material", "compass");
        this.displayName = this.section.getString("preview.display_name", "");
        this.displayLore = this.section.getStringList("preview.display_lore");
        this.displayRow = this.section.getInt("preview.display_position.row", -1);
        this.displayColumn = this.section.getInt("preview.display_position.column", -1);

        this.commands = this.section.getStringList("commands");
        this.messages = this.section.getStringList("messages");
    }

    /**
     * Gets the material from the config file!
     *
     * @return the material to build {@link org.bukkit.Material}
     */
    public final String getDisplayMaterial() {
        return this.displayMaterial;
    }

    /**
     * Gets the display name from the config file!
     *
     * @return {@link String} display name
     */
    public final String getDisplayName() {
        return this.displayName;
    }

    /**
     * Gets the display lore from the config file!
     *
     * @return {@link List<String>} list of strings
     */
    public final List<String> getDisplayLore() {
        return this.displayLore;
    }

    /**
     * Gets the row of the button for the gui.
     *
     * @return the row number
     */
    public final int getDisplayRow() {
        return this.displayRow;
    }

    /**
     * Gets the column to place the button in.
     *
     * @return the column number
     */
    public final int getDisplayColumn() {
        return this.displayColumn;
    }

    /**
     * @return list of commands to send
     */
    public final List<String> getCommands() {
        return this.commands;
    }

    /**
     * @return list of messages to send
     */
    public final List<String> getMessages() {
        return this.messages;
    }

    /**
     * @return {@link SoundProperty}
     */
    public final SoundProperty getSoundProperty() {
        return this.soundProperty;
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

        if (hasMaterial && !this.section.contains("preview.display_material")) {
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