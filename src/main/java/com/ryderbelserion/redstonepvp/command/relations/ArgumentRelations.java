package com.ryderbelserion.redstonepvp.command.relations;

import com.ryderbelserion.redstonepvp.api.enums.Messages;
import com.ryderbelserion.redstonepvp.command.MessageManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.extention.meta.MetaKey;
import dev.triumphteam.cmd.core.message.MessageKey;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class ArgumentRelations extends MessageManager {

    private String getContext(String command, String order) {
        if (command.isEmpty() || order.isEmpty()) return "";

        String usage = null;

        switch (command) {
            case "add" -> usage = order + " <beacon_id> <time>";
            case "remove" -> usage = order + " <beacon_id>";
            case "item" -> usage = order + " <beacon_id> <weight>";
        }

        return usage;
    }

    @Override
    public void build() {
        this.commandManager.registerMessage(BukkitMessageKey.UNKNOWN_COMMAND, (sender, context) -> send(sender, Messages.unknown_command.getMessage(sender, "{command}", context.getInvalidInput())));

        this.commandManager.registerMessage(MessageKey.TOO_MANY_ARGUMENTS, (sender, context) -> {
            Optional<String> meta = context.getMeta().get(MetaKey.NAME);

            meta.ifPresent(key -> {
                if (key.equalsIgnoreCase("add") || key.equalsIgnoreCase("remove") || key.equalsIgnoreCase("item")) {
                    send(sender, Messages.correct_usage.getMessage(sender, "{usage}", getContext(key, "/redstonepvp beacon " + key)));

                    return;
                }

                send(sender, Messages.correct_usage.getMessage(sender, "{usage}", getContext(key, "/redstonepvp " + key)));
            });
        });

        this.commandManager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, (sender, context) -> {
            Optional<String> meta = context.getMeta().get(MetaKey.NAME);

            meta.ifPresent(key -> {
                if (key.equalsIgnoreCase("add") || key.equalsIgnoreCase("remove") || key.equalsIgnoreCase("item")) {
                    send(sender, Messages.correct_usage.getMessage(sender, "{usage}", getContext(key, "/redstonepvp beacon " + key)));

                    return;
                }

                send(sender, Messages.correct_usage.getMessage(sender, "{usage}", getContext(key, "/redstonepvp " + key)));
            });
        });

        this.commandManager.registerMessage(MessageKey.INVALID_ARGUMENT, (sender, context) -> send(sender, Messages.correct_usage.getMessage(sender, "{usage}", context.getSyntax())));

        this.commandManager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, context) -> send(sender, Messages.no_permission.getMessage(sender, "{permission}", context.getPermission().toString())));

        this.commandManager.registerMessage(BukkitMessageKey.PLAYER_ONLY, (sender, context) -> send(sender, Messages.not_a_player.getMessage(sender)));

        this.commandManager.registerMessage(BukkitMessageKey.CONSOLE_ONLY, (sender, context) -> send(sender, Messages.must_be_console_sender.getMessage(sender)));
    }

    @Override
    public void send(@NotNull CommandSender sender, @NotNull String component) {
        sender.sendRichMessage(component);
    }
}
