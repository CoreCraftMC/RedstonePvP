package com.ryderbelserion.redstonepvp.command.subs.root;

import com.ryderbelserion.redstonepvp.api.core.builders.types.MainMenu;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.api.objects.beacons.BeaconDrop;
import com.ryderbelserion.redstonepvp.command.subs.BaseCommand;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.redstonepvp.managers.ConfigManager;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import com.ryderbelserion.vital.paper.util.ItemUtil;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.ArgName;
import dev.triumphteam.cmd.core.annotations.Command;
import dev.triumphteam.cmd.core.annotations.Suggestion;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;

public class CommandRoot extends BaseCommand {

    @Command
    @Permission(value = "redstonepvp.access", def = PermissionDefault.OP)
    public void root(Player player) {
        player.openInventory(new MainMenu(player).build().getInventory());
    }

    @Command("reload")
    @Permission(value = "redstonepvp.reload", def = PermissionDefault.OP)
    public void reload(CommandSender sender) {
        // Refresh the config.
        ConfigManager.refresh();

        // Reload modules.
        this.plugin.getLoader().reload();

        // Send the message.
        Messages.reloaded_plugin.sendMessage(sender);
    }

    @Command(value = "beacon")
    public class CommandBeacon extends BaseCommand {

        @Command
        @Permission(value = "redstonepvp.beacon.access", def = PermissionDefault.OP)
        public void beacon(Player player) {
            BeaconManager.getBeaconData().forEach((key, beacon) -> {
                this.plugin.getComponentLogger().info("Name: {}", key);

                final BeaconDrop beaconDrop = beacon.getDrop();

                this.plugin.getComponentLogger().info("Size: {}", beaconDrop.getItems().size());

                beaconDrop.getItems().forEach((item, weight) -> {
                    this.plugin.getComponentLogger().info("Weight: {}", weight);

                    final ItemStack itemStack = ItemUtil.fromBase64(item);

                    this.plugin.getComponentLogger().info("Item Type: {}", itemStack.getType());
                });
            });

            //player.openInventory(new BeaconMenu(player).build().getInventory());
        }

        @Command(value = "set")
        @Permission(value = "redstonepvp.beacon.set", def = PermissionDefault.OP)
        public void add(Player player, @ArgName("beacon_id") @Suggestion("names") String name, @ArgName("time") @Suggestion("numbers") int time) {
            final Block block = player.getTargetBlock(null, 5);

            if (block.isEmpty()) {
                Messages.not_a_block.sendMessage(player);

                return;
            }

            final String location = MiscUtils.location(block.getLocation());

            if (BeaconManager.hasValue(name) || BeaconManager.hasLocation(location)) {
                Messages.beacon_location_exists.sendMessage(player, "{name}", name);

                return;
            }

            Messages.beacon_location_added.sendMessage(player, "{name}", name);

            BeaconManager.addLocation(name, location, time);
        }

        @Command(value = "additem")
        @Permission(value = "redstonepvp.beacon.item", def = PermissionDefault.OP)
        public void item(Player player, @ArgName("beacon_id") @Suggestion("beacons") String name, @ArgName("position") @Suggestion("positions") int position, @ArgName("weights") @Suggestion("weights") double weight) {
            final ItemStack itemStack = player.getInventory().getItemInMainHand();

            if (itemStack.getType() == Material.AIR) {
                Messages.no_item_in_hand.sendMessage(player);

                return;
            }

            if (!BeaconManager.hasValue(name)) {
                Messages.beacon_location_doesnt_exist.sendMessage(player, "{name}", name);

                return;
            }

            BeaconManager.getBeacon(name).getDrop().addItem(ItemUtil.toBase64(itemStack), position, weight, true);
        }

        @Command("remove")
        @Permission(value = "redstonepvp.beacon.remove", def = PermissionDefault.OP)
        public void reload(Player player, @ArgName("beacon_id") @Suggestion("beacons") String name) {
            if (!BeaconManager.hasValue(name)) {
                Messages.beacon_location_doesnt_exist.sendMessage(player, "{name}", name);

                return;
            }

            Messages.beacon_location_removed.sendMessage(player, "{name}", name);

            BeaconManager.removeLocation(name);
        }
    }
}