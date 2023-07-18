package pw.honu.dvs.commands.setup;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.commands.Commands;
import pw.honu.dvs.managers.LocationManager;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.MonsterManager;
import pw.honu.dvs.managers.WaveManager;
import pw.honu.dvs.monster.MonsterTemplate;
import pw.honu.dvs.wave.WaveEntry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@CommandInfo(
        name = "wave",
        pattern = "wave",
        desc = "Wave setup",
        permission = "",
        usage = "spawn | print | clear | set <monster name> <count>"
)
public class WaveCommand implements Command {

    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        if (args.length < 1) {
            return false;
        }

        if (!Commands.isPlayer(sender)) {
            sender.sendMessage("Must be a player");
            return false;
        }

        String sub = args[0];

        switch (sub) {
            case "repeat": {

                if (args.length != 2) {
                    return false;
                }

                int count;
                try {
                    count = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Failed to parse " + args[1] + " to an int");
                    return false;
                }

                WaveManager.instance.repeatInSeconds = count;
                count = Math.max(0, count);

                if (count > 0) {
                    sender.sendMessage("Wave will be sent every " + count + " seconds");
                } else {
                    sender.sendMessage("Repeat wave disabled");
                }

                break;
            }

            case "add":
            case "set":
                if (args.length != 3) {
                    return false;
                }

                int count;
                try {
                    count = Integer.parseInt(args[2]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Failed to parse " + args[2] + " to an int");
                    return false;
                }

                String templateName = args[1].toLowerCase();
                @Nullable MonsterTemplate template = MonsterManager.instance.get(templateName);
                if (template == null) {
                    sender.sendMessage(ChatColor.RED + "Monster template " + templateName + " does not exist");
                    return false;
                }

                WaveEntry entry = new WaveEntry(count, template);
                WaveManager.instance.addToCurrent(entry);
                sender.sendMessage("Added " + ChatColor.AQUA + count + " " + ChatColor.YELLOW + templateName + ChatColor.RESET + " to the current wave");
                sender.sendMessage("Current wave has:");

                for (WaveEntry e : WaveManager.instance.current.getEntries()) {
                    sender.sendMessage("    " + e.getTemplate().getName() + ": " + e.getCount());
                }

                break;

            case "clear":
                int amount = WaveManager.instance.current.getEntries().size();
                sender.sendMessage("cleared " + amount + " mobs from current wave");
                WaveManager.instance.clearCurrent();
                break;

            case "spawn":
            case "send":
                if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
                    sender.sendMessage(ChatColor.YELLOW + "Match has not started");
                    return true;
                }
                if (LocationManager.instance.getMonsterSpawn() == null) {
                    sender.sendMessage(ChatColor.YELLOW + "Missing monster spawn. Use /dvs setspawn monster");
                    return true;
                }

                boolean spawned = MatchManager.instance.spawnWave(WaveManager.instance.current);
                if (!spawned) {
                    sender.sendMessage(ChatColor.RED + "Failed to spawn wave. See console for more info");
                }
                break;

            case "print":
                sender.sendMessage("Current wave has:");
                for (WaveEntry e : WaveManager.instance.current.getEntries()) {
                    sender.sendMessage("    " + e.getTemplate().getName() + ": " + e.getCount());
                }
                break;


            default:
                return false;
        }

        return true;
    }

    @Override
    public List<String> tab(MatchManager match, Player player, String... args) {
        if (args.length == 0 || args[0].startsWith(" ") || args[0].length() == 0) {
            return Arrays.asList("set", "clear", "spawn", "print");
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "print":
            case "clear":
            case "spawn":
                return Collections.emptyList();

            case "add":
            case "set":
                if (args.length >= 3) {
                    return null;
                }

                if (args.length == 2) {
                    return MonsterManager.instance.getMatchingTemplateNames(args[1]);
                }

                return MonsterManager.instance.getNames();
        }

        return null;
    }

}
