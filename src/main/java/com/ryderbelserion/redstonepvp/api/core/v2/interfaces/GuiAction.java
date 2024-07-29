package com.ryderbelserion.redstonepvp.api.core.v2.interfaces;

import org.bukkit.event.Event;

@FunctionalInterface
public interface GuiAction<T extends Event> {

    /**
     * Executes the event passed to it
     *
     * @param event Inventory action
     */
    void execute(final T event);

}