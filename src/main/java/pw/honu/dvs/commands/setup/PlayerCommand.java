package pw.honu.dvs.commands.setup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.honu.dvs.DvS;
import pw.honu.dvs.PlayerState;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.commands.Commands;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.PlayerManager;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommandInfo(
        name = "player",
        usage = "/dvs player respawn|get|set <state>",
        pattern = "player",
        desc = "",
        permission = ""
)
public class PlayerCommand implements Command {

    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        if (args.length == 0) {
            return false;
        }

        String which = args[0];

        switch (which) {
            case "respawn": {
                if (args.length != 2) {
                    return false;
                }

                String playerName = args[1].toLowerCase();
                @Nullable Player p = Commands.getPlayer(playerName);

                if (p == null) {
                    sender.sendMessage(ChatColor.YELLOW + "Failed to find player '" + playerName + "'");
                    return true;
                }

                p.sendMessage(ChatColor.DARK_PURPLE + "You were respawned to the monster lobby");
                sender.sendMessage(ChatColor.GREEN + "Respawned " + playerName + " to the monster lobby");

                PlayerManager.instance.respawnToMonsterLobby(p.getUniqueId());
                break;
            }

            case "set": {
                if (args.length != 3) {
                    return false;
                }

                String stateName = args[2].toLowerCase();

                PlayerState state = null;
                for (PlayerState s : PlayerState.values()) {
                    if (s.name().toLowerCase().equals(stateName)) {
                        state = s;
                        break;
                    }
                }

                if (state == null) {
                    sender.sendMessage(ChatColor.YELLOW + "Failed to find PlayerState with name of '" + stateName + "'");
                    return true;
                }

                String playerName = args[1].toLowerCase();
                Player p = Commands.getPlayer(playerName);

                if (p == null) {
                    sender.sendMessage(ChatColor.YELLOW + "Failed to find player with name of '" + playerName + "'");
                    return true;
                }

                PlayerManager.instance.setPlayer(p.getUniqueId(), state);
                sender.sendMessage("Set state of " + p.getName() + " to " + state);
                break;
            }

            case "get": {
                if (args.length != 2) {
                    return false;
                }

                String playerName = args[1].toLowerCase();
                Player p = Commands.getPlayer(playerName);

                if (p == null) {
                    sender.sendMessage(ChatColor.YELLOW + "Failed to find player with name of '" + playerName + "'");
                    return true;
                }

                PlayerState state = PlayerManager.instance.getPlayer(p.getUniqueId());
                sender.sendMessage("Player " + p.getName() + " has state of " + state);
                break;
            }

            default: {
                return false;
            }

        }

        return true;
    }

    @Override
    public List<String> tab(MatchManager match, Player player, String... args) {
        if (args.length == 0 || args[0].length() == 0) {
            return Arrays.asList("respawn", "set", "get");
        }

        String which = args[0].toLowerCase();

        switch (which) {
            case "get":
            case "respawn": {
                String who = "";
                if (args.length > 1) {
                    who = args[1].toLowerCase();
                }

                String finalWho = who;
                return Commands.getPlayerNames(finalWho);
            }

            case "set": {
                // Player is set, what state do we want?
                if (args.length > 2) {
                    return Arrays.stream(PlayerState.values())
                            .filter(iter -> iter.name().toLowerCase().startsWith(args[2].toLowerCase()))
                            .map(Enum::name)
                            .toList();
                }

                String who = "";
                if (args.length > 1) {
                    who = args[1].toLowerCase();
                }

                String finalWho = who;
                return Commands.getPlayerNames(finalWho);
            }
        }

        return null;
    }
}
