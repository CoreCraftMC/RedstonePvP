package com.ryderbelserion.redstonepvp.utils;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.objects.ItemDrop;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
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

        final Item item = location.getWorld().dropItemNaturally(location, drops.get(index).getItem());

        item.setCanPlayerPickup(true);
        item.setCanMobPickup(false);
    }
}