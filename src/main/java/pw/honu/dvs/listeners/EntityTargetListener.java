package pw.honu.dvs.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.checkerframework.common.value.qual.EnumVal;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.PlayerState;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.MonsterManager;
import pw.honu.dvs.managers.PlayerManager;

public class EntityTargetListener implements Listener {

    @EventHandler
    public void entityTarget(EntityTargetLivingEntityEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity source = (LivingEntity) event.getEntity();

        if (MonsterManager.instance.getAliveMonsters().contains(source)) {
            if (event.getTarget() == null) {
                Player p = PlayerManager.instance.getRandomAlive();

                if (p != null) {
                    event.setTarget(p);
                }
            } else if (MonsterManager.instance.getAliveMonsters().contains(event.getTarget())) {
                event.setCancelled(true);
            } else if (event.getTarget() instanceof Player) {
                Player p = (Player) event.getTarget();
                if (PlayerManager.instance.getPlayer(p.getUniqueId()) != PlayerState.ALIVE) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void entityCombust(EntityCombustEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();

            if (PlayerManager.instance.getPlayer(p.getUniqueId()) == PlayerState.MONSTER) {
                event.setCancelled(true);
            }
        } else if (event.getEntity() instanceof LivingEntity) {
            LivingEntity le = (LivingEntity) event.getEntity();

            if (MonsterManager.instance.isTrackedMonster(le.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

}
