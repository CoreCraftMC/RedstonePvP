package com.ryderbelserion.redstonepvp.managers.config.beans;

import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiType;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlFile;
import java.util.ArrayList;
import java.util.List;

public class GuiProperty {

    private final YamlFile configuration;

    private final GuiType guiType;
    private final String guiTitle;
    private final int guiRows;

    public GuiProperty(final YamlFile configuration) {
        this.configuration = configuration;

        this.guiType = GuiType.valueOf(this.configuration.getString("menu.type", "chest").toUpperCase());
        this.guiTitle = this.configuration.getString("menu.title", "<red>Basic Title</red>");
        this.guiRows = this.configuration.getInt("menu.rows", 6);
    }

    public final GuiType getGuiType() {
        return this.guiType;
    }

    public final String getGuiTitle() {
        return this.guiTitle;
    }

    public final int getGuiRows() {
        return this.guiRows;
    }

    public @Nullable final ButtonProperty getNextButton() {
        final ConfigurationSection section = this.configuration.getConfigurationSection("menu.next_button");

        if (section == null) return null;

        return new ButtonProperty(section);
    }

    public @Nullable final ButtonProperty getBackButton() {
        final ConfigurationSection section = this.configuration.getConfigurationSection("menu.back_button");

        if (section == null) return null;

        return new ButtonProperty(section);
    }

    public @Nullable final ButtonProperty getMenuButton() {
        final ConfigurationSection section = this.configuration.getConfigurationSection("menu.home_button");

        if (section == null) return null;

        return new ButtonProperty(section);
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