package pw.honu.dvs.map;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HordeMapPhase {

    /**
     * Phase number. Starts at 1 and increments
     */
    private int phase;

    /**
     * What block the monsters will be trying to reach
     */
    private @NotNull  Vector monsterTarget = new Vector(0, 0, 0);

    /**
     * A list of valid monster spawns
     */
    private @NotNull List<Vector> monsterSpawns = new ArrayList<>();

    public @NotNull List<Vector> getMonsterSpawns() {
        return monsterSpawns;
    }

    public void setMonsterSpawns(@NotNull List<Vector> monsterSpawns) {
        this.monsterSpawns = monsterSpawns;
    }

    public @NotNull Vector getMonsterTarget() {
        return monsterTarget;
    }

    public void setMonsterTarget(@NotNull Vector monsterTarget) {
        this.monsterTarget = monsterTarget;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

}
