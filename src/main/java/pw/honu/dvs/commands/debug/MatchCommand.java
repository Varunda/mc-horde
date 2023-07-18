package pw.honu.dvs.commands.debug;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.commands.Commands;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.MonsterManager;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@CommandInfo(
        name = "match",
        usage = "/dvs match killmobs|pathfind|state|end|next",
        desc = "Match control command",
        permission = "",
        pattern = "match"
)
public class MatchCommand implements Command {

    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        if (args.length == 0) {
            return false;
        }

        if (!Commands.isPlayer(sender)) {
            sender.sendMessage("must be a player");
            return true;
        }

        Player p = Commands.unwrap(sender);

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "pathfind": {
                sender.sendMessage("Pathfindign all alive monsters to your location");
                for (LivingEntity le : MonsterManager.instance.getAliveMonsters()) {
                    if (le instanceof Mob) {
                        Mob m = (Mob) le;

                        m.getPathfinder().moveTo(p.getLocation());
                    }
                }
                break;
            }

            case "killmobs": {
                sender.sendMessage("Killing all mobs");
                MonsterManager.instance.killAllMonsters();
                break;
            }

            case "state":
            case "status": {
                if (args.length >= 3) {
                    try {
                        MatchState state = MatchState.valueOf(args[2]);
                        MatchManager.instance.setMatchState(state);
                        sender.sendMessage("State set to " + state);
                    } catch (IllegalArgumentException ex) {
                        sender.sendMessage("Invalid state " + args[2]);
                        return true;
                    }
                } else {
                    MatchState state = MatchManager.instance.getMatchState();
                    sender.sendMessage("Match state: " + state);
                }

                break;
            }

            case "end": {
                MatchManager.instance.endMatch();
                break;
            }

            case "next": {
                MatchState state = MatchManager.instance.getMatchState();

                if (state == MatchState.GATHERING) {
                    MatchManager.instance.startCombatPhase();
                } else if (state == MatchState.RUNNING) {
                    MatchManager.instance.endMatch();
                } else if (state == MatchState.POST_GAME) {
                    MatchManager.instance.resetMatch();
                } else {
                    sender.sendMessage("Cannot advance match to next state, not in GATHERING");
                }

                break;
            }

            default:
                return false;
        }

        return true;
    }

    @Override
    public List<String> tab(MatchManager match, Player player, String... args) {
        return Arrays.asList("pathfind", "killmobs", "state", "end", "next");
    }
}
