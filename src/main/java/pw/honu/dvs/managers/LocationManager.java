package pw.honu.dvs.managers;

import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class LocationManager {

    static {
        //instance = new LocationManager();
    }
    //public static LocationManager instance;

    /**
     * Where all players are teleported at the start of the event
     */
    private @Nullable Location playerStart;

    /**
     * Location where players that die and are picking a monster class will respawn at
     */
    private @Nullable Location monsterLobby;

    /**
     * Locations where monsters spawn
     */
    private @Nullable List<Location> monsterSpawns = new ArrayList<>();

    private List<Location> checkpoints = new ArrayList<>();

    /**
     * What location monsters will attempt to move to when spawned
     */
    private @Nullable Location monsterTarget;

    private static Random random = new Random();

    public LocationManager() {

    }

    @Nullable
    public Location getPlayerStart() {
        return playerStart;
    }

    public void setPlayerStart(@Nullable Location playerStart) {
        this.playerStart = playerStart;
    }

    @Nullable public Location getMonsterLobby() {
        return monsterLobby;
    }

    public void setMonsterLobby(@Nullable Location monsterLobby) {
        this.monsterLobby = monsterLobby;
    }

    @Nullable public List<Location> getMonsterSpawns() {
        return monsterSpawns;
    }

    public void setMonsterSpawns(@Nullable List<Location> monsterSpawn) {
        this.monsterSpawns = monsterSpawn;
    }

    /**
     * Get a random spawn location from the list of valid monster spawns
     */
    public @Nullable Location getRandomMonsterSpawn() {
        if (this.monsterSpawns == null) {
            return null;
        }

        return this.monsterSpawns.get(random.nextInt(this.monsterSpawns.size()));
    }

    @Nullable
    public Location getMonsterTarget() {
        return monsterTarget;
    }

    public void setMonsterTarget(@Nullable Location monsterTarget) {
        this.monsterTarget = monsterTarget;
    }
}
