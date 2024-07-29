package com.ryderbelserion.redstonepvp.api.core.v2.test;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.GuiBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BasicCommand implements io.papermc.paper.command.brigadier.BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (!(stack.getSender() instanceof Player player)) return;

        final GuiBuilder gui = GuiBuilder.gui().setTitle("<red>Beans!").setRows(3).create();

        player.openInventory(gui.getInventory());
    }
}