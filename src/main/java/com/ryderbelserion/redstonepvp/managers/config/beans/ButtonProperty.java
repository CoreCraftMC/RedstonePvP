package com.ryderbelserion.redstonepvp.managers.config.beans;

import com.ryderbelserion.redstonepvp.api.keys.ItemKeys;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;

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

    public void setMaterial(final String material) {
        this.material = material;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setSlot(final int slot) {
        this.slot = slot;
    }

    public final String getMaterial() {
        return this.material;
    }

    public final String getName() {
        return this.name;
    }

    public final int getSlot() {
        return this.slot;
    }

    // not config options
    /*public ItemBuilder build(final String id) {
        return new ItemBuilder()
                .withType(getMaterial())
                .setDisplayName(getName())
                .setPersistentString(ItemKeys.item_key, id);
    }*/
}