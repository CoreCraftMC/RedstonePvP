package me.corecraft.redstonepvp.utils;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.vital.paper.api.enums.Support;
import com.ryderbelserion.vital.paper.util.scheduler.FoliaRunnable;
import me.clip.placeholderapi.PlaceholderAPI;
import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.api.objects.drops.ItemDrop;
import me.corecraft.redstonepvp.managers.config.ConfigManager;
import me.corecraft.redstonepvp.managers.config.impl.Config;
import me.youhavetrouble.yardwatch.Protection;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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

    private static final SettingsManager config = ConfigManager.getConfig();

    public static String location(@NotNull final Location location, boolean getName) {
        String name = getName ? location.getWorld().getName() : String.valueOf(location.getWorld().getUID());

        return name + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

    public static String location(@NotNull final Location location) {
        return location(location, false);
    }

    public static Location location(@NotNull final String location) {
        final String[] split = location.split(",");

        return new Location(plugin.getServer().getWorld(UUID.fromString(split[0])), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    public static void playSound(final Location location, final Sound sound) {
        location.getNearbyPlayers(7.5).forEach(player -> player.playSound(location, sound, 1f, 1f));
    }

    public static void message(@NotNull CommandSender sender, @NotNull String message) {
        if (sender instanceof Player player) {
            player.sendRichMessage(PlaceholderAPI.setPlaceholders(player, message));

            return;
        }

        sender.sendRichMessage(message);
    }

    public static void getDrop(final Location location, final List<ItemDrop> drops) {
        double weight = 0.0;

        for (ItemDrop itemDrop : drops) {
            weight += itemDrop.getWeight();
        }

        int index = 0;

        for (double random = ThreadLocalRandom.current().nextDouble() * weight; index < drops.size() - 1; index++) {
            random -= drops.get(index).getWeight();

            if (random < 0.0) break;
        }

        final ItemStack itemStack = drops.get(index).getItemStack();

        if (itemStack != null) {
            final Item item = location.getWorld().dropItemNaturally(location, itemStack);

            item.setCanPlayerPickup(true);
            item.setCanMobPickup(false);
        }
    }

    public static Calendar getTimeFromString(final String time) {
        Calendar calendar = Calendar.getInstance();

        for (String i : time.split(" ")) {
            if (i.contains("D") || i.contains("d")) calendar.add(Calendar.DATE, Integer.parseInt(i.replace("D", "").replace("d", "")));

            if (i.contains("H") || i.contains("h")) calendar.add(Calendar.HOUR, Integer.parseInt(i.replace("H", "").replace("h", "")));

            if (i.contains("M") || i.contains("m")) calendar.add(Calendar.MINUTE, Integer.parseInt(i.replace("M", "").replace("m", "")));

            if (i.contains("S") || i.contains("s")) calendar.add(Calendar.SECOND, Integer.parseInt(i.replace("S", "").replace("s", "")));
        }

        return calendar;
    }

    public static String convertTimeToString(final Calendar timeTill) {
        final Calendar rightNow = Calendar.getInstance();

        int total = ((int) (timeTill.getTimeInMillis() / 1000) - (int) (rightNow.getTimeInMillis() / 1000));
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;

        for (; total > 86400; total -= 86400, day++);
        for (; total > 3600; total -= 3600, hour++);
        for (; total >= 60; total -= 60, minute++);

        second += total;
        String message = "";

        if (day > 0) message += day + config.getProperty(Config.time_placeholder_day) + ", ";
        if (day > 0 || hour > 0) message += hour + config.getProperty(Config.time_placeholder_hour) + ", ";
        if (day > 0 || hour > 0 || minute > 0) message += minute + config.getProperty(Config.time_placeholder_minute) + ", ";
        if (day > 0 || hour > 0 || minute > 0 || second > 0) message += second + config.getProperty(Config.time_placeholder_second) + ", ";

        if (message.length() < 2) {
            message = "0" + config.getProperty(Config.time_placeholder_second);
        } else {
            message = message.substring(0, message.length() - 2);
        }

        return message;
    }

    public static boolean cantDamage(final Player player, final Player target) {
        if (!Support.yard_watch.isEnabled()) {
            return true;
        }

        final Collection<RegisteredServiceProvider<Protection>> protections = plugin.getServer().getServicesManager().getRegistrations(Protection.class);

        for (final RegisteredServiceProvider<Protection> protection : protections) {
            if (protection.getProvider().canDamage(player, target)) continue;

            return false;
        }

        return true;
    }
}