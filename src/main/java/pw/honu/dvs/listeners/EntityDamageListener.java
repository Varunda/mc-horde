package pw.honu.dvs.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;
import pw.honu.dvs.DvS;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.PlayerState;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.MonsterManager;
import pw.honu.dvs.managers.PlayerManager;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void entityDamageEntity(EntityDamageByEntityEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        Entity damager = event.getDamager();

        if (damager instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) damager).getShooter();
            if (shooter instanceof Entity) {
                damager = (Entity) shooter;
            }
        }

        if (!(damager instanceof Player)) {
            return;
        }

        Player attacker = (Player) damager;
        PlayerState state = PlayerManager.instance.getPlayer(attacker.getUniqueId());

        Entity attacked = event.getEntity();

        // Respawning players cannot attack anything
        if (state == PlayerState.RESPAWNING) {
            event.setCancelled(true);
        } else if (state == PlayerState.ALIVE) {
            if (attacked instanceof Player) {
                Player p = (Player) attacked;
                PlayerState playerState = PlayerManager.instance.getPlayer(p.getUniqueId());

                if (playerState == PlayerState.RESPAWNING) {
                    event.setCancelled(true);
                } else if (playerState == PlayerState.ALIVE) {
                    event.setCancelled(true);
                }
            }
        } else if (state == PlayerState.MONSTER) {
            if (MonsterManager.instance.isTrackedMonster(attacked)) {
                event.setCancelled(true);
            }
        } else {
            DvS.instance.getLogger().info("unchecked state of attacking player");
        }
    }

    //@EventHandler
    public void entityDamage(EntityDamageEvent ev) {

    }

}
