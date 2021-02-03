package net.yukkuricraft.remisux;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;

import java.util.Arrays;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;


public class RemiSux extends JavaPlugin
{
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
        if (cmd.getName().equalsIgnoreCase("pong")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
            String msg = String.format("%sPong! I hear %s%s", ChatColor.YELLOW, sender.getName(), getRandomInsult());
            broadcast(msg);
            return true;
        } else if (cmd.getName().equalsIgnoreCase("yeet")) {
            yeet(sender, args);
            return true;
        }

        return false;
    }

    private void broadcast(String msg) {
        TextChannel textChannel = DiscordSRV.getPlugin().getMainTextChannel();

        Bukkit.broadcastMessage(msg);
        DiscordUtil.sendMessage(textChannel, msg);
    }

    private static final String[] PONG_INSULTS = {
        " likes little boys",
        "'s face makes onions cry",
        " is so dumb even Cirno would be amazed",
        " sold their soul to Remi",
        " still can't beat any 2hu on easy mode"
    };

    private String getRandomInsult() {
        return PONG_INSULTS[(int)(System.currentTimeMillis() % PONG_INSULTS.length)];
    }

    private static final int RAND_VECTOR_CARDINALITY = 3;
    private Vector getSemiRandomizedYeetVector() {

        long x = ThreadLocalRandom.current().nextLong(-1 * RAND_VECTOR_CARDINALITY, RAND_VECTOR_CARDINALITY);
        long y = ThreadLocalRandom.current().nextLong(0, 2 * RAND_VECTOR_CARDINALITY);
        long z = ThreadLocalRandom.current().nextLong(-1 * RAND_VECTOR_CARDINALITY, RAND_VECTOR_CARDINALITY);

        return new Vector(x, y, z);
    }

    private void yeet(CommandSender sender, String[] args) {
        if ( ! sender.hasPermission("remisux.yeet")) {
            sender.sendMessage(String.format("%sYou can't yeet people bish", ChatColor.RED));
            return;
        } else if (args.length == 0) {
            sender.sendMessage(String.format("%s/yeet <name> [-v|-l|-f]. -v(erbose), -l(ightning), -f(orce)", ChatColor.RED));
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);

        boolean isForced = (args.length > 1) ? hasArg(args,"-f") : false;
        boolean isVerbose = (args.length > 1) ? hasArg(args, "-v") : false;
        boolean useLightning = (args.length > 1) ? hasArg(args, "-l") : false;

        if (player == null) {
            sender.sendMessage(String.format("%sThe user %s is not online.", ChatColor.RED, args[0]));
            return;
        } else if ( ! isForced && player.getGameMode() == GameMode.SURVIVAL) {
            String msg = ChatColor.RED+"Please don't yeet people in survival.";
            if (sender.hasPermission("remisux.yeet.bypass")) {
                msg += " Use `/yeet <name> -f` to bypass this.";
            }
            sender.sendMessage(msg);
            return;
        } else if ( isForced && ! sender.hasPermission("remisux.yeet.bypass")) {
            sender.sendMessage(String.format("%sYou don't have permission to use the bypass flag!", ChatColor.RED));
            return;
        } else if (player.getHealth() == 0) {
            sender.sendMessage(String.format("%sStop. Stop! He's already dead!", ChatColor.RED));
            return;
        }

        player.sendMessage(String.format("%syaYEET!", ChatColor.YELLOW));
        player.setVelocity(getSemiRandomizedYeetVector());

        if (useLightning) {
            player.getWorld().strikeLightningEffect(player.getLocation());
        }

        if (isVerbose) {
            broadcast(String.format("%s%s has been YOTE by %s", ChatColor.YELLOW, player.getDisplayName(), sender.getName()));
        }
    }

    private boolean hasArg(String[] args, String targetArg) {
        // Yuck.
        return Arrays
                .asList(args)
                .stream()
                .map(v -> v.toLowerCase())
                .anyMatch(v -> v.equalsIgnoreCase(targetArg));
    }
}
