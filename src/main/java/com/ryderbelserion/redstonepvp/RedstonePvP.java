package com.ryderbelserion.redstonepvp;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.redstonepvp.command.BaseCommand;
import com.ryderbelserion.redstonepvp.command.subs.CommandBypass;
import com.ryderbelserion.redstonepvp.command.subs.CommandReload;
import com.ryderbelserion.redstonepvp.config.ConfigManager;
import com.ryderbelserion.redstonepvp.listeners.PlayerDamageEvent;
import com.ryderbelserion.redstonepvp.listeners.modules.PlayerFrequencyListener;
import com.ryderbelserion.redstonepvp.listeners.modules.items.ItemFrameListener;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class RedstonePvP extends JavaPlugin {

    public static RedstonePvP getPlugin() {
        return JavaPlugin.getPlugin(RedstonePvP.class);
    }

    @Override
    public void onEnable() {
        // Load the configuration.
        ConfigManager.load(getDataFolder());

        // Register commands.
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            LiteralArgumentBuilder<CommandSourceStack> root = new BaseCommand().registerPermission().literal().createBuilder();

            List.of(
                    new CommandReload(),
                    new CommandBypass()
            ).forEach(command -> root.then(command.registerPermission().literal()));

            event.registrar().register(root.build(), "the base command for RedstonePvP");
        });

        List.of(
                new PlayerFrequencyListener(),
                new PlayerDamageEvent(),

                new ItemFrameListener()
        ).forEach(clazz -> getServer().getPluginManager().registerEvents(clazz, this));
    }

    @Override
    public void onDisable() {

    }
}