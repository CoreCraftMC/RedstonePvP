package com.ryderbelserion.redstonepvp.utils;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.objects.ItemDrop;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MiscUtils {

    private static final RedstonePvP plugin = RedstonePvP.getPlugin();

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

        final ItemStack itemStack = drops.get(index).getItem();

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

        if (day > 0) message += day + "d" + ", ";
        if (day > 0 || hour > 0) message += hour + "h" + ", ";
        if (day > 0 || hour > 0 || minute > 0) message += minute + "m" + ", ";
        if (day > 0 || hour > 0 || minute > 0 || second > 0) message += second + "s" + ", ";

        if (message.length() < 2) {
            message = "0" + "s";
        } else {
            message = message.substring(0, message.length() - 2);
        }

        return message;
    }
}