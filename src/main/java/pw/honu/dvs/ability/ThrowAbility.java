package pw.honu.dvs.ability;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class ThrowAbility implements Ability {

    public static final String name = "throw";

    @Override public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Throw a block";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        Location loc = caster.getLocation();
        World world = loc.getWorld();

        if (world == null) {
            return false;
        }

        FallingBlock b = world.spawnFallingBlock(loc, Material.GRAVEL.createBlockData());
        b.setDropItem(false);
        b.setVelocity(loc.getDirection());

        return true;
    }

}
