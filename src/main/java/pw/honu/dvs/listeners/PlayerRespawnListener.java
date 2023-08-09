package pw.honu.dvs.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.managers.MatchManager;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void playerRespawn(PlayerRespawnEvent ev) {
        if (MatchManager.instance.getMatchState() != MatchState.GATHERING) {
            return;
        }

        ev.setRespawnLocation(MatchManager.instance.getRunningMap().getPlayerStart());
    }

}
