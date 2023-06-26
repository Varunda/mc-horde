package pw.honu.dvs.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.PortalCreateEvent;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.managers.BossBarManager;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.PlayerManager;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void playerLogin(PlayerJoinEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        BossBarManager.instance.addPlayer(event.getPlayer());
        PlayerManager.instance.respawnToMonsterLobby(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void playerLogout(PlayerQuitEvent event) {
        BossBarManager.instance.removePlayer(event.getPlayer());
    }

    @EventHandler
    public void playerChangeWorld(PortalCreateEvent event) {
        if (MatchManager.instance.getMatchState() == MatchState.RUNNING) {
            event.setCancelled(true);
        }
    }

}