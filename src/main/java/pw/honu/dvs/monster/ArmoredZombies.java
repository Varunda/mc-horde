package pw.honu.dvs.monster;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import pw.honu.dvs.DvS;

public class ArmoredZombies {

    public static void init()  {
        DvS.instance.getLogger().info("loading " + ArmoredZombies.class.getCanonicalName());
    }

    public static MonsterTemplate IRON_ZOMBIE = new MonsterTemplate("zombie_iron", EntityType.ZOMBIE, DisguiseType.ZOMBIE);

    static {
        IRON_ZOMBIE.setMainHand(new ItemStack(Material.IRON_SWORD));
        IRON_ZOMBIE.setHelmet(new ItemStack(Material.IRON_HELMET));
        IRON_ZOMBIE.setChest(new ItemStack(Material.IRON_CHESTPLATE));
        IRON_ZOMBIE.setLegs(new ItemStack(Material.IRON_LEGGINGS));
        IRON_ZOMBIE.setBoots(new ItemStack(Material.IRON_BOOTS));
    }


}
