package com.ryderbelserion.redstonepvp.managers.config.beans;

import com.ryderbelserion.redstonepvp.api.interfaces.GuiType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class GuiProperty extends YamlConfiguration {

    private final File file;

    public GuiProperty(final File file) {
        this.file = file;
    }

    public void load() throws IOException, InvalidConfigurationException {
        load(this.file);
    }

    public final GuiType getGuiType() {
        return GuiType.valueOf(getString("menu.type", "chest").toUpperCase());
    }

    public final String getGuiTitle() {
        return getString("menu.title", "<red>Basic Title</red>");
    }
}