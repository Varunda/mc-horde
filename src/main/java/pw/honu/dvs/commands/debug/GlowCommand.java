package pw.honu.dvs.commands.debug;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.managers.MatchManager;

import java.util.List;

public class GlowCommand implements Command {

    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        return false;
    }

    @Override
    public List<String> tab(MatchManager match, Player player, String... args) {
        return null;
    }

}
