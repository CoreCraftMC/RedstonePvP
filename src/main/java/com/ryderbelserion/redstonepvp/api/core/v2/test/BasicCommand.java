package com.ryderbelserion.redstonepvp.api.core.v2.test;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.GuiBuilder;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiItem;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BasicCommand implements io.papermc.paper.command.brigadier.BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (!(stack.getSender() instanceof Player player)) return;

        final GuiBuilder gui = GuiBuilder.gui().setTitle("<red>Beans!")
                .disableItemTake()
                .disableItemDrop()
                .disableItemSwap()
                .disableItemPlacement()
                .setRows(3).create();

        GuiItem guiItem = gui.asGuiItem(new ItemStack(Material.STRING), event -> {
            final @NotNull HumanEntity clicker = event.getWhoClicked();

            clicker.sendMessage("You clicked " + event.getSlot());
        });

        gui.setItem(5, guiItem);

        player.openInventory(gui.getInventory());
    }
}