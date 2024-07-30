package com.ryderbelserion.redstonepvp.api.gui;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.Gui;
import com.ryderbelserion.redstonepvp.managers.ConfigManager;
import com.ryderbelserion.redstonepvp.managers.config.Config;
import com.ryderbelserion.redstonepvp.managers.config.beans.GuiProperty;
import org.bukkit.entity.Player;

public class MainMenuGui {

    private static final SettingsManager config = ConfigManager.getConfig();

    public static void build(Player player) {
        player.openInventory(getMainMenuGui().getInventory());
    }

    public static Gui getMainMenuGui() {
        GuiProperty property = config.getProperty(Config.main_menu);

        return Gui.gui()
                .setTitle(property.getTitle())
                .setRows(property.getRows())
                .apply(key -> {
                    //property.getProperties().forEach(pair -> {
                        //final ItemStack itemStack = pair.build("").getStack();

                        //key.setItem(pair.getSlot(), key.asGuiItem(itemStack));

                    //});
                })
                .create();
    }
}