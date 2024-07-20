package com.ryderbelserion.redstonepvp.command.subs.root;

import com.ryderbelserion.redstonepvp.api.core.builders.types.BeaconMenu;
import com.ryderbelserion.redstonepvp.api.core.builders.types.MainMenu;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.api.objects.beacons.BeaconDrop;
import com.ryderbelserion.redstonepvp.command.subs.BaseCommand;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.redstonepvp.managers.ConfigManager;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
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
import java.util.Base64;

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
            player.openInventory(new BeaconMenu(player).build().getInventory());
        }

        @Command(value = "add")
        @Permission(value = "redstonepvp.beacon.add", def = PermissionDefault.OP)
        public void add(Player player, @ArgName("beacon_id") @Suggestion("names") String name, @ArgName("time") @Suggestion("numbers") int time) {
            final Block block = player.getTargetBlock(null, 5);

            if (block.isEmpty()) {
                Messages.not_a_block.sendMessage(player);

                return;
            }

            final String location = MiscUtils.location(block.getLocation());

            if (BeaconManager.hasValue(name) || BeaconManager.hasLocation(location)) {
                Messages.beacon_drop_exists.sendMessage(player, "{name}", name);

                return;
            }

            Messages.beacon_drop_added.sendMessage(player, "{name}", name);

            BeaconManager.addLocation(name, location, time);
        }

        @Command("item")
        @Permission(value = "redstonepvp.beacon.item", def = PermissionDefault.OP)
        public void item(Player player, @ArgName("beacon_id") @Suggestion("beacons") String name, @ArgName("weight") @Suggestion("numbers") int weight) {
            final ItemStack itemStack = player.getInventory().getItemInMainHand();

            if (itemStack.getType() == Material.AIR) {
                Messages.no_item_in_hand.sendMessage(player);

                return;
            }

            if (!BeaconManager.hasValue(name)) {
                Messages.beacon_drop_doesnt_exist.sendMessage(player, "{name}", name);

                return;
            }

            final BeaconDrop beacon = BeaconManager.getBeacon(name).getDrop();

            final String base64 = Base64.getEncoder().encodeToString(itemStack.serializeAsBytes());

            if (beacon.containsItem(base64)) {
                player.sendMessage("The drop already exists.");

                return;
            }

            beacon.addItem(base64, weight);
        }

        @Command("remove")
        @Permission(value = "redstonepvp.beacon.remove", def = PermissionDefault.OP)
        public void reload(Player player, @ArgName("beacon_id") @Suggestion("beacons") String name) {
            if (!BeaconManager.hasValue(name)) {
                Messages.beacon_drop_doesnt_exist.sendMessage(player, "{name}", name);

                return;
            }

            Messages.beacon_drop_removed.sendMessage(player, "{name}", name);

            BeaconManager.removeLocation(name);
        }
    }
}