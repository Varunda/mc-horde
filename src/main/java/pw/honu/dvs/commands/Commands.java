package pw.honu.dvs.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Commands {

    public static boolean isPlayer(CommandSender sender) {
        return (sender instanceof Player);
    }

    public static Player unwrap(CommandSender sender) {
        return (Player)sender;
    }

    public static String[] skipFirst(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }

    public static @Nullable Player getPlayer(String name) {
        return Bukkit.getOnlinePlayers().stream()
            .filter(iter -> iter.getName().toLowerCase().equalsIgnoreCase(name))
            .collect(Collectors.toList()).get(0);
    }

    public static List<String> getPlayerNames(final String start) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(iter -> iter.getName().toLowerCase().startsWith(start))
                .map(Player::getName).toList();
    }

}
