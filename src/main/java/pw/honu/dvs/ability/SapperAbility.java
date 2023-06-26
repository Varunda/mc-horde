package pw.honu.dvs.ability;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;

public class SapperAbility implements Ability {

    private static Random random = new Random();

    public static final String name = "sapper";

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "10% chance to remove 3 blocks above the caster nearby";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        Collection<Block> nearby = AbilityUtils.getBlocksInRadius(caster.getLocation(), 8);

        int removed = 0;

        for (Block b : nearby) {
            if (b.getLocation().getBlockY() <= caster.getLocation().getBlockY()) {
                continue;
            }

            if (b.getType() != Material.BEDROCK && random.nextFloat() >= 0.9){
                b.setType(Material.AIR);
                b.getWorld().playSound(b.getLocation(), Sound.BLOCK_DEEPSLATE_BREAK, 1, 1);
                ++removed;
            }

            if (removed >= 3) {
                break;
            }
        }

        if (removed > 0) {
            caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_PIG_AMBIENT, 1, 1);
        }

        return true;
    }
}
