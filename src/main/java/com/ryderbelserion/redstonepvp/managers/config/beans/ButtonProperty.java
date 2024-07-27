package com.ryderbelserion.redstonepvp.managers.config.beans;

public class ButtonProperty {

    private String name;
    private int slot;

    public ButtonProperty(final String name, final int slot) {
        this.name = name;
        this.slot = slot;
    }

    public ButtonProperty() {
        this.name = "";
        this.slot = -1;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setSlot(final int slot) {
        this.slot = slot;
    }

    public final String getName() {
        return this.name;
    }

    public final int getSlot() {
        return this.slot;
    }
}