package pw.honu.dvs;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.managers.LocationManager;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.WaveManager;

public class WaveRepeatRunnable extends BukkitRunnable {

    private int left = 0;

    private final MatchManager match;

    public WaveRepeatRunnable(MatchManager match) {
        this.match = match;
    }

    @Override
    public void run() {
        if (WaveManager.instance.repeatInSeconds <= 0) {
            return;
        }

        --left;

        if (left <= 0) {
            if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
                return;
            }

            boolean spawned = MatchManager.instance.spawnWave(WaveManager.instance.current);

            left = WaveManager.instance.repeatInSeconds + 1;

            DvS.instance.getLogger().info("Spawning repeat wave");
        }

    }

}
