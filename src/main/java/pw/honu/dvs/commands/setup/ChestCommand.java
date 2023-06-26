package pw.honu.dvs.commands.setup;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.commands.Commands;
import pw.honu.dvs.managers.MatchManager;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

@CommandInfo(
        name = "chest",
        usage = "/dvs chest set|test",
        permission = "set or test the inventory chest",
        desc = "Set the inventory chest",
        pattern = "chest"
)
public class ChestCommand  implements Command {

    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        if (args.length == 0) {
            return false;
        }

        if (!Commands.isPlayer(sender)) {
            sender.sendMessage("Must be a player");
            return true;
        }

        if (match.getMatchState() != MatchState.GATHERING) {
            sender.sendMessage("Match state is " + match.getMatchState() + ", not " + MatchState.GATHERING);
            return true;
        }

        Player p = Commands.unwrap(sender);

        if (args[0].equals("set")) {
            Block b =  p.getTargetBlock(10);
            if (b == null) {
                sender.sendMessage("Not looking at any block");
                return true;
            }

            if (b.getType() != Material.CHEST) {
                sender.sendMessage("Need to be looking at a chest, this is at " + b.getType());
                return true;
            }

            if (!(b.getState() instanceof Chest)) {
                sender.sendMessage("Was looking at a chest material but isn't an instance of a Chest?");
                return true;
            }

            Chest chest = (Chest) b.getState();
            match.setPlayerChest(chest);
            sender.sendMessage("Chest set to " + chest.getLocation());
        } else if (args[0].equals("test")) {
            Chest chest = match.getPlayerChest();
            if (chest == null) {
                sender.sendMessage("Chest is null, cannot test inv copy");
                return true;
            }

            @Nullable ItemStack[] contents = chest.getBlockInventory().getContents();
            if (contents != null){
                p.getInventory().setContents(contents);
            }
        } else {
            return false;
        }

        return true;
    }

    @Override
    public List<String> tab(MatchManager match, Player player, String... args) {
        return Arrays.asList("set", "test");
    }

}
