package me.corecraft.redstonepvp.utils;

import com.ryderbelserion.vital.paper.api.enums.Support;
import com.ryderbelserion.vital.paper.util.scheduler.FoliaRunnable;
import me.clip.placeholderapi.PlaceholderAPI;
import me.corecraft.redstonepvp.RedstonePvP;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;

public class MiscUtils {

    private static final RedstonePvP plugin = RedstonePvP.getPlugin();

    public static void sendCommand(@Nullable final CommandSender sender, @NotNull final String command, @NotNull final Map<String, String> placeholders) {
        if (command.isEmpty()) return;

        final Server server = plugin.getServer();

        final String result = populatePlaceholders(sender, command, placeholders);

        new FoliaRunnable(server.getGlobalRegionScheduler()) {
            @Override
            public void run() {
                server.dispatchCommand(server.getConsoleSender(), result);
            }
        }.run(plugin);
    }

    public static void sendCommand(@NotNull final String command, @NotNull final Map<String, String> placeholders) {
        sendCommand(null, command, placeholders);
    }

    public static void sendCommand(@NotNull final String command) {
        sendCommand(command, new HashMap<>());
    }

    public static String populatePlaceholders(@Nullable final CommandSender sender, @NotNull String line, @NotNull final Map<String, String> placeholders) {
        if (sender != null && Support.placeholder_api.isEnabled()) {
            if (sender instanceof Player player) {
                line = PlaceholderAPI.setPlaceholders(player, line);
            }
        }

        if (!placeholders.isEmpty()) {
            for (final Map.Entry<String, String> placeholder : placeholders.entrySet()) {

                if (placeholder != null) {
                    final String key = placeholder.getKey();
                    final String value = placeholder.getValue();

                    if (key != null && value != null) {
                        line = line.replace(key, value).replace(key.toLowerCase(), value);
                    }
                }
            }
        }

        return line;
    }
}