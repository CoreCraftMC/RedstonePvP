package com.ryderbelserion.redstonepvp.managers;

import com.ryderbelserion.redstonepvp.managers.config.types.Config;
import com.ryderbelserion.redstonepvp.managers.config.ConfigManager;
import com.ryderbelserion.vital.paper.VitalPaper;
import com.ryderbelserion.vital.paper.api.commands.modules.ModuleLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

public class RedstoneManager extends VitalPaper {

    public RedstoneManager(final JavaPlugin plugin) {
        super(plugin);

        ConfigManager.load(plugin.getDataFolder());
    }

    private ModuleLoader moduleLoader;

    public final RedstoneManager start() {
        return start(null);
    }

    public final RedstoneManager start(@Nullable final Consumer<RedstoneManager> consumer) {
        this.moduleLoader = new ModuleLoader();

        apply(consumer);

        return this;
    }

    public void apply(@Nullable final Consumer<RedstoneManager> consumer) {
        if (consumer != null) {
            consumer.accept(this);
        }
    }

    public @NotNull final ModuleLoader getModuleLoader() {
        return this.moduleLoader;
    }

    @Override
    public final boolean isLegacy() {
        return false;
    }

    @Override
    public final boolean isVerbose() {
        return ConfigManager.getConfig().getProperty(Config.verbose_logging);
    }
}