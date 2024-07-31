package com.ryderbelserion.redstonepvp.managers;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.managers.config.Config;
import com.ryderbelserion.vital.core.Vital;
import com.ryderbelserion.vital.paper.commands.modules.ModuleLoader;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.function.Consumer;

public class RedstoneManager extends Vital {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

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
    public @NotNull File getDirectory() {
        return this.plugin.getDataFolder();
    }

    @Override
    public void saveResource(@NotNull final String fileName, final boolean replaceExisting) {
        this.plugin.saveResource(fileName, replaceExisting);
    }

    @Override
    public final boolean isAdventure() {
        return true;
    }

    @Override
    public @NotNull final ComponentLogger getLogger() {
        return this.plugin.getComponentLogger();
    }

    @Override
    public final boolean isLogging() {
        return ConfigManager.getConfig().getProperty(Config.verbose_logging);
    }
}