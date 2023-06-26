package pw.honu.dvs.ability;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;

public class EvaporateAbility implements Ability {

    public static final String name = "evaporate";

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Remove all water source blocks nearby";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        Collection<Block> blocks = AbilityUtils.getBlocksInRadius(caster.getLocation(), 5);

        if (blocks.size() == 0) {
            return false;
        }

        boolean used = false;

        for (Block b : blocks) {
            if (b.getType() == Material.WATER) {
                b.setType(Material.AIR);
                used = true;
            }
        }

        return used;
    }

}
