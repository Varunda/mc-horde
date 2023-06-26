package pw.honu.dvs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;
import pw.honu.dvs.ability.AbilityUtils;
import pw.honu.dvs.managers.BossBarManager;
import pw.honu.dvs.managers.LocationManager;
import pw.honu.dvs.managers.MonsterManager;
import pw.honu.dvs.util.TitleUtil;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class MonsterTargetRunnable extends BukkitRunnable {

    public static int monsterAmount = 10;

    public static int monsterChecksPassed = 0;

    public static int monsterChecksNeeded = 10;

    @Override
    public void run() {
        if (LocationManager.instance.getMonsterTarget() == null) {
            DvS.instance.getLogger().info("no monster target set, not running targeter");
            return;
        }

        List<LivingEntity> monsters = AbilityUtils.getMonsters(LocationManager.instance.getMonsterTarget(), 10);

        if (monsters.size() >= monsterAmount) {
            ++monsterChecksPassed;

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

                LocationManager.instance.getMonsterTarget().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, LocationManager.instance.getMonsterTarget(), 10, 5d, 5d, 1d);
                LocationManager.instance.getMonsterTarget().getWorld().playSound(LocationManager.instance.getMonsterTarget(), Sound.ENTITY_WITHER_SPAWN, 1, 1);

                LocationManager.instance.setMonsterSpawn(LocationManager.instance.getMonsterTarget());
                LocationManager.instance.setMonsterTarget(null);

                monsterChecksPassed = 0;
            } else {
                BossBarManager.instance.setTitle("Monsters on point: " + monsters.size() + " / " + monsterAmount);
                BossBarManager.instance.setProgress(1d - ((double)monsterChecksPassed / monsterChecksNeeded));
            }
        } else {
            BossBarManager.instance.setTitle("Monsters on point: " + monsters.size() + " / " + monsterAmount);
        }

    }

}
