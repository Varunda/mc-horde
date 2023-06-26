package pw.honu.dvs.ability;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.managers.MonsterManager;

import java.util.List;

public class PathfindAbility implements Ability {

    public static final String name = "pathfind";

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Instruct all nearby mobs to pathfind to the targeted block";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        if (!(caster instanceof Player)) {
            return false;
        }

        Player p = (Player) caster;
        Block b = p.getTargetBlock(100);

        if (b == null) {
            return false;
        }

        List<Entity> nearby = p.getNearbyEntities(32, 32, 32);

        for (Entity e : nearby) {
            if (e instanceof Mob) {
                Mob m = (Mob) e;

                m.getPathfinder().moveTo(b.getLocation());
            }
        }

        return true;
    }
}
