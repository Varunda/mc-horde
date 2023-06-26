package pw.honu.dvs.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.inventory.ItemStack;
import pw.honu.dvs.DvS;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.managers.MatchManager;

import java.util.Random;

public class BlockBreakListener implements Listener {

    private static Random _random = new Random();

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.GATHERING) {
            return;
        }

        Chest c = MatchManager.instance.getPlayerChest();
        if (c != null && c.getLocation().equals(event.getBlock().getLocation())) {
            Player p = event.getPlayer();
            p.sendMessage("Don't break the inventory chest >:(");
            event.setCancelled(true);
        }
    }

    // This doesn't work idk why lmao :dab:
    @EventHandler
    public void blockExplode(BlockExplodeEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.GATHERING) {
            return;
        }

        Chest c = MatchManager.instance.getPlayerChest();
        if (c == null) {
            return;
        }

        if (c.getLocation().equals(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

}

