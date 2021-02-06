package net.yukkuricraft.remisux;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

import net.yukkuricraft.remisux.RemiCommands;
import net.yukkuricraft.remisux.RemiTabCompleter;

public class RemiSux extends JavaPlugin
{
    private final RemiCommands RemiCommands = new RemiCommands();

    @Override
    public void onEnable() {
        getLogger().info("onEnable has been invoked!");
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable has been invoked!");
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return RemiCommands.command(sender, cmd, label, args);
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        return RemiTabCompleter.tabComplete(sender, cmd, commandLabel, args);
    }
}
