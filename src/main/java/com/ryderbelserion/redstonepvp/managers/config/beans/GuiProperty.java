package com.ryderbelserion.redstonepvp.managers.config.beans;

import com.ryderbelserion.vital.paper.api.interfaces.GuiType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GuiProperty {

    private final FileConfiguration configuration;

    public GuiProperty(final FileConfiguration configuration) {
        this.configuration = configuration;
    }

    public final GuiType getGuiType() {
        return GuiType.valueOf(this.configuration.getString("menu.type", "chest").toUpperCase());
    }

    public final String getGuiTitle() {
        return this.configuration.getString("menu.title", "<red>Basic Title</red>");
    }

    public final int getGuiRows() {
        return this.configuration.getInt("menu.rows", 6);
    }

    public @Nullable final ButtonProperty getNextButton() {
        final ConfigurationSection section = this.configuration.getConfigurationSection("menu.next_button");

        if (section == null) return null;

        return new ButtonProperty(this.configuration.getConfigurationSection("menu.next_button"));
    }

    public @Nullable final ButtonProperty getBackButton() {
        final ConfigurationSection section = this.configuration.getConfigurationSection("menu.back_button");

        if (section == null) return null;

        return new ButtonProperty(this.configuration.getConfigurationSection("menu.back_button"));
    }

    public @Nullable final ButtonProperty getMenuButton() {
        final ConfigurationSection section = this.configuration.getConfigurationSection("menu.home_button");

        if (section == null) return null;

        return new ButtonProperty(this.configuration.getConfigurationSection("menu.home_button"));
    }

    public final List<ButtonProperty> getButtons() {
        final List<ButtonProperty> buttons = new ArrayList<>();

        final ConfigurationSection section = this.configuration.getConfigurationSection("menu.buttons");

        if (section == null) return buttons;

        for (final String key : section.getKeys(false)) {
            buttons.add(new ButtonProperty(this.configuration.getConfigurationSection("menu.buttons." + key)));
        }

        return buttons;
    }
}