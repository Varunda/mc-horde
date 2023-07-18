package pw.honu.dvs.managers;

import org.bukkit.entity.Player;
import pw.honu.dvs.player_stats.PlayerStats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStatsManager {

    public static PlayerStatsManager instance;

    static {
        instance = new PlayerStatsManager();
    }

    private Map<UUID, PlayerStats> stats = new HashMap<>();

    public void incrementKills(Player p) {
        this.incrementKills(p.getUniqueId());
    }

    public void incrementKills(UUID playerId) {
        if (!stats.containsKey(playerId)) {
            PlayerStats playerStats  = new PlayerStats();
            playerStats.setPlayerId(playerId);

            this.stats.put(playerId, playerStats);
        }

        PlayerStats playerStats = this.stats.get(playerId);
        playerStats.incrementKills();
    }

    public int getKills(Player p) {
        return getKills(p.getUniqueId());
    }

    public int getKills(UUID playerId) {
        if (!stats.containsKey(playerId)) {
            PlayerStats playerStats  = new PlayerStats();
            playerStats.setPlayerId(playerId);

            this.stats.put(playerId, playerStats);
        }

        PlayerStats playerStats = this.stats.get(playerId);
        return playerStats.getKills();
    }

    public void setKills(UUID playerId, int amount) {
        if (!stats.containsKey(playerId)) {
            PlayerStats playerStats  = new PlayerStats();
            playerStats.setPlayerId(playerId);

            this.stats.put(playerId, playerStats);
        }

        PlayerStats playerStats = this.stats.get(playerId);
        playerStats.setKills(amount);
    }

}
