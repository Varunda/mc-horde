package pw.honu.dvs.monster;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;
import pw.honu.dvs.DvS;
import pw.honu.dvs.ability.*;

public class BuffMonsters {

    public static void init()  {
        DvS.instance.getLogger().info("loading " + BuffMonsters.class.getCanonicalName());
    }

    public static final MonsterTemplate PILLAGER_BUFF_SPEED = new MonsterTemplate("pillager_buff_speed", EntityType.PILLAGER, DisguiseType.PILLAGER);
    public static final MonsterTemplate PILLAGER_BUFF_STRENGTH = new MonsterTemplate("pillager_buff_strength", EntityType.PILLAGER, DisguiseType.PILLAGER);
    public static final MonsterTemplate WITCH_BUFF_REGEN = new MonsterTemplate("witch_buff_regen", EntityType.WITCH, DisguiseType.WITCH);

    public static final MonsterTemplate EVOKER_FIREBALL = new MonsterTemplate("evoker_fireball", EntityType.EVOKER, DisguiseType.EVOKER);

    public static final MonsterTemplate CREEPER_IMPLODE = new MonsterTemplate("creeper_implode", EntityType.CREEPER, DisguiseType.CREEPER);

    public static final MonsterTemplate HUSK_SPEW_SAND = new MonsterTemplate("husk_spew_sand", EntityType.HUSK, DisguiseType.HUSK);

    public static final MonsterTemplate VINDICATOR_SAPPER = new MonsterTemplate("vindicator_sapper", EntityType.VINDICATOR, DisguiseType.VINDICATOR);

    public static final MonsterTemplate VINDICATOR_EVAPORATE = new MonsterTemplate("vindicator_evaporate", EntityType.VINDICATOR, DisguiseType.VINDICATOR);

    // nether set
    public static final MonsterTemplate ZOGLIN_MELT = new MonsterTemplate("zoglin_melt", EntityType.ZOGLIN, DisguiseType.ZOGLIN);
    public static final MonsterTemplate CREEPER_SPEW_TNT = new MonsterTemplate("creeper_spew_tnt", EntityType.CREEPER, DisguiseType.CREEPER);
    public static final MonsterTemplate BLAZE_SPEW_FIRE = new MonsterTemplate("blaze_spew_fire", EntityType.BLAZE, DisguiseType.BLAZE);
    public static final MonsterTemplate HOGLIN_SPEW_TNT = new MonsterTemplate("hoglin_spew_tnt", EntityType.HOGLIN, DisguiseType.HOGLIN);
    public static final MonsterTemplate GHAST_MOUNT = new MonsterTemplate("ghast_mount", EntityType.GHAST, DisguiseType.GHAST);
    public static final MonsterTemplate GHAST_SPEW_TNT = new MonsterTemplate("ghast_spew_tnt", EntityType.GHAST, DisguiseType.GHAST);
    public static final MonsterTemplate MAGMA_CUBE_GROW = new MonsterTemplate("magma_cube_grow", EntityType.MAGMA_CUBE, DisguiseType.MAGMA_CUBE);

