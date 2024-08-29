package com.ryderbelserion.redstonepvp.managers.config.beans;

import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GuiProperty {

    private final GuiType guiType;
    private final String guiTitle;
    private final int guiRows;

    private final ButtonProperty nextButton;
    private final ButtonProperty backButton;
    private final ButtonProperty homeButton;

    private final List<ButtonProperty> buttons = new ArrayList<>();

    public GuiProperty(final YamlConfiguration configuration) {
        this.guiType = GuiType.valueOf(configuration.getString("menu.type", "chest").toUpperCase());
        this.guiTitle = configuration.getString("menu.title", "<red>Basic Title</red>");
        this.guiRows = configuration.getInt("menu.rows", 6);

        this.nextButton = new ButtonProperty(configuration.getConfigurationSection("menu.next_button"));
        this.backButton = new ButtonProperty(configuration.getConfigurationSection("menu.back_button"));
        this.homeButton = new ButtonProperty(configuration.getConfigurationSection("menu.home_button"));

        final ConfigurationSection section = configuration.getConfigurationSection("menu.buttons");

        for (final String key : section.getKeys(false)) {
            this.buttons.add(new ButtonProperty(section.getConfigurationSection(key)));
        }
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
        return this.nextButton;
    }

    public @Nullable final ButtonProperty getBackButton() {
        return this.backButton;
    }

    public @Nullable final ButtonProperty getHomeButton() {
        return this.homeButton;
    }

    public final List<ButtonProperty> getButtons() {
        return this.buttons;
    }
}