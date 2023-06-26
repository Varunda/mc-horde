package pw.honu.dvs.managers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.DvS;
import pw.honu.dvs.monster.MonsterTemplate;
import pw.honu.dvs.wave.Wave;
import pw.honu.dvs.wave.WaveEntry;

import javax.annotation.Nullable;
import java.util.List;

public class WaveManager {

    static {
        instance = new WaveManager();
    }

    public static WaveManager instance;

    public Wave current;

    public WaveManager() {
        current = new Wave();
    }

    /**
     * Add a new wave entry to the current wave being built
     * @param entry Entry to be added
     */
    public void addToCurrent(WaveEntry entry) {
        if (current != null) {
            boolean alreadyExists = false;

            for (WaveEntry e : current.getEntries()) {
                if (e.getTemplate().getName().equals(entry.getTemplate().getName())) {
                    e.setCount(e.getCount() + entry.getCount());
                    alreadyExists = true;
                }
            }

            if (!alreadyExists) {
                current.addEntry(entry);
            }
        }
    }

    /**
     * Clear the current wave
     */
    public void clearCurrent() {
        if (current != null) {
            current.clear();
        }
    }

}
