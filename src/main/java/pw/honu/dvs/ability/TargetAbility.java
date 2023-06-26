package pw.honu.dvs.ability;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.managers.MonsterManager;

public class TargetAbility implements Ability {

    public static final String name = "target";

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Target the caster";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        if (!(caster instanceof Player)) {
            return false;
        }

        Player p = (Player) caster;

        RayTraceResult result = p.rayTraceBlocks(50);

        if (result == null) {
            caster.sendMessage("Raytrace failed, returned null");
            return false;
        }

        if (result.getHitEntity() != null) {
            if (!(result.getHitEntity() instanceof LivingEntity))  {
                caster.sendMessage(result.getHitEntity().getType() + " is not a living entity");
            }

            LivingEntity target = (LivingEntity) result.getHitEntity();

            for (LivingEntity le : MonsterManager.instance.getAliveMonsters()) {
                if (le instanceof Mob) {
                    Mob m = (Mob) le;
                    m.setTarget(target);
                }
            }
        } else if (result.getHitBlock() != null) {
            Location where = p.getLocation();
            if (result.getHitBlock() != null) {
                where = result.getHitBlock().getLocation();
                where.setY(where.getBlockY() + 2);
            }

            Entity t = p.getWorld().spawnEntity(where, EntityType.CHICKEN);
            LivingEntity target = (LivingEntity) t;
            target.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 60 * 100, 1));

            for (LivingEntity le : MonsterManager.instance.getAliveMonsters()) {
                if (le instanceof Mob) {
                    Mob m = (Mob) le;

                    m.setTarget(target);
                }
            }
        }

        return false;
    }
}
