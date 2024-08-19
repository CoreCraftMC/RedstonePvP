package com.ryderbelserion.redstonepvp.managers.config.beans;

import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiType;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.ArrayList;
import java.util.List;

public class GuiProperty {

    private final CommentedConfigurationNode configuration;

    public GuiProperty(final CommentedConfigurationNode configuration) {
        this.configuration = configuration;
    }

    public final GuiType getGuiType() {
        return GuiType.valueOf(this.configuration.node("menu", "type").getString("chest").toUpperCase());
    }

    public final String getGuiTitle() {
        return this.configuration.node("menu", "title").getString("<red>Basic Title</red>");
    }

    public final int getGuiRows() {
        this.configuration.virtual();

        return this.configuration.node("menu", "rows").getInt(6);
    }

    public @Nullable final ButtonProperty getNextButton() {
        final CommentedConfigurationNode node = this.configuration.node("menu", "next_button");

        if (!node.virtual()) return null;

        return new ButtonProperty(node);
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