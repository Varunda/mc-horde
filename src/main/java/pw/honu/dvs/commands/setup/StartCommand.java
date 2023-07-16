package pw.honu.dvs.commands.setup;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import pw.honu.dvs.DvS;
import pw.honu.dvs.HordeMap;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.managers.LocationManager;
import pw.honu.dvs.managers.MapManager;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.util.LocationUtil;
import pw.honu.dvs.util.WorldUtil;

import java.util.UUID;

@CommandInfo(
        name = "start",
        pattern = "start",
        desc = "Start a DvS match",
        permission = "",
        usage = "/dvs start"
)
public class StartCommand implements Command {

    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        if (match.getMatchState() != MatchState.PRE_GAME) {
            sender.sendMessage(ChatColor.YELLOW + "Match must be in the GATHERING mode");
            return true;
        }

        HordeMap nextMap = MapManager.instance.getNextMap();
        if (null == nextMap) {
            sender.sendMessage("Map is not set. Use /dvs map set <map>");
            return true;
        }

        String matchId = UUID.randomUUID() + "-" + nextMap.getName();

        World matchWorld = WorldUtil.cloneWorld(nextMap.getFolder(), matchId);

        LocationManager.instance.setMonsterTarget(LocationUtil.create(matchWorld, nextMap.getMonsterTarget()));
        LocationManager.instance.setMonsterSpawn(LocationUtil.create(matchWorld, nextMap.getMonsterSpawn()));
        LocationManager.instance.setMonsterLobby(LocationUtil.create(matchWorld, nextMap.getMonsterLobby()));
        LocationManager.instance.setPlayerStart(LocationUtil.create(matchWorld, nextMap.getPlayerStart()));

        Location chestLoc = LocationUtil.create(matchWorld, nextMap.getChest());
        if (matchWorld.getBlockAt(chestLoc).getType() != Material.CHEST) {
            DvS.instance.getLogger().severe("MISSING CHEST AT " + chestLoc.toString() + " from map " + nextMap.getName());
            matchWorld.getBlockAt(chestLoc).setType(Material.CHEST);
        }

        Chest chest = (Chest) matchWorld.getBlockAt(chestLoc).getState();

        MatchManager.instance.setPlayerChest(chest);

        if (!MatchManager.instance.startMatch()) {
            sender.sendMessage(ChatColor.YELLOW + "Failed to start the match. See console");
        }

        return true;
    }

}
