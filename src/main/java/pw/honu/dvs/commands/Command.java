package pw.honu.dvs.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.honu.dvs.managers.MatchManager;

import java.util.Collections;
import java.util.List;

public interface Command {

    boolean run(MatchManager match, CommandSender sender, String... args);

    default List<String> tab(MatchManager match, Player player, String... args) {
        return Collections.emptyList();
    }

}
