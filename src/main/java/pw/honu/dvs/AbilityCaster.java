package pw.honu.dvs;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.ability.Ability;
import pw.honu.dvs.monster.MonsterTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AbilityCaster  {

    private static Random random = new Random();

    private static class AbilityInstance {
        public Ability ability;
        public LivingEntity caster;
        public int cooldownLeft;
        public int cooldown;
        public int usesLeft;
    }

    private BukkitRunnable task = new BukkitRunnable() {
        @Override
        public void run() {
            Iterator<AbilityInstance> casters = instances.iterator();

            while (casters.hasNext()) {
                AbilityInstance i = casters.next();

                if (i.caster.isDead()) {
                    casters.remove();
                } else {
                    i.cooldownLeft -= 20;

                    if (i.cooldownLeft <= 0) {
                        if (i.ability.execute(i.caster)) {
                            --i.usesLeft;
                            if (i.usesLeft <= 0) {
                                casters.remove();
                            }
                        }

                        i.cooldownLeft = i.cooldown / 2 + (random.nextInt(i.cooldown / 2));
                    }
                }
            }
        }
    };

    public AbilityCaster() {
        task.runTaskTimer(DvS.instance, 20, 20);
    }

    private List<AbilityInstance> instances = new ArrayList<>();

    public void addCaster(LivingEntity caster, Ability ability, int casts, int cooldown) {
        AbilityInstance inst = new AbilityInstance();
        inst.ability = ability;
        inst.caster = caster;
        inst.cooldown = cooldown;
        inst.usesLeft = casts;
        inst.cooldownLeft = cooldown / 2 + (random.nextInt(cooldown / 2));

        DvSLogger.info("new caster of " + ability.name());

        instances.add(inst);
    }

    public void removeCaster(LivingEntity caster) {
        boolean found = false;
        int i = 0;

        for (i = 0; i < instances.size(); ++i) {
            AbilityInstance inst = instances.get(i);
            if (inst.caster.getUniqueId() == caster.getUniqueId()) {
                found = true;
                break;
            }
        }

        if (found) {
            DvS.instance.getLogger().info("Removing caster at index " + i);
            instances.remove(i);
        }
    }

}
