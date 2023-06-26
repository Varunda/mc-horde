package pw.honu.dvs.commands.setup;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
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

import java.util.List;

@CommandInfo(
        name = "spawn",
        usage = "/dvs spawn <template> <count>",
        permission = "",
        pattern = "spawn",
        desc = "spawn a wave"
)
public class SpawnCommand implements Command {

    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        if (!Commands.isPlayer(sender)){
            return true;
        }

        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            sender.sendMessage("Match must be running");
            return true;
        }

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

        String templateName = args[0].toLowerCase();
        @Nullable MonsterTemplate template = MonsterManager.instance.get(templateName);
        if (template == null) {
            sender.sendMessage(ChatColor.RED + "Monster template " + templateName + " does not exist");
            return false;
        }

        Player p = Commands.unwrap(sender);

        Location loc = p.getLocation();
        if (LocationManager.instance.getMonsterSpawn() != null) {
            loc = LocationManager.instance.getMonsterSpawn();
        }

        for (int i = 0; i < count; ++i){
            MonsterManager.instance.spawn(template, loc);
        }

        return true;
    }

    @Override
    public List<String> tab(MatchManager match, Player player, String... args) {
        if (args.length == 0) {
            return null;
        }
        return MonsterManager.instance.getMatchingTemplateNames(args[0]);
    }
}

