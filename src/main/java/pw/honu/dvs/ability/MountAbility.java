package pw.honu.dvs.ability;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class MountAbility implements Ability {

    public static final String name = "mount";

    private static final Random random = new Random();

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Picks up a random nearby mob. If already a mob, drops it if above a player";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        if (caster.getPassengers().size() == 0) {
            List<LivingEntity> monsters = AbilityUtils.getMonsters(caster.getLocation(), 10);
            if (monsters.size() == 0) {
                return false;
            }

            LivingEntity randomMob = monsters.get(random.nextInt(monsters.size()));
            // don't pick itself up
            if (randomMob.getUniqueId() == caster.getUniqueId()) {
                return false;
            }

            Location loc = randomMob.getLocation();
            if (!caster.addPassenger(randomMob)) {
                return false;
            }

            loc.getWorld().spawnParticle(Particle.BUBBLE_COLUMN_UP, loc, 20, 0.5, 0.5, 1);

            return true;
        } else {
            Player p = AbilityUtils.getNearestPlayer(caster.getLocation());
            if (p == null) {
                return false;
            }

            if (p.getLocation().getY() + 5 > caster.getLocation().getY()) {
                return false;
            }

            caster.eject();
        }

        return true;
    }
}
