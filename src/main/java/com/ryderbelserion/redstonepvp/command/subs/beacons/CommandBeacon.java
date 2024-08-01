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
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import com.ryderbelserion.vital.paper.commands.Command;
import com.ryderbelserion.vital.paper.commands.CommandData;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.api.objects.ItemDrop;
import com.ryderbelserion.redstonepvp.command.subs.beacons.item.CommandBeaconItem;
import com.ryderbelserion.vital.paper.util.scheduler.FoliaRunnable;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CommandBeacon extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();
    private final Server server = this.plugin.getServer();

    @Override
    public void execute(final CommandData data) {
        if (!data.isPlayer()) {
            Messages.not_a_player.sendMessage(data.getCommandSender());

            return;
        }

        final GuiProperty property = MenuManager.getGui("beacon-menu");
        final @NotNull PaginatedGui gui = Gui.paginated().disableItemDrop().disableItemPlacement().disableItemSwap().disableItemTake()
                .setTitle(property.getGuiTitle())
                .setRows(property.getGuiRows())
                .create();

        gui.setItem(6, 3, gui.asGuiItem(new ItemStack(Material.ARROW), event -> gui.previous()));
        gui.setItem(6, 7, gui.asGuiItem(new ItemStack(Material.ARROW), event -> gui.next()));

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
                        final GuiItem guiItem = gui.getGuiItem(event.getSlot());

                        if (guiItem == null) return;

                        final ItemStack itemStack = guiItem.getItemStack();

                        final PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();

                        final String beaconName = container.get(PersistentKeys.beacon_uuid.getNamespacedKey(), PersistentDataType.STRING);

                        BeaconManager.removeLocation(beaconName);

                        Messages.beacon_location_removed.sendMessage(player, "{name}", beaconName);

                        button.getSoundProperty().playSound(player);

                        gui.removePageItem(itemStack);
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

    private void playDropAnimation(final Location location) {
        final List<ItemDrop> drops = new ArrayList<>() {{
            add(new ItemDrop(Material.GOLD_INGOT, 3, 12, 75.0));
            add(new ItemDrop(Material.NETHERITE_SCRAP, 1, 3, 0.5));
            add(new ItemDrop(Material.EMERALD, 1, 3, 1.0));
            add(new ItemDrop(Material.DIAMOND, 1, 3, 3.0));
        }};

        final Block topBlock = location.clone().add(0.0, 2, 0.0).getBlock();

        final Block waterBlock = topBlock.getLocation().clone().add(0.0, 1, 0.0).getBlock();

        final BlockData blockData = topBlock.getBlockData();

        new FoliaRunnable(this.plugin.getServer().getRegionScheduler(), location) {
            int counter = 0;

            @Override
            public void run() {
                // Event is cancelled.
                if (isCancelled()) {
                    return;
                }

                // Run start sound.
                if (this.counter == 0) {
                    playSound(location, Sound.ENTITY_GENERIC_EXPLODE);
                }

                // start phase 1
                if (this.counter <= 10) {
                    playSound(location, Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);

                    getDrop(location, drops);
                }

                // spawn water on top of the slab to push items out when it reaches 15.
                if (this.counter == 11) {
                    playSound(location, Sound.ENTITY_GENERIC_EXPLODE);

                    waterBlock.setType(Material.WATER, true);
                }

                // remove the water when it reaches 18.
                if (this.counter == 15) {
                    waterBlock.setType(Material.AIR, true);

                    if (blockData instanceof Waterlogged waterlogged) {
                        waterlogged.setWaterlogged(false);
                    }
                }

                // start phase 2 at 30
                if (this.counter >= 30 && this.counter <= 40) {
                    playSound(location, Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);

                    getDrop(location, drops);
                }

                // spawn water on top of the slab to push items out when it reaches 15.
                if (this.counter == 41) {
                    playSound(location, Sound.ENTITY_GENERIC_EXPLODE);

                    waterBlock.setType(Material.WATER, true);
                }

                // remove the water when it reaches 33.
                if (this.counter == 45) {
                    waterBlock.setType(Material.AIR, true);

                    if (blockData instanceof Waterlogged waterlogged) {
                        waterlogged.setWaterlogged(false);
                    }
                }

                // start phase 3
                if (this.counter >= 55 && this.counter <= 65) {
                    playSound(location, Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);

                    getDrop(location, drops);
                }

                // spawn water on top of the slab to push items out when it reaches 45.
                if (this.counter == 66) {
                    playSound(location, Sound.ENTITY_GENERIC_EXPLODE);

                    waterBlock.setType(Material.WATER, true);
                }

                // cancel task.
                if (this.counter >= 69) {
                    waterBlock.setType(Material.AIR, true);

                    if (blockData instanceof Waterlogged waterlogged) {
                        waterlogged.setWaterlogged(false);
                    }

                    cancel();

                    return;
                }

                this.counter++;
            }
        }.runAtFixedRate(this.plugin, 0, 10);
    }

    private void playSound(final Location location, final Sound sound) {
        location.getNearbyPlayers(7.5).forEach(player -> player.playSound(location, sound, 1f, 1f));
    }

    private void getDrop(final Location location, final List<ItemDrop> drops) {
        double weight = 0.0;

        for (ItemDrop itemDrop : drops) {
            weight += itemDrop.getWeight();
        }

        int index = 0;

        for (double random = ThreadLocalRandom.current().nextDouble() * weight; index < drops.size() - 1; index++) {
            random -= drops.get(index).getWeight();

            if (random < 0.0) break;
        }

        final Item item = location.getWorld().dropItemNaturally(location, drops.get(index).getItem());

        item.setCanPlayerPickup(true);
        item.setCanMobPickup(false);
    }
}