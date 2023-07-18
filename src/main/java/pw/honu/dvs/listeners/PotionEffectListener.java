package pw.honu.dvs.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.PlayerState;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.PlayerManager;
import pw.honu.dvs.managers.PlayerStatsManager;

import java.util.UUID;

public class PotionEffectListener implements Listener {

    @EventHandler
    public void potionEffect(EntityPotionEffectEvent ev) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        if (!(ev.getEntity() instanceof Player)) {
            return;
        }

        Player p = (Player) ev.getEntity();

        UUID entityId = p.getUniqueId();
        PlayerState playerState = PlayerManager.instance.getPlayer(entityId);
        if (playerState != PlayerState.ALIVE) {
            return;
        }

        if (ev.getOldEffect() == null || ev.getNewEffect() != null) {
            return;
        }

        if (ev.getOldEffect().getType().equals(PotionEffectType.INCREASE_DAMAGE)
            && ev.getOldEffect().getAmplifier() == 100)  {

            int kills = PlayerStatsManager.instance.getKills(entityId);
            p.sendMessage(ChatColor.WHITE + "Your rampage lasted " + ChatColor.RED + kills + " kills");
            PlayerStatsManager.instance.setKills(entityId, 0);
        }
    }

}
