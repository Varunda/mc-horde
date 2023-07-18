package pw.honu.dvs.listeners;

import net.minecraft.world.level.block.BlockRedstoneComparator;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Breedable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pw.honu.dvs.DvS;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.managers.MatchManager;

public class BreedListener implements Listener {

    @EventHandler
    public void breedEvent(EntityBreedEvent ev) {
        if (MatchManager.instance.getMatchState() != MatchState.GATHERING) {
            return;
        }

        final Breedable mom = (ev.getMother() instanceof Breedable) ? (Breedable) ev.getMother() : null;
        final Breedable dad = (ev.getFather() instanceof Breedable) ? (Breedable) ev.getFather() : null;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (mom != null && !mom.isDead()) {
                    mom.setAge(mom.getAge() + (5 * 60 * 20) - 10); // 200 second breeding cooldown?
                    mom.setBreed(true);
                }

                if (dad != null && !dad.isDead()) {
                    dad.setAge(dad.getAge() + (5 * 60 * 20) - 10); // 200 second breeding cooldown?
                    dad.setBreed(true);
                }
            }
        }.runTaskLater(DvS.instance, 20);
    }

}
