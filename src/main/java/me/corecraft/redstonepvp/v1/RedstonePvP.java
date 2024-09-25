package me.corecraft.redstonepvp.v1;

import me.corecraft.redstonepvp.v1.managers.data.DataManager;
import com.ryderbelserion.vital.paper.Vital;
import org.bukkit.plugin.java.JavaPlugin;

public class RedstonePvP extends Vital {

    public static RedstonePvP getPlugin() {
        return JavaPlugin.getPlugin(RedstonePvP.class);
    }

    private long startTime;

    @Override
    public void onLoad() {
        this.startTime = System.nanoTime();

        //if (PluginManager.isEnabled("packetevents")) {
        //    PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this, new PacketEventsSettings().checkForUpdates(false)));
        //    PacketEvents.getAPI().load();
        //}
    }

    private DataManager dataManager;

    @Override
    public void onEnable() {
        /*//getFileManager().addFile(new File(getDataFolder(), "player-drops.yml")).addFolder("static").init();

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
        */
    }

    @Override
    public void onDisable() {
        /*final IPlugin packets = PluginManager.getPlugin("packetevents");

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
        getFileManager().purge();*/
    }

    public final DataManager getDataManager() {
        return this.dataManager;
    }
}