package pw.honu.dvs;

import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import pw.honu.dvs.ability.AbilityUtils;
import pw.honu.dvs.managers.BossBarManager;
import pw.honu.dvs.managers.LocationManager;

import java.util.List;

import static pw.honu.dvs.MonsterTargetRunnable.monsterAmount;
import static pw.honu.dvs.MonsterTargetRunnable.monsterChecksNeeded;

@Deprecated
public class MonsterTargetCountRunnable extends BukkitRunnable {

    @Override
    public void run() {
        //List<LivingEntity> monsters = AbilityUtils.getMonsters(LocationManager.instance.getMonsterTarget(), 10);

        //BossBarManager.instance.setTitle("Monsters on point: " + monsters.size() + " / " + monsterAmount);
        //BossBarManager.instance.setProgress(1d - ((double)monsterChecksPassed / monsterChecksNeeded));
    }

}
