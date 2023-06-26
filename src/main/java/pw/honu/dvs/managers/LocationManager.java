package pw.honu.dvs.managers;

import org.bukkit.Location;

import javax.annotation.Nullable;

public class LocationManager {

    static {
        instance = new LocationManager();
    }
    public static LocationManager instance;

    /**
     * Where all players are teleported at the start of the event
     */
    private @Nullable Location playerStart;

    /**
     * Location where players that die and are picking a monster class will respawn at
     */
    private @Nullable Location monsterLobby;

    /**
     * Location where monsters spawn
     */
    private @Nullable Location monsterSpawn;

    /**
     * What location monsters will attempt to move to when spawned
     */
    private @Nullable Location monsterTarget;

    public LocationManager() {

    }

    @Nullable
    public Location getPlayerStart() {
        return playerStart;
    }

    public void setPlayerStart(@Nullable Location playerStart) {
        this.playerStart = playerStart;
    }

    @Nullable
    public Location getMonsterLobby() {
        return monsterLobby;
    }

    public void setMonsterLobby(@Nullable Location monsterLobby) {
        this.monsterLobby = monsterLobby;
    }

    @Nullable
    public Location getMonsterSpawn() {
        return monsterSpawn;
    }

    public void setMonsterSpawn(@Nullable Location monsterSpawn) {
        this.monsterSpawn = monsterSpawn;
    }

    @Nullable
    public Location getMonsterTarget() {
        return monsterTarget;
    }

    public void setMonsterTarget(@Nullable Location monsterTarget) {
        this.monsterTarget = monsterTarget;
    }
}
