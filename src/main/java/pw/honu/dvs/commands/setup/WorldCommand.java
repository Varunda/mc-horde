package pw.honu.dvs.commands.setup;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.commands.Commands;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.util.WorldUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommandInfo(
        name = "world",
        pattern = "world",
        desc = "Control worlds",
        permission = "",
        usage = "/dvs world"
)
public class WorldCommand implements Command {

    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        if (args.length < 1) {
            return false;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "clone":
            case "copy": {
                if (args.length < 2) {
                    return false;
                }

                String inputWorldName = args[1];
                World inputWorld = Bukkit.getWorld(inputWorldName);
                if (inputWorld == null) {
                    sender.sendMessage("Failed to find world named '" + inputWorldName + "'");
                    return true;
                }

                World newWorld = WorldUtil.cloneWorld(inputWorld, inputWorld.getName() + ".copy");
                sender.sendMessage("Successfully created " + newWorld.getName());
                break;
            }

            case "tp":
            case "tele":
            case "teleport": {
                if (!Commands.isPlayer(sender)) {
                    sender.sendMessage("Must be a player");
                    return false;
                }

                if (args.length < 2) {
                    return false;
                }

                Player p = Commands.unwrap(sender);
                GameMode previousGameMode = p.getGameMode(); // preserve gamemode

                String inputWorldName = args[1];
                World inputWorld = Bukkit.getWorld(inputWorldName);
                if (inputWorld == null) {
                    sender.sendMessage("Failed to find world named '" + inputWorldName + "'");
                    return true;
                }

                Location spawn = inputWorld.getSpawnLocation();
                p.teleport(spawn);
                p.setGameMode(previousGameMode);
                break;
            }

            case "ls":
            case "list": {
                List<World> worldNames = Bukkit.getWorlds();

                for (World w : worldNames) {
                    sender.sendMessage(w.getName() + " " + w.getEnvironment());
                }
            }
        }

        return true;
    }

    @Override
    public List<String> tab(MatchManager match, Player player, String... args) {
        if (args.length < 1 || args[0].length() == 0)  {
            return Arrays.asList("copy", "tp", "list");
        }

        String arg = args[0].toLowerCase();

        if ("list".equalsIgnoreCase(arg)) {
            return null;
        }

        List<String> worldNames = Bukkit.getWorlds().stream().map(WorldInfo::getName).collect(Collectors.toList());
        return worldNames;
    }
}
