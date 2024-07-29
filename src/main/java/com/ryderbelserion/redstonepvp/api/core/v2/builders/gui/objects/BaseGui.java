package com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects;

import com.ryderbelserion.redstonepvp.api.core.v2.builders.gui.objects.components.InteractionComponent;
import com.ryderbelserion.redstonepvp.api.core.v2.enums.GuiKeys;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiAction;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.GuiItem;
import com.ryderbelserion.redstonepvp.api.core.v2.interfaces.IBaseGui;
import com.ryderbelserion.redstonepvp.api.core.v2.listeners.GuiListener;
import com.ryderbelserion.vital.paper.util.AdvUtil;
import com.ryderbelserion.vital.paper.util.scheduler.FoliaRunnable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.NamespacedKey;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class BaseGui implements InventoryHolder, Listener, IBaseGui {

    private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(BaseGui.class);

    static {
        plugin.getServer().getPluginManager().registerEvents(new GuiListener(), plugin);
    }

    // Actions for specific slots.
    private final Map<Integer, GuiAction<InventoryClickEvent>> slotActions;
    // Contains all items the GUI will have.
    private final Map<Integer, GuiItem> guiItems;

    private final Set<InteractionComponent> interactionComponents;

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

    private final Inventory inventory;
    private String title;
    private int rows;

    public BaseGui(final String title, final int rows, final Set<InteractionComponent> components) {
        this.interactionComponents = safeCopy(components);

        this.title = title;
        this.rows = rows;

        int size = this.rows * 9;

        this.inventory = plugin.getServer().createInventory(this, size, title());

        this.slotActions = new LinkedHashMap<>(size);
        this.guiItems = new LinkedHashMap<>(size);
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public final String getTitle() {
        return this.title;
    }

    /**
     * {@inheritDoc}
     *
     * @param title {@inheritDoc}
     */
    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public final Component title() {
        return AdvUtil.parse(getTitle());
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public final int getRows() {
        return this.rows;
    }

    /**
     * {@inheritDoc}
     *
     * @param rows {@inheritDoc}
     */
    @Override
    public void setRows(final int rows) {
        this.rows = rows;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public final int getSize() {
        return getRows() * 9;
    }

    /**
     * {@inheritDoc}
     *
     * @param components {@inheritDoc}
     */
    @Override
    public void addInteractionComponent(final InteractionComponent... components) {
        this.interactionComponents.addAll(Arrays.asList(components));
    }

    /**
     * {@inheritDoc}
     *
     * @param component {@inheritDoc}
     */
    @Override
    public void removeInteractionComponent(final InteractionComponent component) {
        this.interactionComponents.remove(component);
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public final boolean canPerformOtherActions() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_OTHER_ACTIONS);
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public final boolean isInteractionsDisabled() {
        return this.interactionComponents.size() == InteractionComponent.VALUES.size();
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public final boolean canPlaceItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_PLACE);
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public final boolean canTakeItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_TAKE);
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public final boolean canSwapItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_SWAP);
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public final boolean canDropItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_DROP);
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @param itemStack {@inheritDoc}
     */
    @Override
    public void giveItem(final Player player, final ItemStack itemStack) {
        itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().remove(new NamespacedKey(plugin, "mf-gui")));

        player.getInventory().addItem(itemStack);
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @param itemStacks {@inheritDoc}
     */
    @Override
    public void giveItem(final Player player, final ItemStack... itemStacks) {
        Arrays.asList(itemStacks).forEach(item -> giveItem(player, item));
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @param guiItem {@inheritDoc}
     */
    @Override
    public void setItem(final int slot, final GuiItem guiItem) {
        this.guiItems.put(slot, guiItem);
        this.inventory.setItem(slot, guiItem.getItemStack());
    }

    /**
     * {@inheritDoc}
     *
     * @param itemStack {@inheritDoc}
     */
    @Override
    public void removeItem(final ItemStack itemStack) {
        final String key = GuiKeys.getName(itemStack);

        final Optional<Map.Entry<Integer, GuiItem>> entry = guiItems.entrySet()
                .stream()
                .filter(it -> {
                    final String pair = it.getValue().getUuid().toString();

                    return key.equalsIgnoreCase(pair);
                })
                .findFirst();

        entry.ifPresent(it -> {
            this.guiItems.remove(it.getKey());
            this.inventory.remove(it.getValue().getItemStack());
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param guiItem {@inheritDoc}
     */
    @Override
    public void removeItem(final GuiItem guiItem) {
        removeItem(guiItem.getItemStack());
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     */
    @Override
    public void removeItem(final int slot) {
        this.guiItems.remove(slot);
        this.inventory.setItem(slot, null);
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public @Nullable final GuiItem getGuiItem(final int slot) {
        return this.guiItems.get(slot);
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @param slotAction {@inheritDoc}
     */
    public void addSlotAction(final int slot, @Nullable final GuiAction<@NotNull InventoryClickEvent> slotAction) {
        this.slotActions.put(slot, slotAction);
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     */
    @Override
    public @Nullable final GuiAction<InventoryClickEvent> getSlotAction(final int slot) {
        return this.slotActions.get(slot);
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     */
    @Override
    public void close(final Player player) {
        new FoliaRunnable(plugin.getServer().getGlobalRegionScheduler()) {
            @Override
            public void run() {
                player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            }
        }.runDelayed(plugin, 2);
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     */
    @Override
    public void updateTitle(final Player player) {
        net.minecraft.network.chat.Component title = CraftChatMessage.fromJSON(JSONComponentSerializer.json().serialize(AdvUtil.parse(this.title)));
        final MenuType<?> window = CraftContainer.getNotchInventoryType(player.getOpenInventory().getTopInventory());
        final ServerPlayer entityPlayer = (ServerPlayer) ((CraftHumanEntity) player).getHandle();
        final int id = entityPlayer.containerMenu.containerId;

        entityPlayer.connection.send(new ClientboundOpenScreenPacket(id, window, title));

        player.updateInventory();
    }

    /**
     * {@inheritDoc}
     */
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
     *
     * @return {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryClickEvent> getDefaultClickAction() {
        return this.defaultClickAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param defaultClickAction {@inheritDoc}
     */
    @Override
    public void setDefaultClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> defaultClickAction) {
        this.defaultClickAction = defaultClickAction;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryClickEvent> getDefaultTopClickAction() {
        return this.defaultTopClickAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param defaultTopClickAction {@inheritDoc}
     */
    @Override
    public void setDefaultTopClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> defaultTopClickAction) {
        this.defaultTopClickAction = defaultTopClickAction;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryClickEvent> getPlayerInventoryAction() {
        return this.playerInventoryAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param playerInventoryAction {@inheritDoc}
     */
    @Override
    public void setPlayerInventoryAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> playerInventoryAction) {
        this.playerInventoryAction = playerInventoryAction;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryDragEvent> getDragAction() {
        return this.dragAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param dragAction {@inheritDoc}
     */
    @Override
    public void setDragAction(@Nullable final GuiAction<@NotNull InventoryDragEvent> dragAction) {
        this.dragAction = dragAction;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryCloseEvent> getCloseGuiAction() {
        return this.closeGuiAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param closeGuiAction {@inheritDoc}
     */
    @Override
    public void setCloseGuiAction(@Nullable final GuiAction<@NotNull InventoryCloseEvent> closeGuiAction) {
        this.closeGuiAction = closeGuiAction;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryOpenEvent> getOpenGuiAction() {
        return this.openGuiAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param openGuiAction {@inheritDoc}
     */
    @Override
    public void setOpenGuiAction(@Nullable final GuiAction<@NotNull InventoryOpenEvent> openGuiAction) {
        this.openGuiAction = openGuiAction;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final @Nullable GuiAction<InventoryClickEvent> getOutsideClickAction() {
        return this.outsideClickAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param outsideClickAction {@inheritDoc}
     */
    @Override
    public void setOutsideClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> outsideClickAction) {
        this.outsideClickAction = outsideClickAction;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Creates a {@link GuiItem} instead of an {@link ItemStack}
     *
     * @return a {@link GuiItem} with no {@link GuiAction}
     */
    public @NotNull final GuiItem asGuiItem(final ItemStack itemStack) {
        return asGuiItem(itemStack, null);
    }

    /**
     * Creates a {@link GuiItem} instead of an {@link ItemStack}
     *
     * @param action The {@link GuiAction} to apply to the item
     * @return A {@link GuiItem} with {@link GuiAction}
     */
    public @NotNull final GuiItem asGuiItem(final ItemStack itemStack, @Nullable final GuiAction<InventoryClickEvent> action) {
        return new GuiItem(itemStack, action);
    }
}