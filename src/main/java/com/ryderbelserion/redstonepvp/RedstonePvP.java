package com.ryderbelserion.redstonepvp;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.redstonepvp.command.BaseCommand;
import com.ryderbelserion.redstonepvp.command.subs.CommandBypass;
import com.ryderbelserion.redstonepvp.command.subs.CommandOpen;
import com.ryderbelserion.redstonepvp.command.subs.CommandReload;
import com.ryderbelserion.redstonepvp.command.subs.beacons.CommandBeacon;
import com.ryderbelserion.redstonepvp.listeners.modules.combat.AttackCooldownModule;
import com.ryderbelserion.redstonepvp.listeners.modules.combat.HitDelayModule;
import com.ryderbelserion.redstonepvp.listeners.modules.combat.PlayerDropsModule;
import com.ryderbelserion.redstonepvp.managers.BeaconManager;
import com.ryderbelserion.redstonepvp.managers.data.DataManager;
import com.ryderbelserion.redstonepvp.listeners.PlayerDamageListener;
import com.ryderbelserion.redstonepvp.listeners.modules.items.AnvilRepairListener;
import com.ryderbelserion.redstonepvp.listeners.modules.items.ItemFrameListener;
import com.ryderbelserion.redstonepvp.managers.RedstoneManager;
import com.ryderbelserion.redstonepvp.managers.data.types.Connector;
import com.ryderbelserion.redstonepvp.support.PacketEventsSupport;
import com.ryderbelserion.vital.paper.commands.modules.ModuleHandler;
import com.ryderbelserion.vital.paper.commands.modules.ModuleLoader;
import com.ryderbelserion.vital.paper.plugins.PluginManager;
import com.ryderbelserion.vital.paper.plugins.interfaces.Plugin;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;
import java.util.Locale;

public class RedstonePvP extends JavaPlugin {

    public static RedstonePvP getPlugin() {
        return JavaPlugin.getPlugin(RedstonePvP.class);
    }

    private List<ItemStack> items;
    private long startTime;

    @Override
    public void onLoad() {
        this.startTime = System.nanoTime();

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this, new PacketEventsSettings().checkForUpdates(false)));
        PacketEvents.getAPI().load();
    }

    private DataManager dataManager;

    private RedstoneManager redstone;

    @Override
    public void onEnable() {
        this.redstone = new RedstoneManager(this).start(redstone -> {
            final ModuleLoader moduleLoader = redstone.getModuleLoader();

            List.of(
                    new AttackCooldownModule(),
                    new PlayerDropsModule(),
                    new HitDelayModule()
            ).forEach(moduleLoader::addModule);

            moduleLoader.load(this);
        });

        // Create data manager.
        this.dataManager = new DataManager().init();

        // Populate existing beacon drop locations in the cache.
        BeaconManager.populate(this.dataManager);

        // Start run tasks.
        BeaconManager.startTasks(false);

        // Register commands.
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            LiteralArgumentBuilder<CommandSourceStack> root = new BaseCommand().registerPermission().literal().createBuilder();

            List.of(
                    new CommandBeacon(),
                    new CommandReload(),
                    new CommandBypass(),
                    new CommandOpen()
            ).forEach(command -> root.then(command.registerPermission().literal()));

            event.registrar().register(root.build(), "the base command for RedstonePvP");
        });

        // Register packets support.
        PluginManager.registerPlugin(new PacketEventsSupport());

        // Register listeners.
        List.of(
                new PlayerDamageListener(),
                new AnvilRepairListener(),
                new ItemFrameListener()

                // Menu listeners
                //new SettingsMenu(),
                //new PlayersMenu(),
                //new BeaconMenu(),
                //new MainMenu(),
                //new ItemMenu()
        ).forEach(key -> getServer().getPluginManager().registerEvents(key, this));

        if (Support.placeholder_api.isEnabled()) {
            new PlaceholderAPISupport().register();
        }

        getComponentLogger().info("Done ({})!", String.format(Locale.ROOT, "%.3fs", (double) (System.nanoTime() - this.startTime) / 1.0E9D));
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

        if (this.redstone == null) return;

        this.redstone.apply(redstone -> {
            final ModuleLoader moduleLoader = redstone.getModuleLoader();

            moduleLoader.getModules().forEach(ModuleHandler::disable);
        });
    }

    public final DataManager getDataManager() {
        return this.dataManager;
    }

    public final RedstoneManager getRedstone() {
        return this.redstone;
    }
}