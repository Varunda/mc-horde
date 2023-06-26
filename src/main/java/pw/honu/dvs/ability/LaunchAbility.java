package pw.honu.dvs.ability;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LaunchAbility implements Ability {

    public static final String name = "launch";

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Launch all nearby entities in the direction you're looking";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        List<Entity> entities = caster.getNearbyEntities(5, 5, 5);

        Vector direction = caster.getLocation().getDirection();
        direction.multiply(2);

        int count = 0;

        for (Entity e : entities) {
            if (e instanceof Mob) {
                ++count;
                e.setVelocity(direction);
            }
        }

        caster.sendMessage(ChatColor.DARK_PURPLE + "Launched " + count + " mobs");

        return true;
    }

}
