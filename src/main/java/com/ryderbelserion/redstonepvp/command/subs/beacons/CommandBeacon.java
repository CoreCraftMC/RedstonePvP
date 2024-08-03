package com.ryderbelserion.redstonepvp.command.subs.beacons;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.builders.gui.PaginatedGui;
import com.ryderbelserion.redstonepvp.api.enums.PersistentKeys;
import com.ryderbelserion.redstonepvp.api.interfaces.Gui;
import com.ryderbelserion.redstonepvp.api.interfaces.GuiItem;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.redstonepvp.managers.MenuManager;
import com.ryderbelserion.redstonepvp.managers.config.beans.ButtonProperty;
import com.ryderbelserion.redstonepvp.managers.config.beans.GuiProperty;
import com.ryderbelserion.redstonepvp.utils.ItemUtils;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import com.ryderbelserion.vital.paper.commands.Command;
import com.ryderbelserion.vital.paper.commands.CommandData;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.command.subs.beacons.item.CommandBeaconItem;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class CommandBeacon extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandData data) {
        if (!data.isPlayer()) {
            Messages.not_a_player.sendMessage(data.getCommandSender());

            return;
        }

        final Player target = data.getPlayer();

        final GuiProperty property = MenuManager.getGui("beacon-menu");
        final @NotNull PaginatedGui gui = Gui.paginated().disableItemDrop().disableItemPlacement().disableItemSwap().disableItemTake()
                .setTitle(property.getGuiTitle())
                .setRows(property.getGuiRows())
                .create();

        ItemUtils.addButtons(property, target, gui);

        final ButtonProperty button = property.getButtons().getFirst();

        BeaconManager.getBeaconData().forEach((uuid, beacon) -> {
            final Location location = MiscUtils.location(beacon.getRawLocation());

            final ItemBuilder itemBuilder = button.build(
                    new HashMap<>() {{
                        put("{world}", location.getWorld().getName());
                        put("{x}", String.valueOf(location.getX()));
                        put("{y}", String.valueOf(location.getY()));
                        put("{z}", String.valueOf(location.getZ()));
                        put("{name}", beacon.getName());
                    }})
                    .setAmount(beacon.getTime())
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

                        Messages.beacon_location_removed.sendMessage(player, "{name}", beaconName);

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

                        ItemUtils.addButtons(item_menu, player, item_gui);

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

        gui.open(data.getPlayer(), 1);
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.beacon.access";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("beacon")
                .requires(source -> source.getSender().hasPermission(getPermission()))
                .executes(context -> {
                    execute(new CommandData(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
                .then(new CommandBeaconSet().registerPermission().literal())
                .then(new CommandBeaconRemove().registerPermission().literal())

                // the item subcommand
                .then(new CommandBeaconItem().registerPermission().literal())
                .build();
    }

    @Override
    public @NotNull final Command registerPermission() {
        final Permission permission = this.plugin.getServer().getPluginManager().getPermission(getPermission());

        if (permission == null) {
            this.plugin.getServer().getPluginManager().addPermission(new Permission(getPermission(), PermissionDefault.OP));
        }

        return this;
    }
}