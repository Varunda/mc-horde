package pw.honu.dvs.player_stats;

import java.util.UUID;

public class PlayerStats {

    private UUID playerId;

    private int kills;

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void incrementKills() {
        this.setKills(this.getKills() + 1);
    }
}
