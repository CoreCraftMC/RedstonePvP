package com.ryderbelserion.redstonepvp.api.core.v2.interfaces;

import net.kyori.adventure.text.Component;

public interface IBaseGui {

    String getTitle();

    Component title();

    int getRows();

    int getSize();

}