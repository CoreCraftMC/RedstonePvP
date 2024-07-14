package com.ryderbelserion.redstonepvp;

import com.ryderbelserion.redstonepvp.command.BaseCommand;
import com.ryderbelserion.redstonepvp.config.ConfigManager;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RedstonePvP extends JavaPlugin {

    @Override
    public void onEnable() {
        // Load the configuration.
        ConfigManager.load(getDataFolder());

        // Register the base command.
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final @NotNull Commands commands = event.registrar();

            commands.register("redstone", new BaseCommand());
        });
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}