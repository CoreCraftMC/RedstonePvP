package com.ryderbelserion.redstonepvp.utils;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.builders.gui.PaginatedGui;
import com.ryderbelserion.redstonepvp.managers.config.beans.ButtonProperty;
import com.ryderbelserion.redstonepvp.managers.config.beans.GuiProperty;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class ItemUtils {

    private static final RedstonePvP plugin = RedstonePvP.getPlugin();

    private static final Server server = plugin.getServer();

    public static void addButtons(final GuiProperty property, final Player target, final PaginatedGui gui) {
        final ButtonProperty backButton = property.getBackButton();
        final ButtonProperty nextButton = property.getNextButton();
        final ButtonProperty menuButton = property.getMenuButton();

        if (backButton != null) {
            gui.setItem(backButton.getDisplayRow(), backButton.getDisplayColumn(), gui.asGuiItem(new ItemBuilder().withType(backButton.getDisplayMaterial())
                    .setDisplayName(backButton.getDisplayName())
                    .setDisplayLore(backButton.getDisplayLore())
                    .getStack(), back -> {
                backButton.getSoundProperty().playSound(target);

                backButton.getCommands().forEach(command -> server.dispatchCommand(server.getConsoleSender(), command.replaceAll("\\{player}", target.getName())));
                backButton.getMessages().forEach(message -> MiscUtils.message(target, message));

                gui.previous();
            }));
        }

        if (nextButton != null) {
            gui.setItem(nextButton.getDisplayRow(), nextButton.getDisplayColumn(), gui.asGuiItem(new ItemBuilder().withType(nextButton.getDisplayMaterial())
                    .setDisplayName(nextButton.getDisplayName())
                    .setDisplayLore(nextButton.getDisplayLore())
                    .getStack(), next -> {
                nextButton.getSoundProperty().playSound(target);

                nextButton.getCommands().forEach(command -> server.dispatchCommand(server.getConsoleSender(), command.replaceAll("\\{player}", target.getName())));
                nextButton.getMessages().forEach(message -> MiscUtils.message(target, message));

                gui.next();
            }));
        }

        if (menuButton != null) {
            gui.setItem(menuButton.getDisplayRow(), menuButton.getDisplayColumn(), gui.asGuiItem(new ItemBuilder().withType(menuButton.getDisplayMaterial())
                    .setDisplayName(menuButton.getDisplayName())
                    .setDisplayLore(menuButton.getDisplayLore())
                    .getStack(), next -> {
                menuButton.getSoundProperty().playSound(target);

                menuButton.getCommands().forEach(command -> server.dispatchCommand(server.getConsoleSender(), command.replaceAll("\\{player}", target.getName())));
                menuButton.getMessages().forEach(message -> MiscUtils.message(target, message));
            }));
        }
    }
}