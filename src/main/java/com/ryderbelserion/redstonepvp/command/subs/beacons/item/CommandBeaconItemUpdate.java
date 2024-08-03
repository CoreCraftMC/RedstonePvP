package com.ryderbelserion.redstonepvp.command.subs.beacons.item;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.objects.beacons.Beacon;
import com.ryderbelserion.vital.paper.commands.Command;
import com.ryderbelserion.vital.paper.commands.CommandData;
import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.api.objects.beacons.BeaconDrop;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.vital.paper.util.ItemUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandBeaconItemUpdate extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandData data) {
        if (!data.isPlayer()) {
            Messages.not_a_player.sendMessage(data.getCommandSender());

            return;
        }

        final Player player = data.getPlayer();

        final ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() == Material.AIR) {
            Messages.no_item_in_hand.sendMessage(player);

            return;
        }

        final String name = data.getStringArgument("name");

        if (!BeaconManager.hasBeacon(name)) {
            Messages.beacon_location_doesnt_exist.sendMessage(player, "{name}", name);

            return;
        }

        final BeaconDrop beacon = BeaconManager.getBeacon(name).getDrop();

        final int position = data.getIntegerArgument("position");

        if (!beacon.hasPosition(position)) {
            Messages.beacon_drop_doesnt_exist.sendMessage(player, new HashMap<>() {{
                put("{position}", String.valueOf(position));
                put("{name}", name);
            }});

            return;
        }

        final String item = ItemUtil.toBase64(itemStack);

        if (beacon.hasItem(item)) {
            Messages.beacon_drop_exists.sendMessage(player);

            return;
        }

        final int min = data.getIntegerArgument("min");
        final int max = data.getIntegerArgument("max");
        final float weight = data.getFloatArgument("weight");

        beacon.addItem(item, position, min, max, weight, true);

        Messages.beacon_drop_added.sendMessage(player, new HashMap<>() {{
            put("{position}", String.valueOf(position));
            put("{min}", String.valueOf(min));
            put("{max}", String.valueOf(max));
            put("{weight}", String.valueOf(weight));
            put("{name}", name);
        }});
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.beacon.item.update";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("update").requires(source -> source.getSender().hasPermission(getPermission()));

        final RequiredArgumentBuilder<CommandSourceStack, String> arg1 = argument("name", StringArgumentType.string()).suggests((ctx, builder) -> {
            BeaconManager.getBeaconData().keySet().forEach(builder::suggest);

            return builder.buildFuture();
        });

        final RequiredArgumentBuilder<CommandSourceStack, Integer> arg2 = argument("position", IntegerArgumentType.integer()).suggests((ctx, builder) -> {
            final String name = ctx.getLastChild().getArgument("name", String.class);

            Beacon beacon = BeaconManager.getBeacon(name);

            if (beacon != null) {
                BeaconDrop drop = beacon.getDrop();

                if (drop != null) {
                    drop.getPositions().values().forEach(builder::suggest);
                }
            }

            return builder.buildFuture();
        });

        final RequiredArgumentBuilder<CommandSourceStack, Integer> arg3 = argument("min", IntegerArgumentType.integer()).suggests((ctx, builder) -> suggestIntegers(builder, 1, 60));
        final RequiredArgumentBuilder<CommandSourceStack, Integer> arg4 = argument("max", IntegerArgumentType.integer()).suggests((ctx, builder) -> suggestIntegers(builder, 1, 60));
        final RequiredArgumentBuilder<CommandSourceStack, Float> arg5 = argument("weight", FloatArgumentType.floatArg()).suggests((ctx, builder) -> suggestDoubles(builder)).executes(context -> {
            execute(new CommandData(context));

            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });

        return root.then(arg1.then(arg2.then(arg3.then(arg4.then(arg5))))).build();
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