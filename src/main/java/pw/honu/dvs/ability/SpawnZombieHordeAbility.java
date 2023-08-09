package pw.honu.dvs.ability;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.DvS;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.managers.MonsterManager;
import pw.honu.dvs.monster.MonsterTemplate;

import java.util.Random;

public class SpawnZombieHordeAbility implements Ability {

    private static final Random random = new Random();

    public static final String name = "spawn_zombie_horde";

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Spawns 3 random zombie types(regular, husk or drowned)";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        if (!(caster instanceof Player)) {
            return false;
        }

        Player p = (Player) caster;
        RayTraceResult result = p.rayTraceBlocks(100);
        if (result == null) {
            DvS.instance.getLogger().info("ray trace block failed");
            return false;
        }

        Block b = result.getHitBlock();

        if (b == null || b.getType() == Material.AIR) {
            return false;
        }

        MonsterTemplate reg = MonsterManager.instance.get("zombie");
        MonsterTemplate husk = MonsterManager.instance.get("husk");
        MonsterTemplate drowned = MonsterManager.instance.get("drowned");

        if (reg == null && husk == null && drowned == null) {
            DvSLogger.warn("Cannot cast " + name + ": all monster templates returned null");
            return false;
        }

        // if at least one of templates is null, have a fallback one
        MonsterTemplate fallback = (reg != null) ? reg : (husk != null) ? husk : drowned;

        Location loc = b.getLocation();
        loc.setY(loc.getBlockY() + 1);

        for (int i = 0; i < 3; ++i) {
            int type = random.nextInt(3); // 0 - 2

            MonsterTemplate template = null;

            if (type == 0 && reg != null) {
                template = reg;
            } else if (type == 1 && husk != null) {
                template = husk;
            } else if (type == 2 && drowned != null) {
                template = drowned;
            }

            if (template == null) {
                template = fallback;
            }

            MonsterManager.instance.spawn(template, loc);
        }

        return true;
    }

}
