package pw.honu.dvs.ability;


import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedBuffAbility extends BuffAbility {

    public static final String name = "buff_speed";

    public SpeedBuffAbility() {
        super(name, new PotionEffect(PotionEffectType.SPEED, 15 * 20, 1));
    }
}
