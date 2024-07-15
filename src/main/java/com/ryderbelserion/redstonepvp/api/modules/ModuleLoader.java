package com.ryderbelserion.redstonepvp.api.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleLoader {

    private final List<ModuleHandler> modules = new ArrayList<>();

    private EventRegistry registry;

    public void load() {
        if (this.registry == null) this.registry = new EventRegistry();

        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                this.registry.addListener(module);

                return;
            }

            this.registry.removeListener(module);
        });
    }

    public void reload() {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                module.reload();
            }
        });
    }
    
    public void unload() {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                this.registry.removeListener(module);
            }
        });
    }

    public void addModule(final ModuleHandler module) {
        if (containsModule(module)) return;

        this.modules.add(module);
    }

    public void removeModule(final ModuleHandler module) {
        if (!containsModule(module)) return;

        this.modules.remove(module);
    }

    public final List<ModuleHandler> getModules() {
        return Collections.unmodifiableList(this.modules);
    }

    public final EventRegistry getRegistry() {
        return this.registry;
    }

    private boolean containsModule(final ModuleHandler module) {
        return this.modules.contains(module);
    }
}