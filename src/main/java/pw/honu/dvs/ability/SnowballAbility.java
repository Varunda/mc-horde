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

import java.util.Random;

public class SnowballAbility implements Ability {

    public static final String name = "snowball";
    private static Random random = new Random();

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Throw a flurry of snowballs";
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

        for (int i = 0; i < 20; ++i){
            Location s = spawnLocation.clone();

            Entity projEntity = w.spawnEntity(s, EntityType.SNOWBALL);
            if (projEntity instanceof Projectile) {
                Projectile proj = (Projectile) projEntity;
                proj.setShooter(caster);
            }

            projEntity.setVelocity(caster.getLocation().clone().add(Vector.getRandom()).getDirection());
        }

        return true;
    }

}
