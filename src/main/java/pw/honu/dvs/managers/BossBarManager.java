package pw.honu.dvs.managers;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BossBarManager {

    public static BossBarManager instance;
    static {
        instance = new BossBarManager();
    }

    private final BossBar bar;

    public BossBarManager() {
        bar = Bukkit.createBossBar(
                "Players alive",
                BarColor.GREEN,
                BarStyle.SOLID
        );
    }

    public void setProgress(double progress) {
        bar.setProgress(Math.min(1d, progress));
    }

    public void addPlayer(@NotNull Player p) {
        bar.addPlayer(p);
    }

    public void removePlayer(Player p) {
        bar.removePlayer(p);
    }

    /**
     * Remove the boss bar from all players
     */
    public void removeAll() {
        bar.removeAll();
    }

    public void setColor(@NotNull BarColor color) {
        bar.setColor(color);
    }

    public void setTitle(@NotNull String title) {
        bar.setTitle(title);
    }

}
