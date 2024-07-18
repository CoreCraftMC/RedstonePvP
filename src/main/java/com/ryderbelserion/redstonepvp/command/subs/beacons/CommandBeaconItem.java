package com.ryderbelserion.redstonepvp.command.subs.beacons;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.core.command.objects.Command;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.api.objects.beacons.Beacon;
import com.ryderbelserion.redstonepvp.api.objects.beacons.BeaconDrop;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import java.util.Base64;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandBeaconItem extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandContext<CommandSourceStack> stack) {
        final CommandSender sender = stack.getSource().getSender();

        if (!(sender instanceof Player player)) {
            Messages.not_a_player.sendMessage(sender);

            return;
        }

        final String name = stack.getArgument("name", String.class);

        if (name == null || name.isEmpty() || name.isBlank()) {
            //todo() add message saying it can't be invalid.

            return;
        }

        if (!BeaconManager.hasValue(name)) {
            Messages.beacon_drop_doesnt_exist.sendMessage(player, "{name}", name);

            return;
        }

        final ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() == Material.AIR) {
            Messages.no_item_in_hand.sendMessage(sender);

            return;
        }

        if (BeaconManager.hasValue(name)) {
            Messages.beacon_drop_doesnt_exist.sendMessage(player, "{name}", name);

            return;
        }

        final String base64 = Base64.getEncoder().encodeToString(itemStack.serializeAsBytes());
    }

    @Override
    public final String getPermission() {
        return "redstonepvp.beacon.add";
    }

    @Override
    public final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("item")
                .requires(source -> source.getSender().hasPermission(getPermission()))
                .then(argument("name", StringArgumentType.string())
                .suggests((context, builder) -> {
                    BeaconManager.getBeaconData().keySet().forEach(builder::suggest);

                    return builder.buildFuture();
                })
                .executes(context -> {
                    execute(context);

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })).build();
    }

    @Override
    public Command registerPermission() {
        final Permission permission = this.plugin.getServer().getPluginManager().getPermission(getPermission());

        if (permission == null) {
            this.plugin.getServer().getPluginManager().addPermission(new Permission(getPermission(), PermissionDefault.OP));
        }

        return this;
    }
}