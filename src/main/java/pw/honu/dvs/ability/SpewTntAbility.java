package pw.honu.dvs.ability;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SpewTntAbility implements  Ability {

    public static final String name = "spew_tnt";

    private static Random _random = new Random();

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Throws unlight TNT around";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        Location loc = caster.getLocation();
        World world = loc.getWorld();

        final int amount = 3;
        float x, y, z;

        for (int i = 0; i < amount; ++i) {
            FallingBlock block = world.spawnFallingBlock(loc, Material.TNT.createBlockData());
            block.setDropItem(false);
            block.setHurtEntities(false);

            x = (_random.nextFloat() - 0.5f) / 4;
            y = 0.75f;
            z = (_random.nextFloat() - 0.5f) / 4;

            block.setVelocity(new Vector(x, y, z));
        }

        return true;
    }
}
