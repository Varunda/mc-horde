package pw.honu.dvs.monster;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import pw.honu.dvs.DvS;
import pw.honu.dvs.item.AbilityItem;
import pw.honu.dvs.util.ItemUtil;

public class PlayerMonsters {

    public static void init()  {
        DvS.instance.getLogger().info("loading " + PlayerMonsters.class.getCanonicalName());
    }

    public static final MonsterTemplate PLAYER_PILLAGER = new MonsterTemplate("player_pillager", EntityType.PILLAGER, DisguiseType.PILLAGER);
    public static final MonsterTemplate PLAYER_VINDICATOR = new MonsterTemplate("player_vindicator", EntityType.VINDICATOR, DisguiseType.VINDICATOR);

    public static final MonsterTemplate PLAYER_MONSTER_MELEE = new MonsterTemplate("player_monster_melee", EntityType.PIGLIN, DisguiseType.PIGLIN);
    public static final MonsterTemplate PLAYER_MONSTER_RANGED = new MonsterTemplate("player_monster_ranged", EntityType.PIGLIN, DisguiseType.PIGLIN);
    public static final MonsterTemplate PLAYER_MONSTER_SAPPER = new MonsterTemplate("player_monster_sapper", EntityType.PIGLIN_BRUTE, DisguiseType.PIGLIN_BRUTE);
    public static final MonsterTemplate PLAYER_MONSTER_CONTROLLER = new MonsterTemplate("player_monster_controller", EntityType.PIGLIN_BRUTE, DisguiseType.PIGLIN_BRUTE);

    static {
        PLAYER_PILLAGER.setMainHand(new ItemStack(Material.IRON_AXE));

        PLAYER_VINDICATOR.addInventoryItem(new ItemStack(Material.CROSSBOW));
        PLAYER_VINDICATOR.addInventoryItem(new ItemStack(Material.ARROW, 64));
        PLAYER_VINDICATOR.addInventoryItem(new ItemStack(Material.ARROW, 64));

        PLAYER_MONSTER_MELEE.setHelmet(ItemUtil.getColoredLeatherHelmet(Color.RED));
        PLAYER_MONSTER_MELEE.addInventoryItem(new ItemStack(Material.IRON_SWORD));

        PLAYER_MONSTER_RANGED.setHelmet(ItemUtil.getColoredLeatherHelmet(Color.RED));
        PLAYER_MONSTER_RANGED.addInventoryItem(new ItemStack(Material.BOW));
        PLAYER_MONSTER_RANGED.addInventoryItem(new ItemStack(Material.ARROW, 64));
        PLAYER_MONSTER_RANGED.addInventoryItem(new ItemStack(Material.ARROW, 64));

        PLAYER_MONSTER_SAPPER.setHelmet(ItemUtil.getColoredLeatherHelmet(Color.RED));
        PLAYER_MONSTER_SAPPER.addInventoryItem(new ItemStack(Material.GOLDEN_PICKAXE));
        //PLAYER_MONSTER_SAPPER.addInventoryItem(new ItemStack(Material.TNT, 1));
        PLAYER_MONSTER_SAPPER.addInventoryItem(new ItemStack(Material.FIRE_CHARGE, 3));
        PLAYER_MONSTER_SAPPER.addInventoryItem(new ItemStack(Material.WEEPING_VINES, 10));
        PLAYER_MONSTER_SAPPER.addInventoryItem(new ItemStack(Material.LADDER, 10));

        PLAYER_MONSTER_CONTROLLER.setHelmet(ItemUtil.getColoredLeatherHelmet(Color.RED));
        PLAYER_MONSTER_CONTROLLER.addInventoryItem(new ItemStack(Material.STONE_SWORD));
        PLAYER_MONSTER_CONTROLLER.setOffHand(AbilityItem.create(AbilityItem.PATHFIND_NEAR_ITEM, 1));

    }

}
