package pw.honu.dvs.commands.setup;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.honu.dvs.managers.LocationManager;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.commands.Commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@CommandInfo(
        name = "loc",
        pattern = "loc",
        usage = "/dvs loc goto|json",
        desc = "",
        permission = ""
)
public class LocCommand implements Command {

    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        if (!Commands.isPlayer(sender)) {
            sender.sendMessage(ChatColor.YELLOW + "Must be a player");
            return true;
        }

        String action = args[0].toLowerCase();

        Player p = Commands.unwrap(sender);
        Location loc = p.getLocation();

        if (action.equals("goto")) {
            if (args.length < 2) {
                return false;
            }

            String which = args[1].toLowerCase();

            Location gotoLoc = null;

            switch (which) {
                case "player": gotoLoc = MatchManager.instance.getRunningMap().getPlayerStart(); break;
                case "monsterlobby": gotoLoc = MatchManager.instance.getRunningMap().getMonsterLobby(); break;
                case "monstertarget": gotoLoc = MatchManager.instance.getRunningMap().getMonsterTarget(); break;
                default: return false;
            }

            if (gotoLoc != null) {
                p.sendMessage("Teleporting to " + which);
                p.teleport(gotoLoc);
                return true;
            } else {
                p.sendMessage(ChatColor.RED + which + " is not set!");
            }

            return false;
        }

        if ("json".equals(action)) {
            Location l = p.getLocation();
            p.sendMessage(String.format("{ \"x\": %d, \"y\": %d, \"z\": %d } ", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        }

        return true;
    }

    @Override
    public List<String> tab(MatchManager match, Player player, String... args) {
        if (args.length <= 1) {
            return List.of("goto", "json");
        }

        return Arrays.asList("player", "monsterlobby", "monsterspawn", "monstertarget", "all");
    }

}
