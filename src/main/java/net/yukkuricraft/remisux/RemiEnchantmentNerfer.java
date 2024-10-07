package net.yukkuricraft.remisux;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RemiEnchantmentNerfer implements Listener {
    private final String nerfBypassPermission = "remisux.enchantmentnerfbypass";
    private final String absurdEnchantWarning = ChatColor.YELLOW + "Found absurdly high enchant levels; we reduced it to 10 :)";
    private final RemiSux plugin;
    private final Logger logger;

    public RemiEnchantmentNerfer(RemiSux plugin) {
        this.plugin = plugin;
        this.logger = this.plugin.getLogger();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();

        if (entity.hasPermission(nerfBypassPermission))
            return;

        if (this.scanAndFixItem(event.getCurrentItem())) {
            if (entity instanceof Player) {
                entity.sendMessage(absurdEnchantWarning);
            } else {
                logger.warning("Weird inventory event from " + entity.getName());
            }
        }
    }

    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent event) {
        if (event.getPlayer().hasPermission(nerfBypassPermission))
            return;

        Item item = event.getItemDrop();
        ItemStack stack = item.getItemStack();
        if (this.scanAndFixItem(stack)) {
            item.setItemStack(stack);
            event.getPlayer().sendMessage(absurdEnchantWarning);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().hasPermission(nerfBypassPermission))
            return;

        Entity src = event.getDamager();
        if (src instanceof HumanEntity) {
            PlayerInventory inventory = ((HumanEntity)src).getInventory();
            ItemStack[] items = { inventory.getItemInMainHand(), inventory.getItemInOffHand() };
            for (ItemStack item : items) {
                if (this.scanAndFixItem(item)) {
                    event.setCancelled(true);

                    src.sendMessage(absurdEnchantWarning);
                    src.sendMessage(ChatColor.YELLOW + "(We also stopped your attack)");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission(nerfBypassPermission))
            return;

        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        if (this.scanAndFixItem(item)) {
            player.sendMessage(absurdEnchantWarning);
        }
    }

    public boolean scanAndFixItem(ItemStack item) {
        if (item == null) {
            return false;
        } else {
            Map<Enchantment, Integer> enchantments = new HashMap<>(item.getEnchantments());
            boolean needsUpdate = false;

            for (Enchantment enchant : enchantments.keySet()) {
                if (enchantments.get(enchant) > 10) {
                    enchantments.put(enchant, 10);
                    needsUpdate = true;
                }
            }

            if (needsUpdate) {
                enchantments.keySet().forEach(item::removeEnchantment);
                enchantments.forEach(item::addUnsafeEnchantment);
            }

            return needsUpdate;
        }
    }

}
