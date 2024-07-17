package com.ryderbelserion.redstonepvp;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.redstonepvp.api.core.command.modules.ModuleLoader;
import com.ryderbelserion.redstonepvp.command.BaseCommand;
import com.ryderbelserion.redstonepvp.command.subs.CommandBypass;
import com.ryderbelserion.redstonepvp.command.subs.CommandReload;
import com.ryderbelserion.redstonepvp.command.subs.beacons.CommandBeacon;
import com.ryderbelserion.redstonepvp.listeners.modules.combat.PlayerDropsModule;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.redstonepvp.managers.ConfigManager;
import com.ryderbelserion.redstonepvp.managers.DataManager;
import com.ryderbelserion.redstonepvp.managers.config.Config;
import com.ryderbelserion.redstonepvp.listeners.PlayerDamageListener;
import com.ryderbelserion.redstonepvp.listeners.modules.combat.AttackCooldownModule;
import com.ryderbelserion.redstonepvp.listeners.modules.combat.HitDelayModule;
import com.ryderbelserion.redstonepvp.listeners.modules.items.AnvilRepairListener;
import com.ryderbelserion.redstonepvp.listeners.modules.items.ItemFrameListener;
import com.ryderbelserion.redstonepvp.managers.data.Connector;
import com.ryderbelserion.redstonepvp.support.PacketEventsSupport;
import com.ryderbelserion.vital.paper.VitalPaper;
import com.ryderbelserion.vital.paper.plugins.PluginManager;
import com.ryderbelserion.vital.paper.plugins.interfaces.Plugin;
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

    private DataManager dataManager;

    @Override
    public void onEnable() {
        // Create the instance of the framework.
        final VitalPaper vital = new VitalPaper(this);

        // Load the configuration.
        ConfigManager.load(getDataFolder());

        // Set if we should log in the framework.
        vital.setLogging(ConfigManager.getConfig().getProperty(Config.verbose_logging));

        // Create data manager.
        this.dataManager = new DataManager().init();

        // Populate existing beacon drop locations in the cache.
        BeaconManager.populate(this.dataManager);

        // Register commands.
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            LiteralArgumentBuilder<CommandSourceStack> root = new BaseCommand().registerPermission().literal().createBuilder();

            List.of(
                    new CommandBeacon(),
                    new CommandReload(),
                    new CommandBypass()
            ).forEach(command -> root.then(command.registerPermission().literal()));

            event.registrar().register(root.build(), "the base command for RedstonePvP");
        });

        // Register packets support.
        PluginManager.registerPlugin(new PacketEventsSupport());

        // Load modules.
        this.loader = new ModuleLoader();

        List.of(
                new AttackCooldownModule(),
                new PlayerDropsModule(),
                new HitDelayModule()
        ).forEach(module -> this.loader.addModule(module));

        this.loader.load();

        // Register listeners.
        List.of(
                new PlayerDamageListener(),
                new AnvilRepairListener(),
                new ItemFrameListener()
        ).forEach(clazz -> getServer().getPluginManager().registerEvents(clazz, this));
    }

    @Override
    public void onDisable() {
        final Plugin packets = PluginManager.getPlugin("packetevents");

        if (packets != null) {
            packets.remove();
        }

        if (this.dataManager != null) {
            final Connector connector = this.dataManager.getConnector();

            if (connector != null) {
                connector.stop();
            }
        }

        if (this.loader != null) {
            this.loader.unload();
        }
    }

    public final ModuleLoader getLoader() {
        return this.loader;
    }

    public final DataManager getDataManager() {
        return this.dataManager;
    }
}