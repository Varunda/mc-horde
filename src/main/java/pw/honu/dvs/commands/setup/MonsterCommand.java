package pw.honu.dvs.commands.setup;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.MonsterManager;

import java.util.List;

@CommandInfo(
        name = "monsters",
        usage = "monsters",
        permission = "",
        desc = "List all monsters",
        pattern = "monsters"
)
public class MonsterCommand implements Command {

    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        List<String> templateNames = MonsterManager.instance.getNames();

        sender.sendMessage(ChatColor.AQUA + "Have " + templateNames.size() + " templates");
        for (String s : templateNames) {
            sender.sendMessage("    " + s);
        }

        return true;
    }
}
