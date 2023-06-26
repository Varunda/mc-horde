package pw.honu.dvs.ability;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class RegenBuff extends BuffAbility {

    public static final String name = "buff_regen";

    public RegenBuff() {
        super(name, new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 2));
    }

}
