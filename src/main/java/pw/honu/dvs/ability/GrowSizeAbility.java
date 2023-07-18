package pw.honu.dvs.ability;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class GrowSizeAbility implements Ability {

    public static final String name = "grow_size";

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "if a slime: grow in size and destroy blocks around it";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        int size = 10;

        if (caster instanceof Slime) {
            Slime slime = (Slime) caster;

            size = slime.getSize() + 1;

            if (slime.getSize() < 10) {
                slime.setSize(slime.getSize() + 1);
                slime.getWorld().spawnParticle(Particle.CRIT_MAGIC, slime.getLocation(), Math.min(30, slime.getSize() * 5), size, 3, size);
            }
        }

        Collection<Block> blocks = AbilityUtils.getBlocksInRadius(caster.getLocation(), size);

        for (Block b : blocks) {
            // 1200 = obsidian and higher
            if (b.getType().getBlastResistance() >= 1200) {
                continue;
            }

            if (b.getY() <= caster.getLocation().getY()) {
                continue;
            }

            b.setType(Material.AIR);
            b.getWorld().spawnParticle(Particle.FLAME, b.getLocation(), 1);
        }

        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1, 1);

        return true;
    }

}
