package me.corecraft.redstonepvp;

import com.ryderbelserion.vital.paper.Vital;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Locale;

public class RedstonePvP extends Vital {

    public static RedstonePvP getPlugin() {
        return JavaPlugin.getPlugin(RedstonePvP.class);
    }

    private final long startTime;

    public RedstonePvP() {
        this.startTime = System.nanoTime();
    }

    @Override
    public void onEnable() {
        getComponentLogger().info("Done ({})!", String.format(Locale.ROOT, "%.3fs", (double) (System.nanoTime() - this.startTime) / 1.0E9D));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}