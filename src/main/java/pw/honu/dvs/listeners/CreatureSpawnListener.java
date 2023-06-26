package pw.honu.dvs.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.managers.MatchManager;

public class CreatureSpawnListener implements Listener {

    @EventHandler
    public void creatureSpawn(CreatureSpawnEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        if (event.getEntityType() == EntityType.VEX || event.getEntityType() == EntityType.PHANTOM) {
            event.setCancelled(true);
        }

        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            event.setCancelled(true);
        }
    }

}
