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
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RemiCommands implements CommandExecutor
{
    private final RemiSux plugin;
    private final Boolean discordSrvInstalled;
    private final TextChannel discordSrvTextChannel;

    public RemiCommands(RemiSux remiSux) {
        this.plugin = remiSux;
        this.discordSrvInstalled = Bukkit.getPluginManager().getPlugin("DiscordSRV") != null;
        if (this.discordSrvInstalled) {
            this.discordSrvTextChannel = DiscordSRV.getPlugin().getMainTextChannel();
        } else {
            this.discordSrvTextChannel = null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pong")) {
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
        Bukkit.broadcastMessage(msg);

        if(this.discordSrvInstalled) {
            DiscordUtil.sendMessage(this.discordSrvTextChannel, msg);
        }
    }

    private static final String[] PONG_INSULTS = {
        " eats their boogers",
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

        boolean isForced = args.length > 1 && hasArg(args, "-f");
        boolean isVerbose = args.length > 1 && hasArg(args, "-v");
        boolean useLightning = args.length > 1 && hasArg(args, "-l");

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

        // Yeet
        player.sendMessage(String.format("%syaYEET!", ChatColor.YELLOW));
        player.setVelocity(getSemiRandomizedYeetVector());

        if (useLightning) {
            player.getWorld().strikeLightningEffect(player.getLocation());
        }

        if (isVerbose) {
            broadcast(String.format("%s%s just YOTE %s!", ChatColor.YELLOW, sender.getName(), player.getDisplayName()));
        }
    }

    private boolean hasArg(String[] args, String targetArg) {
        // Yuck.
        return Arrays.stream(args)
                .anyMatch(v -> v.equalsIgnoreCase(targetArg));
    }
}
