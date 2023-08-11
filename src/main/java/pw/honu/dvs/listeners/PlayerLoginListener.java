package pw.honu.dvs.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.PortalCreateEvent;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.PlayerState;
import pw.honu.dvs.managers.*;
import ru.xezard.glow.data.glow.Glow;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void playerLogin(PlayerJoinEvent event) {
        MatchState state = MatchManager.instance.getMatchState();

        if (state == MatchState.PRE_GAME) {
            PlayerManager.instance.setPlayer(event.getPlayer().getUniqueId(), PlayerState.ALIVE);

            World lobby = Bukkit.getWorld("world");
            if (null == lobby) {
                DvSLogger.warn("No world 'world' (the lobby)");
            } else {
                event.getPlayer().teleport(lobby.getSpawnLocation());
            }

            if (!event.getPlayer().isOp()) {
                event.getPlayer().setGameMode(GameMode.ADVENTURE);
                event.getPlayer().getInventory().clear();
                event.getPlayer().clearActivePotionEffects();
                event.getPlayer().setExp(0);
            }

        } else if (state == MatchState.GATHERING) {
            PlayerManager.instance.setPlayer(event.getPlayer().getUniqueId(), PlayerState.ALIVE);
            PlayerManager.instance.sendToPlayerSpawn(event.getPlayer());
        } else if (state == MatchState.RUNNING) {
            if (!event.getPlayer().isOp()) {
                PlayerManager.instance.respawnToMonsterLobby(event.getPlayer().getUniqueId());
            }
        } else if (state == MatchState.POST_GAME) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            PlayerManager.instance.setPlayer(event.getPlayer().getUniqueId(), PlayerState.MONSTER);
            PlayerManager.instance.sendToPlayerSpawn(event.getPlayer());
        }

        BossBarManager.instance.addPlayer(event.getPlayer());
        ScoreboardManager.instance.update();
    }

    @EventHandler
    public void playerLogout(PlayerQuitEvent event) {
        BossBarManager.instance.removePlayer(event.getPlayer());
        ScoreboardManager.instance.update();
    }

    @EventHandler
    public void playerChangeWorld(PortalCreateEvent event) {
        if (MatchManager.instance.getMatchState() == MatchState.RUNNING) {
            event.setCancelled(true);
        }
    }

}
