package net.yukkuricraft.remisux;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class RemiTabCompleter implements TabCompleter
{
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (commandLabel.equalsIgnoreCase("yeet")) {
            return yeetTabComplete(sender, cmd, commandLabel, args);
        } else {
            return Arrays.asList("Remi", "Is", "Cutest", "2hu");
        }
    }
    private List<String> yeetTabComplete(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (args.length < 2) {
            Collection<? extends Player> players = (args.length == 0
                                                      ? Bukkit.getOnlinePlayers()
                                                      : Bukkit.matchPlayer(args[0]));
            return players
                    .stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }

        List<String> argsList = Arrays.asList(args);
        List<String> options = Arrays.asList("-v", "-l", "-f");
        return options
                .stream()
                .filter(opt -> ! argsList.contains(opt))
                .collect(Collectors.toList());
    }
}
