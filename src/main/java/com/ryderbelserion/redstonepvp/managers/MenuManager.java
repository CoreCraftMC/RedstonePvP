package com.ryderbelserion.redstonepvp.managers;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.builders.gui.PaginatedGui;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.api.enums.PersistentKeys;
import com.ryderbelserion.redstonepvp.api.interfaces.Gui;
import com.ryderbelserion.redstonepvp.api.interfaces.GuiItem;
import com.ryderbelserion.redstonepvp.managers.config.ConfigManager;
import com.ryderbelserion.redstonepvp.managers.config.beans.ButtonProperty;
import com.ryderbelserion.redstonepvp.managers.config.beans.GuiProperty;
import com.ryderbelserion.redstonepvp.managers.config.types.Config;
import com.ryderbelserion.redstonepvp.utils.ItemUtils;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder;
import com.ryderbelserion.vital.paper.files.config.FileManager;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("DuplicatedCode")
public class MenuManager {

    private static final RedstonePvP plugin = RedstonePvP.getPlugin();
    private static final Server server = plugin.getServer();

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

    public static void openMenu(final Player sender, final String menu) {
        final GuiProperty property = MenuManager.getGui(menu);

        if (menu.equalsIgnoreCase(ConfigManager.getConfig().getProperty(Config.main_menu_name))) {
            final Gui gui = Gui.gui().disableItemDrop().disableItemPlacement().disableItemSwap().disableItemTake()
                    .setTitle(property.getGuiTitle())
                    .setRows(property.getGuiRows())
                    .setType(property.getGuiType())
                    .create();

            final List<ButtonProperty> buttons = property.getButtons();

            buttons.forEach(button -> {
                final GuiItem item = gui.asGuiItem(button.build().getStack(), action -> {
                    if (!(action.getWhoClicked() instanceof Player clicker)) return;

                    button.getCommands().forEach(command -> server.dispatchCommand(server.getConsoleSender(), command.replaceAll("\\{player}", clicker.getName())));
                    button.getMessages().forEach(message -> MiscUtils.message(clicker, message));

                    button.getSoundProperty().playSound(clicker);
                });

                gui.setItem(button.getDisplayRow(), button.getDisplayColumn(), item);
            });

            gui.open(sender);

            return;
        }

        final PaginatedGui gui = Gui.paginated().disableItemDrop().disableItemPlacement().disableItemSwap().disableItemTake()
                .setTitle(property.getGuiTitle())
                .setRows(property.getGuiRows())
                .create();

        ItemUtils.addButtons(property, gui);

        final ButtonProperty button = property.getButtons().getFirst();

        switch (menu) {
            case "beacon-menu" -> BeaconManager.getBeaconData().forEach((uuid, beacon) -> {
                final Location location = MiscUtils.location(beacon.getRawLocation());

                final ItemBuilder itemBuilder = button.build(
                                new HashMap<>() {{
                                    put("{world}", location.getWorld().getName());
                                    put("{time}", beacon.getTime());
                                    put("{x}", String.valueOf(location.getX()));
                                    put("{y}", String.valueOf(location.getY()));
                                    put("{z}", String.valueOf(location.getZ()));
                                    put("{name}", beacon.getName());
                                }})
                        .setPersistentString(PersistentKeys.beacon_uuid.getNamespacedKey(), beacon.getName());

                gui.addPageItem(gui.asGuiItem(itemBuilder.getStack(), event -> {
                    if (!(event.getWhoClicked() instanceof Player player)) return;

                    switch (event.getClick()) {
                        case LEFT -> {
                            final GuiItem item = gui.getPageItem(event.getSlot());

                            if (item == null) return;

                            final ItemStack itemStack = item.getItemStack();

                            final PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();

                            final String beaconName = container.get(PersistentKeys.beacon_uuid.getNamespacedKey(), PersistentDataType.STRING);

                            BeaconManager.removeLocation(beaconName);

                            Messages.beacon_drop_party_removed.sendMessage(player, "{name}", beaconName);

                            button.getSoundProperty().playSound(player);

                            gui.removePageItem(item);
                        }

                        case RIGHT -> {
                            final GuiItem item = gui.getPageItem(event.getSlot());

                            if (item == null) return;

                            final ItemStack itemStack = item.getItemStack();

                            final PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();

                            final String beaconName = container.get(PersistentKeys.beacon_uuid.getNamespacedKey(), PersistentDataType.STRING);

                            final GuiProperty item_menu = MenuManager.getGui("item-menu");
                            final PaginatedGui item_gui = Gui.paginated().disableItemDrop().disableItemPlacement().disableItemSwap().disableItemTake()
                                    .setTitle(item_menu.getGuiTitle())
                                    .setRows(item_menu.getGuiRows())
                                    .create();

                            ItemUtils.addButtons(item_menu, item_gui);

                            final ButtonProperty itemButton = item_menu.getButtons().getFirst();

                            BeaconManager.getBeacon(beaconName).getDrop().getItems().forEach((key, itemDrop) -> {
                                // if null, don't add anything.
                                if (key == null) return;

                                final ItemBuilder drop = itemButton.build(key, false);

                                Map<String, String> placeholders = new HashMap<>() {{
                                    put("{weight}", String.valueOf(itemDrop.getWeight()));
                                    put("{min}", String.valueOf(itemDrop.getMin()));
                                    put("{max}", String.valueOf(itemDrop.getMax()));
                                    put("{name}", drop.getStrippedName());
                                }};

                                placeholders.forEach((placeholder, value) -> {
                                    drop.addNamePlaceholder(placeholder, value);
                                    drop.addLorePlaceholder(placeholder, value);
                                });

                                drop.setPersistentString(PersistentKeys.beacon_item.getNamespacedKey(), key);

                                item_gui.addPageItem(item_gui.asGuiItem(drop.getStack(), clickEvent -> {
                                    final GuiItem guiItem = item_gui.getPageItem(clickEvent.getSlot());

                                    if (guiItem == null) return;

                                    final PersistentDataContainer pdc = guiItem.getItemStack().getItemMeta().getPersistentDataContainer();

                                    final String itemName = pdc.get(PersistentKeys.beacon_item.getNamespacedKey(), PersistentDataType.STRING);

                                    BeaconManager.getBeacon(beaconName).getDrop().removeItem(itemName);

                                    itemButton.getSoundProperty().playSound(player);

                                    item_gui.removePageItem(guiItem);
                                }));
                            });

                            item_gui.open(player, 1);
                        }
                    }
                }));
            });

            case "item-menu" -> {

            }
        }

        gui.open(sender, 1);
    }
}