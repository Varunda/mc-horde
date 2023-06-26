package pw.honu.dvs.ability;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.managers.MonsterManager;

public class TargetNearAbility implements Ability {

    public static final String name = "target_near";

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Force nearby entities to target the clicked block";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        if (!(caster instanceof Player)) {
            return false;
        }

        Player p = (Player) caster;

        Block targetBlock = p.getTargetBlock(100);

        Location where = p.getLocation();
        if (targetBlock != null) {
            where = targetBlock.getLocation();
            where.setY(where.getBlockY() + 2);
        }

        Entity t = p.getWorld().spawnEntity(where, EntityType.CHICKEN);
        LivingEntity target = (LivingEntity) t;
        target.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 100, 1));

        for (Entity e : caster.getNearbyEntities(10, 10, 10)) {
            if (e instanceof Mob) {
                Mob m = (Mob) e;

                m.setTarget(target);
            }
        }

        return false;
    }
}
