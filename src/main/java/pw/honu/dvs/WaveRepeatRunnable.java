package pw.honu.dvs;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.managers.LocationManager;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.WaveManager;

public class WaveRepeatRunnable extends BukkitRunnable {

    private int left = 0;

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
            if (LocationManager.instance.getMonsterSpawn() == null) {
                return;
            }

            boolean spawned = MatchManager.instance.spawnWave(WaveManager.instance.current);

            left = WaveManager.instance.repeatInSeconds + 1;

            DvS.instance.getLogger().info("Spawning repeat wave");
        }

    }

}
