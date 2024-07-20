package com.ryderbelserion.redstonepvp.command;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.command.relations.ArgumentRelations;
import com.ryderbelserion.redstonepvp.command.subs.root.CommandBypass;
import com.ryderbelserion.redstonepvp.command.subs.root.CommandRoot;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.vital.paper.builders.PlayerBuilder;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandManager {

    private final static RedstonePvP plugin = RedstonePvP.getPlugin();

    private final static BukkitCommandManager<CommandSender> commandManager = BukkitCommandManager.create(plugin);

    /**
     * Loads commands.
     */
    public static void load() {
        new ArgumentRelations().build();

        commandManager.registerSuggestion(SuggestionKey.of("players"), (sender, context) -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).toList());

        commandManager.registerSuggestion(SuggestionKey.of("numbers"), (sender, context) -> {
            final List<String> numbers = new ArrayList<>();

            for (int i = 1; i <= 64; i++) numbers.add(String.valueOf(i));

            return numbers;
        });

        commandManager.registerSuggestion(SuggestionKey.of("positions"), (sender, context) -> {
            final List<String> numbers = new ArrayList<>();

            for (int i = 1; i <= 64; i++) numbers.add(String.valueOf(i));

            return numbers;
        });

       commandManager.registerSuggestion(SuggestionKey.of("weights"), (sender, context) -> {
            final List<String> numbers = new ArrayList<>();

            for (double i = 0.1; i <= 64.0; i++) numbers.add(String.valueOf(i));

            return numbers;
        });

        commandManager.registerSuggestion(SuggestionKey.of("beacons"), ((sender, arguments) -> new ArrayList<>() {{
            addAll(BeaconManager.getBeaconData().keySet().stream().toList());
        }}));

        commandManager.registerSuggestion(SuggestionKey.of("names"), ((sender, arguments) -> new ArrayList<>() {{
            for (int i = 0; i < 7; i++) {
                add(UUID.randomUUID().toString().replace("-", "").substring(0, 8));
            }
        }}));

        commandManager.registerArgument(PlayerBuilder.class, (sender, context) -> new PlayerBuilder(context));

        List.of(
                new CommandRoot(),
                new CommandBypass()

                //new CommandBeacon()
        ).forEach(commandManager::registerCommand);
    }

    public static BukkitCommandManager<CommandSender> getCommandManager() {
        return commandManager;
    }
}