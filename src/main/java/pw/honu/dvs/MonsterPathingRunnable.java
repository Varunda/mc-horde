package pw.honu.dvs;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;
import pw.honu.dvs.ability.AbilityUtils;
import pw.honu.dvs.managers.LocationManager;
import pw.honu.dvs.managers.MonsterManager;

import java.util.List;
import java.util.Set;

@Deprecated()
public class MonsterPathingRunnable extends BukkitRunnable {

    @Override
    public void run() {
        if (LocationManager.instance.getMonsterTarget() == null) {
            DvS.instance.getLogger().info("no monster target set, not running pather");
            return;
        }

        Set<LivingEntity> monsters = MonsterManager.instance.getAliveMonsters();

        int hasTarget = 0;
        int hasPath = 0;
        int setPathSuccess = 0;
        int setPathFail = 0;

        for (LivingEntity le : monsters) {
            if (le instanceof Mob) {
                Mob m = (Mob) le;

                if (m.getTarget() != null) {
                    ++hasTarget;
                    continue;
                }

                if (m.getPathfinder().getCurrentPath() != null) {
                    ++hasPath;
                    continue;
                }
                if (m.getPathfinder().moveTo(LocationManager.instance.getMonsterTarget())) {
                    ++setPathSuccess;
                } else {
                    ++setPathFail;
                }
            }
        }

        DvS.instance.getLogger().info(String.format("updated target for %d monsters. [Had target=%d] [Had path=%d] [Set success=%d] [Set fail=%d]", monsters.size(), hasTarget, hasPath, setPathSuccess, setPathFail));

    }
}
