package com.ryderbelserion.redstonepvp;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.redstonepvp.api.modules.ModuleLoader;
import com.ryderbelserion.redstonepvp.command.BaseCommand;
import com.ryderbelserion.redstonepvp.command.subs.CommandBypass;
import com.ryderbelserion.redstonepvp.command.subs.CommandReload;
import com.ryderbelserion.redstonepvp.config.ConfigManager;
import com.ryderbelserion.redstonepvp.listeners.PlayerDamageListener;
import com.ryderbelserion.redstonepvp.listeners.modules.PlayerPacketModule;
import com.ryderbelserion.redstonepvp.listeners.modules.combat.AttackCooldownModule;
import com.ryderbelserion.redstonepvp.listeners.modules.combat.HitDelayModule;
import com.ryderbelserion.redstonepvp.listeners.modules.items.ItemFrameListener;
import com.ryderbelserion.redstonepvp.support.PacketEventsSupport;
import com.ryderbelserion.vital.paper.plugins.PluginManager;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class RedstonePvP extends JavaPlugin {

    public static RedstonePvP getPlugin() {
        return JavaPlugin.getPlugin(RedstonePvP.class);
    }

    private ModuleLoader loader;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this, new PacketEventsSettings().checkForUpdates(false)));
        PacketEvents.getAPI().load();
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

        PluginManager.registerPlugin(new PacketEventsSupport());

        this.loader = new ModuleLoader();

        List.of(
                new AttackCooldownModule(),
                new HitDelayModule()
        ).forEach(module -> this.loader.addModule(module));

        this.loader.load();

        List.of(
                new PlayerDamageListener(),
                new ItemFrameListener()
        ).forEach(clazz -> getServer().getPluginManager().registerEvents(clazz, this));
    }

    @Override
    public void onDisable() {

    }

    public final ModuleLoader getLoader() {
        return this.loader;
    }
}