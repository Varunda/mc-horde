package pw.honu.dvs.map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.util.ListUtil;

import javax.annotation.Nullable;

public class RunningHordeMap {

    private final World matchWorld;

    private final HordeMap map;

    private int currentPhase;
    private int phaseIndex;

    public RunningHordeMap(HordeMap map, World world) {
        this.map = map;
        this.matchWorld = world;
        this.currentPhase = map.getPhases().get(0).getPhase();
    }

    /**
     * Get the {@link World} that the map is running in
     */
    public @NotNull World getMatchWorld() {
        return this.matchWorld;
    }

    /**
     * Get the {@link HordeMap} that is currently running
     */
    public @NotNull HordeMap getMap() {
        return this.map;
    }

    /**
     * Get the current phase number
     */
    public int getCurrentPhaseNumber() {
        return this.currentPhase;
    }

    /**
     * Advance the running map to the next phase. If {@link RunningHordeMap#isFinalPhase()} is true, then this will do nothing
     */
    public void nextPhase() {
        if (isFinalPhase()) {
            return;
        }

        ++this.phaseIndex;
        this.currentPhase = this.map.getPhases().get(this.phaseIndex).getPhase();
    }

    /**
     * Get the current phase of the running map
     */
    public @NotNull HordeMapPhase getCurrentPhase() {
        @Nullable HordeMapPhase phase = this.getMap().getPhase(this.getCurrentPhaseNumber());
        assert phase != null;

        return phase;
    }

    /**
     * Check if the current phase of the running map is the final one or not
     */
    public boolean isFinalPhase() {
        return this.phaseIndex >= this.map.getPhases().size() - 1;
    }

    /**
     * Get the location where alive players respawn
     */
    public @NotNull Location getPlayerStart() {
        return map.getPlayerStart().toLocation(matchWorld);
    }

    /**
     * Get the location where monster players respawn
     */
    public @NotNull Location getMonsterLobby() {
        return map.getMonsterLobby().toLocation(matchWorld);
    }

    /**
     * Get the current monster target based on the current phase
     */
    public @NotNull Location getMonsterTarget() {
        return getCurrentPhase().getMonsterTarget().toLocation(this.getMatchWorld());
    }

    /**
     * Get a random monster spawn location based on the current phase
     */
    public @NotNull Location getRandomMonsterSpawn() {
        HordeMapPhase phase = getCurrentPhase();

        Vector vec = ListUtil.getRandomElement(phase.getMonsterSpawns());
        return vec.toLocation(this.getMatchWorld());
    }

}
