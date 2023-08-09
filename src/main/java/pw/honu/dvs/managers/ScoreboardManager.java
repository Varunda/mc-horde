package pw.honu.dvs.managers;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.PlayerState;

public class ScoreboardManager {

    public static ScoreboardManager instance;

    static {
        instance = new ScoreboardManager();
    }

    private Scoreboard playerState;
    private Objective playerObjective;

    public ScoreboardManager() {
        this.playerState = Bukkit.getScoreboardManager().getNewScoreboard();
        this.playerObjective = this.playerState.registerNewObjective("state", Criteria.DUMMY, Component.text("State"));
        this.playerObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void update() {
        this.playerObjective.unregister();
        this.playerObjective = null;

        int aliveCount = PlayerManager.instance.getAlive().size();
        int total = Bukkit.getOnlinePlayers().size();
        Component c = Component.text(MatchManager.instance.getMatchState() + " " + aliveCount + "/" + total);
        this.playerObjective = this.playerState.registerNewObjective("state", Criteria.DUMMY, c);
        this.playerObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int i = 0;
        for (Player online : Bukkit.getOnlinePlayers()) {
            String score = "";

            PlayerState playerState = PlayerManager.instance.getPlayer(online.getUniqueId());
            if (playerState == PlayerState.ALIVE) {
                score = ChatColor.GREEN.toString();
            } else if (playerState == PlayerState.RESPAWNING) {
                score = ChatColor.RED.toString();
            } else if (playerState == PlayerState.MONSTER) {
                score = ChatColor.RED.toString();
            } else {
                score = ChatColor.GRAY.toString();
            }

            score += online.getName();

            Score s = this.playerObjective.getScore(score);
            s.setScore(i++);
        }

        // this for loop is after, cause we need to set the scores first
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.setScoreboard(playerState);
        }
    }

}
