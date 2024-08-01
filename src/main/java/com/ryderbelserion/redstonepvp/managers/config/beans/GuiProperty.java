package com.ryderbelserion.redstonepvp.managers.config.beans;

import com.ryderbelserion.redstonepvp.api.interfaces.GuiType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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