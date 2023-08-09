package pw.honu.dvs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;
import pw.honu.dvs.ability.AbilityUtils;
import pw.honu.dvs.managers.BossBarManager;
import pw.honu.dvs.managers.LocationManager;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.MonsterManager;
import pw.honu.dvs.util.TitleUtil;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MonsterTargetRunnable extends BukkitRunnable {

    /**
     * how many monsters are needed on the checkpoint to take away a tick of progress
     */
    public static final int monsterAmount = 10;

    /**
     * how many checks need to pass before the checkpoint is considered lost
     */
    public static final int monsterChecksNeeded = 20;

    /**
     * how many times this runnable is ran before a check takes place
     */
    private static final int checkCooldownMax = 5;

    /**
     * how many checks have passed
     */
    private int monsterChecksPassed = 0;

    /**
     * how many more runs of this runnable need to take place before a monster check takes place
     */
    private int checkCooldown = checkCooldownMax;

    private int checksPassedInARow = 0;

    private final MatchManager match;

    public MonsterTargetRunnable(MatchManager match) {
        this.match = match;
    }

    @Override
    public void run() {
        Location monsterTarget = match.getRunningMap().getMonsterTarget();
        if (monsterTarget == null) {
            DvS.instance.getLogger().info("no monster target set, not running targeter");
            return;
        }

        List<LivingEntity> monsters = AbilityUtils.getMonsters(monsterTarget, 10);
        BossBarManager.instance.setTitle("Monsters at checkpoint: " + monsters.size() + " / " + monsterAmount);

        DvS.instance.getLogger().info("Target cooldown: " + checkCooldown);

        --checkCooldown;
        if (checkCooldown > 0) {
            return;
        }

        DvS.instance.getLogger().info("doing check: " + monsters.size() + " at checkpoint");

        if (monsters.size() >= monsterAmount) {
            ++monsterChecksPassed;
            ++checksPassedInARow;

            DvS.instance.getLogger().info("check passed (needed " + monsterAmount + "), have passed " + monsterChecksPassed + " of " + monsterChecksNeeded + " checks needed");

            if (monsterChecksPassed >= monsterChecksNeeded) {
                TitleUtil.title(
                        Title.title(
                                Component.text(ChatColor.RED + "The checkpoint was lost"),
                                Component.text(""),
                                Title.Times.times(
                                        Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1)
                                )
                        )
                );

                monsterTarget.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, monsterTarget, 10, 5d, 5d, 1d);
                monsterTarget.getWorld().playSound(monsterTarget, Sound.ENTITY_WITHER_SPAWN, 1, 1);

                if (match.getRunningMap().isFinalPhase()) {
                    Bukkit.broadcast(Component.text("The horde has captured the final checkpoint").color(TextColor.color(255, 0, 0)));
                    DvS.instance.getLogger().info("was last phase, ending match");
                    match.endMatch();
                } else {
                    Bukkit.broadcast(Component.text(ChatColor.YELLOW + "The horde has captured the checkpoint"));
                    DvS.instance.getLogger().info("not last phase, next phase");
                    match.getRunningMap().nextPhase();
                }

                monsterChecksPassed = 0;
            }
        } else {
            checksPassedInARow = 0;
        }

        // reset the check
        BossBarManager.instance.setProgress(1d - ((double)monsterChecksPassed / monsterChecksNeeded));
        checkCooldown = Math.max(0, checkCooldownMax - checksPassedInARow);
        DvS.instance.getLogger().info("checkCooldown set to " + checkCooldown);
    }

}
