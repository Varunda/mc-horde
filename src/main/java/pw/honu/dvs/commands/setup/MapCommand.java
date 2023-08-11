package pw.honu.dvs.commands.setup;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.honu.dvs.DvS;
import pw.honu.dvs.map.HordeMap;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.managers.MapManager;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.util.WorldUtil;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CommandInfo(
        name = "map",
        pattern = "map",
        desc = "Set the map",
        permission = "",
        usage = "/dvs map"
)
public class MapCommand implements Command {

    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        if (args.length < 1) {
            return false;
        }

        String arg = args[0].toLowerCase();

        switch (arg) {
            case "reload": {
                sender.sendMessage("Reloading maps...");
                DvS.instance.loadMaps();
                sender.sendMessage("Loaded " + MapManager.instance.getMaps().size() + " maps");
                break;
            }

            case "set": {
                if (args.length < 2) {
                    sender.sendMessage("Missing map name");
                    return false;
                }

                String inputMap = args[1].toLowerCase();
                HordeMap map = MapManager.instance.getMap(inputMap);
                if (null == map) {
                    sender.sendMessage("Invalid map name '" + inputMap + "'");
                    return true;
                }

                MapManager.instance.setNextMap(map);
                sender.sendMessage("This game will use map " + inputMap);

                break;
            }

            case "list": {
                List<HordeMap> maps = MapManager.instance.getMaps();
                sender.sendMessage(maps.size() + " maps available");

                for (HordeMap map : maps) {
                    sender.sendMessage(" - " + map.getName() + " by " + map.getAuthor());
                }

                break;
            }

            case "explore": {
                if (args.length < 2) {
                    sender.sendMessage("Missing map name");
                    return false;
                }

                String inputMap = args[1].toLowerCase();
                HordeMap map = MapManager.instance.getMap(inputMap);
                if (null == map) {
                    sender.sendMessage("Invalid map name '" + inputMap + "'");
                    return true;
                }

                String matchId = UUID.randomUUID() + "-" + map.getName();

                World matchWorld = WorldUtil.cloneWorld(map.getFolder(), matchId);
                break;
            }

            default:
                sender.sendMessage("Unknown sub-command '" + arg + "'");
                return false;
        }

        return true;
    }

    @Override
    public List<String> tab(MatchManager match, Player player, String... args) {
        if (args.length < 1 || args[0].length() == 0)  {
            return Arrays.asList("set", "list", "reload", "explore");
        }

        String arg = args[0].toLowerCase();

        if ("list".equalsIgnoreCase(arg)) {
            return null;
        }

        if ("set".equalsIgnoreCase(arg) || "explore".equalsIgnoreCase(arg)) {
            return MapManager.instance.getMaps().stream().map(HordeMap::getName).collect(Collectors.toList());
        }

        return null;
    }

}
