package pw.honu.dvs.listeners;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.managers.BossBarManager;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.MonsterManager;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MonsterDeathListener implements Listener {

    private static Random _random = new Random();

    @EventHandler(priority = EventPriority.NORMAL)
    public void entityDeath(EntityDeathEvent event) {
        if (MatchManager.instance.getMatchState() == MatchState.GATHERING) {
            if (event.getEntity() instanceof Player) {
                return;
            }

            for (ItemStack drop : event.getDrops()) {
                drop.setAmount(drop.getAmount() * 2 + _random.nextInt(3));
            }

            event.setDroppedExp(event.getDroppedExp() * 2);

            return;
        }

        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        LivingEntity le = event.getEntity();
        event.setDroppedExp(0);
        event.getDrops().clear();

        List<MetadataValue> dvsID = le.getMetadata("dvs-spawned");
        if (dvsID.size() == 0) {
            return;
        }

        MonsterManager.instance.removeAliveMonster(le);
        //MatchManager.instance.updateBossBar();
    }

    @EventHandler
    public void entityUnload(EntityRemoveFromWorldEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity le = (LivingEntity) event.getEntity();

        MonsterManager.instance.removeAliveMonster(le);
    }

}
