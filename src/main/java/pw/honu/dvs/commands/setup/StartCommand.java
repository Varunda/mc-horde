package pw.honu.dvs.commands.setup;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.managers.MapManager;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.map.HordeMap;

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
        if (match.getMatchState() != MatchState.PRE_GAME) {
            sender.sendMessage(ChatColor.YELLOW + "Match must be in the GATHERING mode");
            return true;
        }

        HordeMap nextMap = MapManager.instance.getNextMap();
        if (null == nextMap) {
            sender.sendMessage("Map is not set. Use /dvs map set <map>");
            return true;
        }

        DvSLogger.info("Starting match...");
        if (!MatchManager.instance.startMatch()) {
            sender.sendMessage(ChatColor.YELLOW + "Failed to start the match. See console");
        }

        return true;
    }

}
