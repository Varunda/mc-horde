package pw.honu.dvs.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

public class BlockMeltListener implements Listener {

    @EventHandler
    public void blockMeltEvent(BlockFadeEvent ev) {
        if (ev.getBlock().getType() == Material.ICE) {
            ev.setCancelled(true);
        }

        if (ev.getNewState().getType() == Material.WATER) {
            ev.setCancelled(true);
        }
    }

}
