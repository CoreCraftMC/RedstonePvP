package me.corecraft.redstonepvp;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.vital.common.api.interfaces.IPlugin;
import com.ryderbelserion.vital.common.api.managers.PluginManager;
import com.ryderbelserion.vital.paper.Vital;
import com.ryderbelserion.vital.paper.api.enums.Support;
import com.ryderbelserion.vital.paper.modules.ModuleLoader;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.corecraft.redstonepvp.api.listeners.PlayerDamageListener;
import me.corecraft.redstonepvp.api.listeners.modules.combat.AttackCooldownModule;
import me.corecraft.redstonepvp.api.listeners.modules.combat.HitDelayModule;
import me.corecraft.redstonepvp.api.listeners.modules.combat.PlayerDropsModule;
import me.corecraft.redstonepvp.api.listeners.modules.items.AnvilRepairListener;
import me.corecraft.redstonepvp.api.listeners.modules.items.ItemFrameListener;
import me.corecraft.redstonepvp.command.BaseCommand;
import me.corecraft.redstonepvp.command.subs.CommandBypass;
import me.corecraft.redstonepvp.command.subs.CommandHelp;
import me.corecraft.redstonepvp.command.subs.CommandOpen;
import me.corecraft.redstonepvp.command.subs.CommandReload;
import me.corecraft.redstonepvp.command.subs.beacons.CommandBeacon;
import me.corecraft.redstonepvp.managers.BeaconManager;
import me.corecraft.redstonepvp.managers.config.ConfigManager;
import me.corecraft.redstonepvp.managers.data.DataManager;
import me.corecraft.redstonepvp.managers.data.types.Connector;
import me.corecraft.redstonepvp.support.PacketEventsSupport;
import me.corecraft.redstonepvp.support.PlaceholderAPISupport;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;
import java.util.Locale;

public class RedstonePvP extends Vital {

    public static RedstonePvP getPlugin() {
        return JavaPlugin.getPlugin(RedstonePvP.class);
    }

    private final long startTime;

    public RedstonePvP() {
        this.startTime = System.nanoTime();
    }

    private DataManager dataManager;

    @Override
    public void onLoad() {
        if (PluginManager.isEnabled("packetevents")) {
            PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this, new PacketEventsSettings().checkForUpdates(false)));
            PacketEvents.getAPI().load();
        }
    }

    @Override
    public void onEnable() {
        ConfigManager.load(getDataFolder());

        final ModuleLoader loader = getLoader();

        List.of(
                new AttackCooldownModule(),
                new PlayerDropsModule(),
                new HitDelayModule()
        ).forEach(loader::addModule);

        loader.load();

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
                    new CommandHelp(),
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
        ).forEach(key -> getServer().getPluginManager().registerEvents(key, this));

        if (Support.placeholder_api.isEnabled()) {
            new PlaceholderAPISupport().register();
        }

        getComponentLogger().info("Done ({})!", String.format(Locale.ROOT, "%.3fs", (double) (System.nanoTime() - this.startTime) / 1.0E9D));
    }

    @Override
    public void onDisable() {
        final IPlugin packets = PluginManager.getPlugin("packetevents");

        if (packets != null) {
            packets.stop();
        }

        if (this.dataManager != null) {
            final Connector connector = this.dataManager.getConnector();

            if (connector != null) {
                connector.stop();
            }
        }

        // yeet data
        getLoader().unload(true);

        getFileManager().purge();
    }

    public final DataManager getDataManager() {
        return this.dataManager;
    }
}