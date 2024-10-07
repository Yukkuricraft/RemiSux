package net.yukkuricraft.remisux;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.yukkuricraft.remisux.RemiCommands;
import net.yukkuricraft.remisux.RemiTabCompleter;

public class RemiSux extends JavaPlugin implements Listener
{
    private RemiCommands remiCommands;

    @Override
    public void onEnable() {
        getLogger().info("onEnable has been invoked!");
        this.remiCommands = new RemiCommands(this);

        this.getCommand("yeet").setExecutor(this.remiCommands);
        this.getCommand("pong").setExecutor(this.remiCommands);
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable has been invoked!");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

    }

    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent event) {

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {

    }

}
