package pw.honu.dvs.ability;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class StrengthBuffAbility extends BuffAbility {

    public static final String name = "buff_strength";

    public StrengthBuffAbility() {
        super(name, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15, 1));
    }

}
