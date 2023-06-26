package pw.honu.dvs.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.PlayerState;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.PlayerManager;

public class HungerListener implements Listener {

    @EventHandler
    public void hungerEvent(FoodLevelChangeEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player p = (Player) event.getEntity();
        if (PlayerManager.instance.getPlayer(p.getUniqueId()) == PlayerState.MONSTER) {
            event.setCancelled(true);
        }
    }

}
