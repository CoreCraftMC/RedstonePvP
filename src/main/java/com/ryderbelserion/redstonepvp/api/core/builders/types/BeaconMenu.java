package com.ryderbelserion.redstonepvp.api.core.builders.types;

import com.ryderbelserion.redstonepvp.api.core.builders.InventoryBuilder;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.api.enums.PersistentKeys;
import com.ryderbelserion.redstonepvp.api.objects.beacons.Beacon;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class BeaconMenu extends InventoryBuilder {

    public BeaconMenu(@NotNull final Player player) {
        super(player, "<red>Beacon Menu</red>", 27);
    }

    public BeaconMenu() {}

    @Override
    public InventoryBuilder build() {
        final Inventory inventory = getInventory();

        final Map<String, Beacon> beaconData = BeaconManager.getBeaconData();

        final ItemBuilder itemBuilder = new ItemBuilder().withType(Material.CHEST);

        beaconData.forEach((uuid, beacon) -> {
            // Set the amount to how much time it takes to start a drop event.
            itemBuilder.setAmount(beacon.getTime());

            final Location location = MiscUtils.location(beacon.getRawLocation());

            itemBuilder.setDisplayLore(List.of(
                    "World: " + location.getWorld().getName(),
                    "X: " + location.x(),
                    "Y: " + location.y(),
                    "Z: " + location.z()
            ));

            itemBuilder.setDisplayName(beacon.getName());

            itemBuilder.setPersistentString(PersistentKeys.beacon_location.getNamespacedKey(), beacon.getRawLocation());
            itemBuilder.setPersistentString(PersistentKeys.beacon_uuid.getNamespacedKey(), beacon.getName());

            inventory.setItem(inventory.firstEmpty(), itemBuilder.getStack());
        });

        return this;
    }

    @Override
    public void run(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        final Inventory inventory = event.getClickedInventory();

        if (inventory == null) return;

        if (!(inventory.getHolder(false) instanceof BeaconMenu)) return;

        final ItemStack itemStack = event.getCurrentItem();

        if (itemStack == null || !itemStack.hasItemMeta()) return;

        final ItemMeta itemMeta = itemStack.getItemMeta();

        final PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (container.has(PersistentKeys.beacon_location.getNamespacedKey())) {
            final String location = container.get(PersistentKeys.beacon_location.getNamespacedKey(), PersistentDataType.STRING);

            switch (event.getClick()) {
                case LEFT -> {
                    if (location != null) {
                        BeaconManager.removeLocation(location);

                        Messages.beacon_location_removed.sendMessage(player, "{name}", container.get(PersistentKeys.beacon_uuid.getNamespacedKey(), PersistentDataType.STRING));

                        player.openInventory(new BeaconMenu(player).build().getInventory());
                    }
                }

                case RIGHT -> {
                    player.openInventory(new ItemMenu(player, location).build().getInventory());
                }
            }
        }

        event.setCancelled(true);
    }
}