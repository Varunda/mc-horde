package pw.honu.dvs.ability;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.DvSLogger;

public class FireballAbility implements Ability {

    public static final String name = "fireball";

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Launch a fireball at the target";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        Location target = AbilityUtils.getTarget(caster);
        if (target == null) {
            return false;
        }
        World w = caster.getWorld();

        Location spawnLocation = caster.getLocation();
        spawnLocation.setY(spawnLocation.getBlockY() + 3);

        Location targetZero = target.clone(); targetZero.setY(0);
        Location projZero = spawnLocation.clone(); projZero.setY(0);

        double dist = targetZero.distance(projZero);
        double y = spawnLocation.getY() - target.getY();

        double pitch = Math.atan2(dist, y);
        double pitchDeg = 90d - Math.toDegrees(pitch);

        spawnLocation.setPitch((float) pitchDeg);

        Entity projEntity = w.spawnEntity(spawnLocation, EntityType.FIREBALL);
        if (projEntity instanceof Projectile) {
            Projectile proj = (Projectile) projEntity;
            proj.setShooter(caster);
        }

        return true;
    }
}
