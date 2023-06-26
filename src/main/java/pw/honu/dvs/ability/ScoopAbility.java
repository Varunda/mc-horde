package pw.honu.dvs.ability;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;

public class ScoopAbility implements Ability {

    private static Random random = new Random();

    public static final String name = "scoop";

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "scoops up nearby liquids into a bucket";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {

        EntityEquipment eq = caster.getEquipment();
        if (eq == null) {
            return false;
        }

        if (eq.getItemInMainHand().getType() != Material.BUCKET) {
            return false;
        }

        Collection<Block> blocks = AbilityUtils.getBlocksInRadius(caster.getLocation(), 10);

        for (Block b : blocks) {
            if (b.isEmpty()) {
                continue;
            }

            if (!b.isLiquid()) {
                continue;
            }

            BlockData data = b.getBlockData();
            if (data instanceof Levelled) {
                Levelled level = (Levelled)  data;
                if (level.getLevel() != 0) {
                    continue;
                }
            }

            if (random.nextFloat() > 0.5) {
                if (b.getType() == Material.LAVA) {
                    eq.setItemInMainHand(new ItemStack(Material.LAVA_BUCKET, 1));
                } else if (b.getType() == Material.WATER) {
                    eq.setItemInMainHand(new ItemStack(Material.WATER_BUCKET, 1));
                }

                b.setType(Material.AIR);
                break;
            }
        }

        return true;
    }

}
