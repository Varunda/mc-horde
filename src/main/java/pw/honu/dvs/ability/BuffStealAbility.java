package pw.honu.dvs.ability;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class BuffStealAbility implements Ability {

    public final static String name = "buff_steal";

    private final Random random = new Random();

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Steal buff from a single player and give it a random nearby mob";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {

        Player player = AbilityUtils.getNearestPlayer(caster.getLocation());
        if (player == null || player.getLocation().distance(caster.getLocation()) >= 20d) {
            return false;
        }

        Collection<PotionEffect> effects = player.getActivePotionEffects();
        if (effects.size() == 0) {
            return true;
        }

        player.clearActivePotionEffects();
        caster.getWorld().playSound(player, Sound.ENTITY_WITCH_CELEBRATE, 10, 1);
        caster.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, player.getLocation(), 15, 0.5d, 0d, 0.5d);

        List<LivingEntity> mobs = AbilityUtils.getMonsters(caster.getLocation(), 20d);
        if (mobs.size() == 0) {
            return true;
        }

        LivingEntity randomMob = mobs.get(random.nextInt(0, mobs.size() - 1));

        for (PotionEffect effect : effects) {
            randomMob.addPotionEffect(effect);
        }

        return true;
    }
}
