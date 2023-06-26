package pw.honu.dvs.ability;

import com.sun.jna.platform.win32.OaIdl;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;

public class SetFireAbility implements Ability {

    public static final String name = "set_fire";

    private static final Random _random = new Random();

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Sets nearby blocks on fire";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {

        Collection<Block> blocks = AbilityUtils.getBlocksInRadius(caster.getLocation(), 10);

        int count = 8;

        for (Block b : blocks) {
            if (_random.nextFloat() < 0.95) {
                continue;
            }

            Block above = b.getRelative(BlockFace.UP);

            if (!above.getType().isCollidable()) {
                above.setType(Material.FIRE);

                if (--count <= 0) {
                    break;
                }
            }
        }

        if (count == 8) {
            return false;
        }

        return true;
    }

}
