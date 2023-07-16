package pw.honu.dvs.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.managers.MatchManager;

public class PlayerToggleSneakListener implements Listener {

    @EventHandler
    public void playerSneak(PlayerToggleSneakEvent ev) {
        if (MatchManager.instance.getMatchState() != MatchState.GATHERING) {
            return;
        }

        if (!ev.isSneaking()) {
            return;
        }

        Location playerLoc = ev.getPlayer().getLocation();
        playerLoc.setY(playerLoc.getY());

        Block b = playerLoc.getWorld().getBlockAt(playerLoc);

        if (b.getType() == Material.GRASS) {
            return;
        }

        if (b.getType() == Material.FARMLAND) {
            b = b.getRelative(BlockFace.UP);
        }

        b.applyBoneMeal(BlockFace.NORTH);
    }

}
