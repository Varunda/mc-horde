package pw.honu.dvs.commands.debug;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.honu.dvs.ability.Ability;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.commands.Commands;
import pw.honu.dvs.item.AbilityItem;
import pw.honu.dvs.managers.AbilityManager;
import pw.honu.dvs.managers.MatchManager;

import java.util.List;

@CommandInfo(
        name = "item",
        usage = "/dvs item <name>",
        desc = "Bind an ability to an item",
        pattern = "item",
        permission = ""
)
public class ItemCommand implements Command {

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

        Player p = Commands.unwrap(sender);
        if (p.getEquipment().getItemInMainHand().getType() == Material.AIR) {
            sender.sendMessage("You are not holding an item");
            return true;
        }

        AbilityItem.attachAbilityData(ability, p.getEquipment().getItemInMainHand(), false);

        return true;
    }

    @Override
    public List<String> tab(MatchManager match, Player player, String... args) {
        return AbilityManager.instance.getNames();
    }

}
