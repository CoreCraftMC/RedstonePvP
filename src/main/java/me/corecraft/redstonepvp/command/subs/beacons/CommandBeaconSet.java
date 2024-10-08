package me.corecraft.redstonepvp.command.subs.beacons;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.paper.commands.PaperCommand;
import com.ryderbelserion.vital.paper.commands.context.PaperCommandInfo;
import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.api.enums.Messages;
import me.corecraft.redstonepvp.managers.BeaconManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.corecraft.redstonepvp.utils.MiscUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandBeaconSet extends PaperCommand {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final PaperCommandInfo data) {
        if (!data.isPlayer()) {
            Messages.not_a_player.sendMessage(data.getCommandSender());

            return;
        }

        final Player player = data.getPlayer();

        final Block block = player.getTargetBlock(null, 5);

        if (block.isEmpty()) {
            Messages.not_a_block.sendMessage(player);

            return;
        }

        final String location = MiscUtils.location(block.getLocation());

        final String name = data.getStringArgument("name");

        if (BeaconManager.hasBeacon(name) || BeaconManager.hasLocation(location)) {
            Messages.beacon_drop_party_exists.sendMessage(player, "{name}", name);

            return;
        }

        Messages.beacon_drop_party_added.sendMessage(player, "{name}", name);

        BeaconManager.addLocation(name, location, data.getStringArgument("time"));
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.beacon.set";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("set").requires(source -> source.getSender().hasPermission(getPermission()));

        final RequiredArgumentBuilder<CommandSourceStack, String> arg1 = argument("name", StringArgumentType.string()).suggests((ctx, builder) -> suggestNames(builder));

        final RequiredArgumentBuilder<CommandSourceStack, String> arg2 = argument("time", StringArgumentType.string()).suggests((ctx, builder) -> {
            List.of("1m", "5m", "15m", "30m", "45m", "1h").forEach(builder::suggest);

            return builder.buildFuture();
        }).executes(context -> {
            execute(new PaperCommandInfo(context));

            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });

        return root.then(arg1.then(arg2)).build();
    }

    @Override
    public @NotNull final PaperCommand registerPermission() {
        final Permission permission = this.plugin.getServer().getPluginManager().getPermission(getPermission());

        if (permission == null) {
            this.plugin.getServer().getPluginManager().addPermission(new Permission(getPermission(), PermissionDefault.OP));
        }

        return this;
    }
}