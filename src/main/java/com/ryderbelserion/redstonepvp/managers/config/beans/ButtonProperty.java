package com.ryderbelserion.redstonepvp.managers.config.beans;

public class ButtonProperty {

    private String name;
    private String material;
    private int slot;

    public ButtonProperty(final String name, final String material, final int slot) {
        this.name = name;
        this.material = material;
        this.slot = slot;
    }

    public ButtonProperty() {
        this.name = "";
        this.material = "stone";
        this.slot = -1;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setMaterial(final String material) {
        this.material = material;
    }

    public void setSlot(final int slot) {
        this.slot = slot;
    }

    public final String getName() {
        return this.name;
    }

    public final String getMaterial() {
        return this.material;
    }

    public final int getSlot() {
        return this.slot;
    }
}