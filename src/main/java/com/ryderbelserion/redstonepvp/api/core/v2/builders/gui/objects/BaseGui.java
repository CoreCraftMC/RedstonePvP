package com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.components.InteractionComponent;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiAction;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.IBaseGui;
import com.ryderbelserion.redstonepvp.api.core.v2.listeners.GuiListener;
import com.ryderbelserion.vital.paper.util.AdvUtil;
import com.ryderbelserion.vital.paper.util.scheduler.FoliaRunnable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.Set;

public abstract class BaseGui implements InventoryHolder, Listener, IBaseGui {

    private static final RedstonePvP plugin = RedstonePvP.getPlugin();

    static {
        plugin.getServer().getPluginManager().registerEvents(new GuiListener(), plugin);
    }

    private Set<InteractionComponent> interactionComponents;

    // Action to execute when clicking on the top part of the GUI only.
    private GuiAction<InventoryClickEvent> defaultTopClickAction;
    // Action to execute when clicking on the player Inventory.
    private GuiAction<InventoryClickEvent> playerInventoryAction;
    // Action to execute when clicked outside the GUI.
    private GuiAction<InventoryClickEvent> outsideClickAction;
    // Action to execute when clicking on any item.
    private GuiAction<InventoryClickEvent> defaultClickAction;
    // Action to execute when GUI closes.
    private GuiAction<InventoryCloseEvent> closeGuiAction;
    // Action to execute when GUI opens.
    private GuiAction<InventoryOpenEvent> openGuiAction;
    // Action to execute when dragging the item on the GUI.
    private GuiAction<InventoryDragEvent> dragAction;

    private Inventory inventory;
    private String title;
    private int rows;

    public BaseGui(final String title, final int rows, final Set<InteractionComponent> components) {
        this.interactionComponents = safeCopy(components);

        this.title = title;
        this.rows = rows;

        this.inventory = plugin.getServer().createInventory(this, this.rows * 9, title());
    }

    public BaseGui() {}

    @Override
    public final String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public final Component title() {
        return AdvUtil.parse(getTitle());
    }

    @Override
    public final int getRows() {
        return this.rows;
    }

    @Override
    public void setRows(final int rows) {
        this.rows = rows;
    }

    @Override
    public final int getSize() {
        return getRows() * 9;
    }

    @Override
    public void addInteractionComponent(final InteractionComponent... components) {
        this.interactionComponents.addAll(Arrays.asList(components));
    }

    @Override
    public void removeInteractionComponent(final InteractionComponent component) {
        this.interactionComponents.remove(component);
    }

    @Override
    public final boolean canPerformOtherActions() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_OTHER_ACTIONS);
    }

    @Override
    public final boolean isInteractionsDisabled() {
        return this.interactionComponents.size() == InteractionComponent.VALUES.size();
    }

    @Override
    public final boolean canPlaceItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_PLACE);
    }

    @Override
    public final boolean canTakeItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_TAKE);
    }

    @Override
    public final boolean canSwapItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_SWAP);
    }

    @Override
    public final boolean canDropItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_DROP);
    }

    @Override
    public void close(final Player player) {
        new FoliaRunnable(plugin.getServer().getGlobalRegionScheduler()) {
            @Override
            public void run() {
                player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            }
        }.runDelayed(plugin, 2);
    }

    @Override
    public void updateTitle(final Player player) {
        net.minecraft.network.chat.Component title = CraftChatMessage.fromJSON(JSONComponentSerializer.json().serialize(AdvUtil.parse(this.title)));
        final MenuType<?> window = CraftContainer.getNotchInventoryType(player.getOpenInventory().getTopInventory());
        final ServerPlayer entityPlayer = (ServerPlayer) ((CraftHumanEntity) player).getHandle();
        final int id = entityPlayer.containerMenu.containerId;

        entityPlayer.connection.send(new ClientboundOpenScreenPacket(id, window, title));

        player.updateInventory();
    }

    @Override
    public void updateTitles() {
        plugin.getServer().getOnlinePlayers().forEach(player -> {
            final InventoryHolder inventory = player.getOpenInventory().getTopInventory().getHolder(false);

            if (!(inventory instanceof BaseGui)) return;

            updateTitle(player);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryClickEvent> getDefaultClickAction() {
        return defaultClickAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> defaultClickAction) {
        this.defaultClickAction = defaultClickAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryClickEvent> getDefaultTopClickAction() {
        return this.defaultTopClickAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultTopClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> defaultTopClickAction) {
        this.defaultTopClickAction = defaultTopClickAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryClickEvent> getPlayerInventoryAction() {
        return this.playerInventoryAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayerInventoryAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> playerInventoryAction) {
        this.playerInventoryAction = playerInventoryAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryDragEvent> getDragAction() {
        return this.dragAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDragAction(@Nullable final GuiAction<@NotNull InventoryDragEvent> dragAction) {
        this.dragAction = dragAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryCloseEvent> getCloseGuiAction() {
        return this.closeGuiAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCloseGuiAction(@Nullable final GuiAction<@NotNull InventoryCloseEvent> closeGuiAction) {
        this.closeGuiAction = closeGuiAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryOpenEvent> getOpenGuiAction() {
        return this.openGuiAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOpenGuiAction(@Nullable final GuiAction<@NotNull InventoryOpenEvent> openGuiAction) {
        this.openGuiAction = openGuiAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryClickEvent> getOutsideClickAction() {
        return this.outsideClickAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOutsideClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> outsideClickAction) {
        this.outsideClickAction = outsideClickAction;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}