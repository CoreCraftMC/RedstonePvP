package com.ryderbelserion.redstonepvp.api.core.builders.types;

import com.ryderbelserion.redstonepvp.api.core.builders.InventoryBuilder;
import com.ryderbelserion.redstonepvp.api.objects.BeaconDrop;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BeaconMenu extends InventoryBuilder {

    public BeaconMenu(@NotNull final Player player) {
        super(player, "<red>Beacon Menu</red>", 27);
    }

    public BeaconMenu() {}

    @Override
    public InventoryBuilder build() {
        final Inventory inventory = getInventory();

        final Map<UUID, BeaconDrop> beaconData = BeaconManager.getBeaconData();

        final ItemBuilder itemBuilder = new ItemBuilder().withType(Material.CHEST);

        beaconData.forEach((uuid, beaconDrop) -> {
            // Set the amount to how much time it takes to start a drop event.
            itemBuilder.setAmount(beaconDrop.getTime());

            //final Location location = beaconDrop.getLocation();

            //itemBuilder.setDisplayLore(List.of(
            //        "World: " + location.getWorld().getName(),
            //        "X: " + location.x(),
            //        "Y: " + location.y(),
            //        "Z: " + location.z()
            //));

            itemBuilder.setDisplayName(beaconDrop.getUUID().toString());

            inventory.setItem(inventory.firstEmpty(), itemBuilder.getStack());
        });

        return this;
    }

    @Override
    public void run(InventoryClickEvent event) {
        final ItemStack itemStack = event.getCurrentItem();

        if (itemStack == null || !itemStack.hasItemMeta()) return;

        final ItemMeta itemMeta = itemStack.getItemMeta();

        event.setCancelled(true);
    }
}