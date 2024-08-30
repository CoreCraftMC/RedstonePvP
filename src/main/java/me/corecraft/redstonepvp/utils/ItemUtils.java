package me.corecraft.redstonepvp.utils;

import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.api.objects.ItemBuilder;
import me.corecraft.redstonepvp.managers.config.beans.ButtonProperty;
import me.corecraft.redstonepvp.managers.config.beans.GuiProperty;
import com.ryderbelserion.vital.paper.api.builders.gui.types.PaginatedGui;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class ItemUtils {

    private static final RedstonePvP plugin = RedstonePvP.getPlugin();

    private static final Server server = plugin.getServer();

    public static void addButtons(final GuiProperty property, final PaginatedGui gui) {
        final ButtonProperty backButton = property.getBackButton();
        final ButtonProperty nextButton = property.getNextButton();
        final ButtonProperty menuButton = property.getHomeButton();

        if (backButton != null) {
            gui.setItem(backButton.getDisplayRow(), backButton.getDisplayColumn(), gui.asGuiItem(new ItemBuilder().withType(backButton.getDisplayMaterial())
                    .setDisplayName(backButton.getDisplayName())
                    .setDisplayLore(backButton.getDisplayLore())
                    .getStack(), action -> {
                if (!(action.getWhoClicked() instanceof Player clicker)) return;

                backButton.getSoundProperty().playSound(clicker);

                backButton.getCommands().forEach(command -> server.dispatchCommand(server.getConsoleSender(), command.replaceAll("\\{player}", clicker.getName())));
                backButton.getMessages().forEach(message -> MiscUtils.message(clicker, message));

                gui.previous();
            }));
        }

        if (nextButton != null) {
            gui.setItem(nextButton.getDisplayRow(), nextButton.getDisplayColumn(), gui.asGuiItem(new ItemBuilder().withType(nextButton.getDisplayMaterial())
                    .setDisplayName(nextButton.getDisplayName())
                    .setDisplayLore(nextButton.getDisplayLore())
                    .getStack(), action -> {
                if (!(action.getWhoClicked() instanceof Player clicker)) return;

                nextButton.getSoundProperty().playSound(clicker);

                nextButton.getCommands().forEach(command -> server.dispatchCommand(server.getConsoleSender(), command.replaceAll("\\{player}", clicker.getName())));
                nextButton.getMessages().forEach(message -> MiscUtils.message(clicker, message));

                gui.next();
            }));
        }

        if (menuButton != null) {
            gui.setItem(menuButton.getDisplayRow(), menuButton.getDisplayColumn(), gui.asGuiItem(new ItemBuilder().withType(menuButton.getDisplayMaterial())
                    .setDisplayName(menuButton.getDisplayName())
                    .setDisplayLore(menuButton.getDisplayLore())
                    .getStack(), action -> {
                if (!(action.getWhoClicked() instanceof Player clicker)) return;

                menuButton.getSoundProperty().playSound(clicker);

                menuButton.getCommands().forEach(command -> server.dispatchCommand(server.getConsoleSender(), command.replaceAll("\\{player}", clicker.getName())));
                menuButton.getMessages().forEach(message -> MiscUtils.message(clicker, message));
            }));
        }
    }
}