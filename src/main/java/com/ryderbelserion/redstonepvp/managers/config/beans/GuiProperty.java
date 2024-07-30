package com.ryderbelserion.redstonepvp.managers.config.beans;

import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiType;

public class GuiProperty {

    private GuiType guiType;
    private String title;
    private int rows;

    public GuiProperty(final GuiType guiType, final String title, final int rows) {
        this.guiType = guiType;
        this.title = title;
        this.rows = rows;
    }

    public GuiProperty(final String title, final int rows) {
        this(GuiType.CHEST, title, rows);
    }

    public GuiProperty() {
        this.guiType = GuiType.CHEST;
        this.title = "";
        this.rows = 6;
    }

    public void setGuiType(final GuiType guiType) {
        this.guiType = guiType;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setRows(final int rows) {
        this.rows = rows;
    }

    public final GuiType getGuiType() {
        return this.guiType;
    }

    public final String getTitle() {
        return this.title;
    }

    public final int getRows() {
        return this.rows;
    }
}