package com.ryderbelserion.redstonepvp.managers;

import com.ryderbelserion.redstonepvp.managers.config.ConfigManager;
import com.ryderbelserion.redstonepvp.managers.config.beans.GuiProperty;
import com.ryderbelserion.vital.paper.files.config.FileManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MenuManager {

    private final static FileManager fileManager = ConfigManager.getFileManager();

    public static Map<String, GuiProperty> guis = new HashMap<>();

    public static void populate() {
        guis.clear();

        fileManager.getCustomFiles().forEach(file -> guis.put(file.getStrippedName(), new GuiProperty(file.getConfiguration())));
    }

    public static GuiProperty getGui(final String name) {
        return guis.get(name);
    }

    public static Map<String, GuiProperty> getGuis() {
        return Collections.unmodifiableMap(guis);
    }
}