package pw.honu.dvs.commands.debug;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.honu.dvs.ability.Ability;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.commands.Commands;
import pw.honu.dvs.managers.AbilityManager;
import pw.honu.dvs.managers.MatchManager;

import java.util.List;

@CommandInfo(
        name = "ability",
        usage = "/dvs ability <name>",
        desc = "Cast an ability",
        pattern = "ability",
        permission = ""
)
public class AbilityCommand implements Command {

    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        if (!Commands.isPlayer(sender)) {
            sender.sendMessage("Must be a player");
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        Ability ability = AbilityManager.instance.get(args[0].toLowerCase());
        if (ability == null) {
            sender.sendMessage(ChatColor.YELLOW + "No ability named '" + args[0] + "' exists");
            return true;
        }

        ability.execute(Commands.unwrap(sender));
        sender.sendMessage(ChatColor.GREEN + "Used ability '" + args[0] + "'");

        return true;
    }


    @Override
    public List<String> tab(MatchManager match, Player player, String... args) {
        return AbilityManager.instance.getNames();
    }
}
