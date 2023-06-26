package pw.honu.dvs.ability;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;

public class MeltAbility implements Ability {

    public static final String name = "melt";

    private static Random random = new Random();

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Melt nearby blocks";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        Collection<Block> blocks = AbilityUtils.getBlocksInRadius(caster.getLocation(), 10);

        for (Block b : blocks) {
            if (b.isEmpty()) {
                continue;
            }

            Material mat = b.getType();

            if (mat == Material.LAVA && random.nextFloat() > 0.1) {
                b.setType(Material.OBSIDIAN);
            } else if ((mat == Material.OBSIDIAN || mat == Material.CRYING_OBSIDIAN || mat == Material.END_STONE || (mat.getBlastResistance() >= 6.0f && mat.getBlastResistance() <= 3000.0f)) && random.nextFloat() > 0.7) {
                b.setType(Material.NETHER_BRICKS);
            } else if (mat == Material.NETHER_BRICKS && random.nextFloat() > 0.7) {
                b.setType(Material.NETHERRACK);
            } else if (mat == Material.NETHERRACK && random.nextFloat() > 0.7) {
                b.setType(Material.GRAVEL);
            } else if (mat == Material.GRAVEL && random.nextFloat() > 0.7) {
                b.setType(Material.AIR);
            }
        }

        return true;
    }

}
