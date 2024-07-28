package com.ryderbelserion.redstonepvp.api.core.v2.interfaces;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface IBaseGui {

    String getTitle();

    Component title();

    int getRows();

    int getSize();

    void close(Player player);

}