package pw.honu.dvs.map;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HordeMapSettings {

    /**
     * does this map increase ore drops?
     */
    private boolean increaseOreDrops = true;

    /**
     * does this map increase the items dropped by mobs
     */
    private boolean increaseMobDrops = true;

    public boolean getIncreaseOreDrops() {
        return this.increaseOreDrops;
    }

    public void setIncreaseOreDrops(boolean b) {
        this.increaseOreDrops = b;
    }

    public void setIncreaseMobDrops(boolean increaseMobDrops) {
        this.increaseMobDrops = increaseMobDrops;
    }

    public boolean getIncreaseMobDrops() {
        return this.increaseMobDrops;
    }
}
