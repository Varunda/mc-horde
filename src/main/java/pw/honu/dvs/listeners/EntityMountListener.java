package pw.honu.dvs.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityMountEvent;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.MonsterManager;

import java.util.Arrays;
import java.util.Collection;

public class EntityMountListener implements Listener {

    private final Collection<EntityType> IGNORED_TYPES = Arrays.asList(
            EntityType.BOAT,
            EntityType.MINECART,
            EntityType.CHEST_BOAT
    );

    @EventHandler
    public void entityMount(EntityMountEvent ev) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        Entity mounter = ev.getEntity();

        if (!MonsterManager.instance.isTrackedMonster(mounter)) {
            return;
        }

        EntityType type = ev.getMount().getType();

        if (IGNORED_TYPES.contains(type)) {
            ev.setCancelled(true);
        }
    }

}
