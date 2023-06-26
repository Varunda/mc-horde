package pw.honu.dvs.ability;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;

public class FreezeAbility implements Ability {

    public static final String name = "freeze";

    private static Random random = new Random();

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Freeze water";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        Collection<Block> blocks = AbilityUtils.getBlocksInRadius(caster.getLocation(), 10);

        for (Block b : blocks) {
            if (b.isEmpty()) {
                continue;
            }

            Material mat = b.getType();

            if (mat == Material.WATER && random.nextFloat() > 0.7) {
                b.setType(Material.ICE);
            } else if (mat == Material.ICE && random.nextFloat() > 0.7) {
                b.setType(Material.PACKED_ICE);
            } else if (mat == Material.PACKED_ICE && random.nextFloat() > 0.7) {
                b.setType(Material.BLUE_ICE);
            } else if (mat == Material.BLUE_ICE) {
                b.setType(Material.AIR);
            }
        }

        return true;
    }
}
