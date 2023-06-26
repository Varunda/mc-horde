package pw.honu.dvs.ability;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;

public class ImplodeAbility implements Ability {

    public static final String name = "implode";

    public static Random random = new Random();

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Implode all nearby blocks, throwing them everywhere";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        Player nearest = AbilityUtils.getNearestPlayer(caster.getLocation());
        if (nearest == null) {
            return false;
        }

        double dist = nearest.getLocation().distance(caster.getLocation());
        if (dist >= 10d) {
            return false;
        }

        float x, y, z;

        World world = caster.getWorld();

        world.playEffect(caster.getLocation(), Effect.FIREWORK_SHOOT, 51);

        Collection<Block> blocks = AbilityUtils.getBlocksInRadius(caster.getLocation(), 5);
        for (Block b : blocks) {
            if (b.getType() == Material.BEDROCK) {
                continue;
            }

            BlockData data = b.getBlockData();
            world.getBlockAt(b.getLocation()).setType(Material.AIR);

            FallingBlock block = world.spawnFallingBlock(b.getLocation(), data);
            block.setDropItem(false);
            block.setHurtEntities(false);

            float resistance = Math.max(0.1F, data.getMaterial().getBlastResistance()) + 1F;

            x = (random.nextFloat() - 0.5f) / resistance;
            y = random.nextFloat() / resistance;
            z = (random.nextFloat() - 0.5f) / resistance;

            block.setVelocity(new Vector(x, y, z));
        }

        return true;
    }

}
