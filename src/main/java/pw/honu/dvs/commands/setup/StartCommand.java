package pw.honu.dvs.commands.setup;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.managers.MatchManager;

@CommandInfo(
        name = "start",
        pattern = "start",
        desc = "Start a DvS match",
        permission = "",
        usage = "/dvs start"
)
public class StartCommand implements Command {

    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        if (match.getMatchState() != MatchState.GATHERING) {
            sender.sendMessage(ChatColor.YELLOW + "Match must be in the GATHERING mode");
            return true;
        }

        if (!MatchManager.instance.startMatch()) {
            sender.sendMessage(ChatColor.YELLOW + "Failed to start the match. See console");
        }

        return true;
    }

}
