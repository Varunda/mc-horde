package pw.honu.dvs;

import org.bukkit.scheduler.BukkitRunnable;
import pw.honu.dvs.managers.BossBarManager;
import pw.honu.dvs.managers.MatchManager;

public class GatheringCountdownRunnable extends BukkitRunnable {

    private int target;
    private int counter;

    private MatchManager match;

    public GatheringCountdownRunnable(MatchManager match, int target) {
        this.match = match;

        this.counter = 0;
        this.target = target;
    }

    @Override
    public void run() {
        ++counter;

        if (counter % 20 == 0) {
            int left = this.target - this.counter;

            int minutes = left / (20 * 60); // 20 ticks/sec, 60 seconds per minute
            int seconds = (left / 20) % 60;

            BossBarManager.instance.setTitle("Gathering time left: " + minutes + "m " + seconds + "s");
            BossBarManager.instance.setProgress(1d - (((double) this.counter) / this.target));
        }

        if (this.counter >= this.target) {
            this.match.startCombatPhase();
            this.cancel();
        }
    }

}
