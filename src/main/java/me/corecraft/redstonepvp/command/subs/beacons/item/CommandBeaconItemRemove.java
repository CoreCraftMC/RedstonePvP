package me.corecraft.redstonepvp.command.subs.beacons.item;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.api.enums.Messages;
import me.corecraft.redstonepvp.api.objects.beacons.Beacon;
import me.corecraft.redstonepvp.api.objects.beacons.BeaconDrop;
import me.corecraft.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.vital.paper.api.commands.Command;
import com.ryderbelserion.vital.paper.api.commands.CommandData;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandBeaconItemRemove extends Command {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final CommandData data) {
        if (!data.isPlayer()) {
            Messages.not_a_player.sendMessage(data.getCommandSender());

            return;
        }

        final Player player = data.getPlayer();

        final String name = data.getStringArgument("name");

        if (!BeaconManager.hasBeacon(name)) {
            Messages.beacon_drop_party_doesnt_exist.sendMessage(player, "{name}", name);

            return;
        }

        final BeaconDrop drop = BeaconManager.getBeacon(name).getDrop();

        final int position = data.getIntegerArgument("position");

        if (!drop.hasPosition(position)) {
            Messages.beacon_drop_doesnt_exist.sendMessage(player, new HashMap<>() {{
                put("{position}", String.valueOf(position));
                put("{name}", name);
            }});

            return;
        }

        drop.removeItem(position);

        Messages.beacon_drop_removed.sendMessage(player, new HashMap<>() {{
            put("{position}", String.valueOf(position));
            put("{name}", name);
        }});
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.beacon.item.remove";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("remove").requires(source -> source.getSender().hasPermission(getPermission()));

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
        }).executes(context -> {
            execute(new CommandData(context));

            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });

        return root.then(arg1.then(arg2)).build();
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