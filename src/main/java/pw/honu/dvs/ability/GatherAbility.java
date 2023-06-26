package pw.honu.dvs.ability;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GatherAbility implements Ability {

    public static final String name = "gather";

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Call all nearby mobs to you";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        List<Entity> nearby = caster.getNearbyEntities(32, 32, 32);

        int count = 0;

        for (Entity e : nearby) {
            if (e instanceof Mob) {
                Mob m = (Mob) e;

                if (m.getPathfinder().moveTo(caster.getLocation())) {
                    ++count;
                }
            }
        }

        caster.sendMessage(ChatColor.DARK_PURPLE + "You summoned " + count + " monsters to your location");

        return true;
    }
}
