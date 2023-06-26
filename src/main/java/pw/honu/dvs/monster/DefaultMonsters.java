package pw.honu.dvs.monster;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import pw.honu.dvs.DvS;
import pw.honu.dvs.managers.MonsterManager;

public class DefaultMonsters {

    public static void init()  {
        DvS.instance.getLogger().info("loading " + DefaultMonsters.class.getCanonicalName());
    }

    public static final MonsterTemplate ZOMBIE = new MonsterTemplate("zombie", EntityType.ZOMBIE, DisguiseType.ZOMBIE);
    public static final MonsterTemplate SKELETON = new MonsterTemplate("skeleton", EntityType.SKELETON, DisguiseType.SKELETON);
    public static final MonsterTemplate CREEPER = new MonsterTemplate("creeper", EntityType.CREEPER, DisguiseType.CREEPER);
    public static final MonsterTemplate SPIDER = new MonsterTemplate("spider", EntityType.SPIDER, DisguiseType.SPIDER);
    public static final MonsterTemplate WITCH  = new MonsterTemplate("witch", EntityType.WITCH, DisguiseType.WITCH);
    public static final MonsterTemplate EVOKER = new MonsterTemplate("evoker", EntityType.EVOKER, DisguiseType.EVOKER);
    public static final MonsterTemplate RAVAGER = new MonsterTemplate("ravager", EntityType.RAVAGER, DisguiseType.RAVAGER);
    public static final MonsterTemplate BLAZE = new MonsterTemplate("blaze", EntityType.BLAZE, DisguiseType.BLAZE);
    public static final MonsterTemplate GHAST = new MonsterTemplate("ghast", EntityType.GHAST, DisguiseType.GHAST);
    public static final MonsterTemplate ILLUSIONER = new MonsterTemplate("illusioner", EntityType.ILLUSIONER, DisguiseType.ILLUSIONER);
    public static final MonsterTemplate GIANT = new MonsterTemplate("giant", EntityType.GIANT, DisguiseType.GIANT);

    public static final MonsterTemplate PILLAGER = new MonsterTemplate("pillager", EntityType.PILLAGER, DisguiseType.PILLAGER);
    public static final MonsterTemplate VINDICATOR = new MonsterTemplate("vindicator", EntityType.VINDICATOR, DisguiseType.VINDICATOR);

    public static final MonsterTemplate PIGLIN_ARCHER = new MonsterTemplate("piglin_archer", EntityType.PIGLIN_BRUTE, DisguiseType.PIGLIN_BRUTE);
    public static final MonsterTemplate PIGLIN = new MonsterTemplate("piglin", EntityType.PIGLIN, DisguiseType.PIGLIN);
    public static final MonsterTemplate PIGLIN_BRUTE = new MonsterTemplate("piglin_brute", EntityType.PIGLIN_BRUTE, DisguiseType.PIGLIN_BRUTE);
    public static final MonsterTemplate PIGLIN_BRUTE_ARCHER = new MonsterTemplate("piglin_brute_archer", EntityType.PIGLIN_BRUTE, DisguiseType.PIGLIN_BRUTE);

    static {
        SKELETON.setMainHand(new ItemStack(Material.BOW));

        //VINDICATOR.setMainHand(new ItemStack(Material.WOODEN_AXE));
        PILLAGER.setMainHand(new ItemStack(Material.CROSSBOW));

        PIGLIN.setMainHand(new ItemStack(Material.STONE_AXE));

        PIGLIN_ARCHER.setMainHand(new ItemStack(Material.BOW));

        PIGLIN_BRUTE.setMainHand(new ItemStack(Material.IRON_AXE));

        PIGLIN_BRUTE_ARCHER.setMainHand(new ItemStack(Material.CROSSBOW));
    }

}
