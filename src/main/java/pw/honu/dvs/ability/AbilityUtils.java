package pw.honu.dvs.ability;

import it.unimi.dsi.fastutil.objects.AbstractReferenceList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import pw.honu.dvs.managers.MonsterManager;
import pw.honu.dvs.managers.PlayerManager;

import javax.annotation.Nullable;
import java.util.*;

public class AbilityUtils {

    private static Random _random = new Random();

    /**
     * Get the player nearest to a location
     */
    public static @Nullable Player getNearestPlayer(Location center) {
        Player p = null;
        double d = 999d;

        for (UUID id : PlayerManager.instance.getAlive()) {
            Player i = Bukkit.getPlayer(id);
            if (i == null) {
                continue;
            }

            if (i.getLocation().distance(center) < d) {
                p = i;
            }
        }

        return p;
    }

    public static @Nullable Location getTarget(LivingEntity le) {
        if (le instanceof Player) {
            Player p = (Player) le;
            Block b = p.getTargetBlock(100);
            if (b != null) {
                return b.getLocation();
            }
        }

        if (le instanceof Mob) {
            Mob m = ((Mob) le);
            if (m.getTarget() != null) {
                return m.getTarget().getLocation();
            }
        }

        return null;
    }

    public static Collection<Block> getBlocksInRadius(Location loc, int radius) {
        Collection<Block> blocks = new ArrayList<>();

        if (loc.getWorld() == null) {
            return blocks;
        }

        int diff = radius / 2;

        for (int x = 0; x < radius; ++x) {
            for (int y = 0; y < radius; ++y) {
                for (int z = 0; z < radius; ++z) {
                    Block b = loc.getWorld().getBlockAt(loc.getBlockX() + x - diff, loc.getBlockY() + y - diff, loc.getBlockZ() + z - diff);
                    if (b.getType() != Material.AIR) {
                        blocks.add(b);
                    }
                }
            }
        }

        return blocks;
    }

    public static Collection<Block> getRandomBlocksInRadius(Location loc, int radius, int count) {
        Collection<Block> blocks = new ArrayList<>();

        if (loc.getWorld() == null) {
            return blocks;
        }

        for (int i = 0; i < count; ++i) {
            int x_offset = _random.nextInt(-radius / 2, radius / 2);
            int y_offset = _random.nextInt(-radius / 2, radius / 2);
            int z_offset = _random.nextInt(-radius / 2, radius / 2);

            Block b = loc.getWorld().getBlockAt(loc.getBlockX() + x_offset, loc.getBlockY() + y_offset, loc.getBlockZ() + z_offset);
            if (b.getType() != Material.AIR) {
                blocks.add(b);
            }
        }

        return blocks;
    }

    /**
     * Get all monster mobs and monster players within the radius of a location
     */
    public static List<LivingEntity> getMonsters(Location center, double radius) {
        List<LivingEntity> inRange = new ArrayList<>();

        for (LivingEntity le : MonsterManager.instance.getAliveMonsters()) {
            if (le.getLocation().distance(center) <= radius) {
                inRange.add(le);
            }
        }

        for (UUID id : PlayerManager.instance.getMonsters()) {
            Player p = Bukkit.getPlayer(id);
            if (p == null) {
                continue;
            }

            if (p.getLocation().distance(center) <= radius) {
                inRange.add(p);
            }
        }

        return inRange;
    }

}
