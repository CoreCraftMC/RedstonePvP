package me.corecraft.redstonepvp.command.subs;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.paper.commands.PaperCommand;
import com.ryderbelserion.vital.paper.commands.context.PaperCommandInfo;
import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.api.cache.CacheManager;
import me.corecraft.redstonepvp.api.enums.Messages;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class CommandBypass extends PaperCommand {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @Override
    public void execute(final PaperCommandInfo data) {
        if (!data.isPlayer()) {
            Messages.not_a_player.sendMessage(data.getCommandSender());

            return;
        }

        final Player player = data.getPlayer();

        final boolean hasPlayer = CacheManager.containsPlayer(player);

        if (hasPlayer) {
            CacheManager.removePlayer(player);
        } else {
            CacheManager.addPlayer(player);
        }

        Messages.item_frame_bypass.sendMessage(player, "{toggle}", hasPlayer ? "enabled" : "disabled");
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.frame.bypass";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("bypass").requires(source -> source.getSender().hasPermission(getPermission()));

        return root.executes(context -> {
                    execute(new PaperCommandInfo(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                }).build();
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