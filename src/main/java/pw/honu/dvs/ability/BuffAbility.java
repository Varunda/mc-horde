package pw.honu.dvs.ability;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BuffAbility implements Ability {

    final private PotionEffect effect;
    private String name;

    public BuffAbility(@NotNull String name, @NotNull PotionEffect effect) {
        this.effect = effect;
        this.name = name;
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "";
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        List<LivingEntity> monsters = AbilityUtils.getMonsters(caster.getLocation(), 5d);

        for (LivingEntity le : monsters)  {
            le.addPotionEffect(this.effect);
        }

        return true;
    }

}
