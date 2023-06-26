package pw.honu.dvs.mob_goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.managers.LocationManager;

import java.util.EnumSet;

public class MonsterTargetMobGoal implements Goal<Mob> {

    private final GoalKey<Mob> key;
    private final Mob mob;

    private int cooldown;

    public MonsterTargetMobGoal(Plugin plugin, Mob mob) {
        this.key = GoalKey.of(Mob.class, new NamespacedKey(plugin, "move_to_monster_target"));
        this.mob = mob;
    }

    @Override
    public boolean shouldActivate() {
        if (cooldown > 0) {
            --cooldown;
            return false;
        }

        return shouldStayActive();
    }

    @Override
    public boolean shouldStayActive() {
        if (LocationManager.instance.getMonsterTarget() == null) {
            return false;
        }

        // if the mob has a target and is less than 15 block blocks away, stop pathing towards the monster target
        if (mob.getTarget() != null && mob.getTarget().getLocation().distanceSquared(mob.getLocation()) <= 225d) {
            return false;
        }

        // if the mob is more than 2 blocks away (and no target), pathfind towards it
        if (mob.getLocation().distanceSquared(LocationManager.instance.getMonsterTarget()) > 4d) {
            return true;
        }

        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        mob.getPathfinder().stopPathfinding();
        cooldown = 20 * 10;
    }

    @Override
    public void tick() {
        if (LocationManager.instance.getMonsterTarget() == null) {
            return;
        }

        if (mob.getLocation().distance(LocationManager.instance.getMonsterTarget()) < 2d) {
            mob.getPathfinder().stopPathfinding();
        } else {
            this.mob.getPathfinder().moveTo(LocationManager.instance.getMonsterTarget(), 1d);
        }
    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return key;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE);
    }

}
