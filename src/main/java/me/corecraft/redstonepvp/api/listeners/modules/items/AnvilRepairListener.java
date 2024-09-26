package me.corecraft.redstonepvp.api.listeners.modules.items;

import me.corecraft.redstonepvp.RedstonePvP;
import me.corecraft.redstonepvp.api.enums.Messages;
import me.corecraft.redstonepvp.managers.config.ConfigManager;
import me.corecraft.redstonepvp.managers.config.impl.Config;
import com.ryderbelserion.vital.paper.util.ItemUtil;
import com.ryderbelserion.vital.paper.util.scheduler.FoliaRunnable;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import java.util.HashMap;

public class AnvilRepairListener implements Listener {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    @EventHandler
    public void onAnvilInteract(PlayerInteractEvent event) {
        final Block block = event.getClickedBlock();

        if (block == null || event.getAction().isLeftClick() || event.getHand() != EquipmentSlot.HAND || block.getType() != Material.ANVIL) return;

        // Deny the interaction.
        event.setUseInteractedBlock(Event.Result.DENY);

        // Get the itemstack.
        final ItemStack itemStack = event.getItem();

        // Get the player.
        final Player player = event.getPlayer();

        if (itemStack == null || !itemStack.hasItemMeta()) {
            Messages.no_item_in_hand.sendMessage(player);

            return;
        }

        // Get the item meta.
        final ItemMeta itemMeta = itemStack.getItemMeta();

        if (!(itemMeta instanceof Damageable damageable)) {
            Messages.anvil_repair_not_valid.sendMessage(player);

            return;
        }

        if (!damageable.hasDamage()) {
            Messages.anvil_repair_no_damage.sendMessage(player);

            return;
        }

        // Get the inventory.
        final PlayerInventory inventory = player.getInventory();

        // Check if inventory contains enough.
        boolean hasAmount = false;

        final int cost = ConfigManager.getConfig().getProperty(Config.anvil_repair_cost);

        final Material material = ItemUtil.getMaterial(ConfigManager.getConfig().getProperty(Config.anvil_repair_material));

        // Loop through inventory.
        for (ItemStack item : inventory.getStorageContents()) {
            if (item == null || item.getType() != material) continue;

            hasAmount = cost == -1 || item.getAmount() >= cost;
        }

        if (!hasAmount) {
            Messages.anvil_repair_not_enough.sendMessage(player, new HashMap<>() {{
                put("{amount}", String.valueOf(cost));

                if (material != null) {
                    put("{type}", cost >= 2 ? "<lang:" + material.getItemTranslationKey() + ">s" : "<lang:" + material.getItemTranslationKey() + ">");
                }
            }});

            return;
        }

        Item item;

        try {
            item = player.getWorld().dropItem(block.getLocation().clone().add(.5, 2, .5), itemStack);

            item.setVelocity(new Vector(0, .2, 0));
            item.setCustomNameVisible(false);
            item.setCanPlayerPickup(false);
            item.setCanMobPickup(false);
        } catch (Exception exception) {
            this.plugin.getComponentLogger().warn("Failed to add the display item above the anvil.");

            return;
        }

        // Remove the item from inventory.
        inventory.setItem(inventory.getHeldItemSlot(), null);

        // Remove the gold from the inventory.
        inventory.removeItem(new ItemStack(material, cost));

        final ScheduledTask repairAnimation = new FoliaRunnable(this.plugin.getServer().getRegionScheduler(), block.getLocation()) {
            @Override
            public void run() {
                // Spawn the block particle.
                player.spawnParticle(Particle.BLOCK, block.getLocation(), 5, Material.ANVIL.createBlockData());

                // Play the sound.
                player.playSound(block.getLocation(), Sound.BLOCK_ANVIL_USE, 1f, 1f);
            }
        }.runAtFixedRate(this.plugin, 0, 20);

        new FoliaRunnable(this.plugin.getServer().getRegionScheduler(), block.getLocation()) {
            @Override
            public void run() {
                // Check if cancelled.
                if (!repairAnimation.isCancelled()) {
                    // Cancel the repair animation task.
                    repairAnimation.cancel();
                }

                final ItemStack itemStack = item.getItemStack();

                itemStack.editMeta(itemMeta -> {
                    if (itemMeta instanceof Damageable damageable) {
                        damageable.setDamage(0);
                    }
                });

                // Add the repaired item.
                inventory.addItem(itemStack);

                // Remove the display item.
                item.remove();
            }
        }.runDelayed(this.plugin, 80);
    }
}