    static {
        PILLAGER_BUFF_SPEED.setHelmet(new ItemStack(Material.BLUE_BANNER));
        PILLAGER_BUFF_SPEED.setMainHand(new ItemStack(Material.CROSSBOW, 1));
        PILLAGER_BUFF_SPEED.setAbilityName(SpeedBuffAbility.name);
        PILLAGER_BUFF_SPEED.setAbilityCooldown(20 * 20);
        PILLAGER_BUFF_SPEED.setAbilityCount(10);

        PILLAGER_BUFF_STRENGTH.setHelmet(new ItemStack(Material.RED_BANNER));
        PILLAGER_BUFF_STRENGTH.setMainHand(new ItemStack(Material.CROSSBOW, 1));
        PILLAGER_BUFF_STRENGTH.setAbilityName(StrengthBuffAbility.name);
        PILLAGER_BUFF_STRENGTH.setAbilityCooldown(20 * 20);
        PILLAGER_BUFF_STRENGTH.setAbilityCount(10);

        VINDICATOR_SAPPER.setHelmet(new ItemStack(Material.YELLOW_BANNER));
        VINDICATOR_SAPPER.setMainHand(new ItemStack(Material.WOODEN_PICKAXE));
        VINDICATOR_SAPPER.setAbilityName(SapperAbility.name);
        VINDICATOR_SAPPER.setAbilityCooldown(20 * 20);
        VINDICATOR_SAPPER.setAbilityCount(10);

        VINDICATOR_EVAPORATE.setHelmet(new ItemStack(Material.CYAN_BANNER));
        VINDICATOR_EVAPORATE.setMainHand(new ItemStack(Material.BLAZE_POWDER));
        VINDICATOR_EVAPORATE.setAbilityName(EvaporateAbility.name);
        VINDICATOR_EVAPORATE.setAbilityCooldown(20 * 20);
        VINDICATOR_EVAPORATE.setAbilityCount(10);

        CREEPER_IMPLODE.setAbilityName(ImplodeAbility.name);
        CREEPER_IMPLODE.setAbilityCooldown(20 * 15);
        CREEPER_IMPLODE.setAbilityCount(1);
        CREEPER_IMPLODE.setGlowColor(ChatColor.GREEN);

        HUSK_SPEW_SAND.setAbilityName(SpewSandAbility.name);
        HUSK_SPEW_SAND.setAbilityCooldown(20 * 5);
        HUSK_SPEW_SAND.setAbilityCount(10);
        HUSK_SPEW_SAND.setGlowColor(ChatColor.YELLOW);

        ZOGLIN_MELT.setAbilityName(MeltAbility.name);
        ZOGLIN_MELT.setAbilityCooldown(20 * 10);
        ZOGLIN_MELT.setAbilityCount(10);
        ZOGLIN_MELT.setGlowColor(ChatColor.RED);

        EVOKER_FIREBALL.setAbilityName(FireballAbility.name);
        EVOKER_FIREBALL.setHelmet(new ItemStack(Material.RED_BANNER));

        WITCH_BUFF_REGEN.setAbilityName(RegenBuff.name);
        WITCH_BUFF_REGEN.setHelmet(new ItemStack(Material.PINK_BANNER));
        WITCH_BUFF_REGEN.setAbilityCooldown(20 * 20);
        WITCH_BUFF_REGEN.setAbilityCount(5);

        CREEPER_SPEW_TNT.setAbilityName(SpewTntAbility.name);
        CREEPER_SPEW_TNT.setAbilityCooldown(20 * 10);
        CREEPER_SPEW_TNT.setAbilityCount(10);

        BLAZE_SPEW_FIRE.setAbilityName(SpewFireAbility.name);
        BLAZE_SPEW_FIRE.setAbilityCooldown(20 * 6);
        BLAZE_SPEW_FIRE.setAbilityCount(30);

        HOGLIN_SPEW_TNT.setAbilityName(SpewTntAbility.name);
        HOGLIN_SPEW_TNT.setAbilityCooldown(20 * 10);
        HOGLIN_SPEW_TNT.setAbilityCount(10);

        GHAST_MOUNT.setAbilityName(MountAbility.name);
        GHAST_MOUNT.setAbilityCooldown(20 * 5);
        GHAST_MOUNT.setAbilityCount(3);

        GHAST_SPEW_TNT.setAbilityName(SpewTntAbility.name);
        GHAST_SPEW_TNT.setAbilityCooldown(20 * 20);
        GHAST_SPEW_TNT.setAbilityCount(5);

        MAGMA_CUBE_GROW.setAbilityName(GrowSizeAbility.name);
        MAGMA_CUBE_GROW.setAbilityCooldown(10 * 20);
        MAGMA_CUBE_GROW.setAbilityCount(15);

    }

}
