package pw.honu.dvs.item;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.DvS;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.ability.*;
import pw.honu.dvs.managers.AbilityManager;

import java.util.ArrayList;
import java.util.List;

public class AbilityItem {

    private boolean consumable;

    private @NotNull String abilityName;

    private @NotNull Material itemMaterial;

    public AbilityItem(boolean consumable, @NotNull String abilityName, @NotNull Material itemMaterial) {
        this.consumable = consumable;
        this.abilityName = abilityName;
        this.itemMaterial = itemMaterial;
    }

    // Dev items
    public static final AbilityItem THROW_ITEM = new AbilityItem(true, ThrowAbility.name, Material.CLAY_BALL);
    public static final AbilityItem LAUNCHER_ITEM = new AbilityItem(false, LaunchAbility.name, Material.SUGAR);
    public static final AbilityItem PATHFIND_ITEM = new AbilityItem(false, PathfindAbility.name, Material.PRISMARINE_SHARD);
    public static final AbilityItem TARGET_ITEM = new AbilityItem(false, TargetAbility.name, Material.BLAZE_ROD);

    public static final AbilityItem SPAWN_ZOMBIE = new AbilityItem(false, SpawnZombieAbility.name, Material.ROTTEN_FLESH);
    public static final AbilityItem SPAWN_HUSK = new AbilityItem(false, SpawnHuskAbility.name, Material.HEART_OF_THE_SEA);
    public static final AbilityItem SPAWN_DROWNED = new AbilityItem(false, SpawnDrownedAbility.name, Material.TRIDENT);
    public static final AbilityItem SPAWN_CREEPER = new AbilityItem(false, SpawnCreeperAbility.name, Material.GUNPOWDER);
    public static final AbilityItem SPAWN_PIGLIN_ITEM = new AbilityItem(false, SpawnPiglinAbility.name, Material.NAUTILUS_SHELL);
    public static final AbilityItem SPAWN_PIGLIN_ARCHER_ITEM = new AbilityItem(false, SpawnPiglinArcherAbility.name, Material.COPPER_INGOT);

    // Player items
    public static final AbilityItem PATHFIND_NEAR_ITEM = new AbilityItem(false, PathfindNearAbility.name, Material.COMPASS);
    public static final AbilityItem TARGET_NEAR_ITEM = new AbilityItem(false, TargetNearAbility.name, Material.BLAZE_ROD);
    public static final AbilityItem GATHER_ITEM = new AbilityItem(false, GatherAbility.name, Material.HORN_CORAL);

    // NBT stuff
    public static final NamespacedKey ABILITY_ITEM_NAME_KEY = new NamespacedKey(DvS.instance, "ability-name");
    public static final NamespacedKey ABILITY_ITEM_CONSUMABLE_KEY = new NamespacedKey(DvS.instance, "ability-consumable");

    public static ItemStack create(AbilityItem item, int count) {
        ItemStack stack = new ItemStack(item.itemMaterial, count);

        Ability ability = AbilityManager.instance.get(item.getAbilityName());
        if (ability == null) {
            DvSLogger.error("Failed to find ability " + item.getAbilityName());
        } else {
            AbilityItem.attachAbilityData(ability, stack, item.isConsumable());
        }

        return stack;
    }

    public static ItemStack attachAbilityData(Ability ability, ItemStack item, boolean consumable) {
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(ChatColor.WHITE + "Use " + ability.name()));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Left click - Description"));
        lore.add(Component.text("Right click - Cast " + (consumable ? " (CONSUMABLE)" : "")));
        meta.lore(lore);

        meta.addEnchant(Enchantment.DURABILITY, 10, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);

        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(ABILITY_ITEM_NAME_KEY, PersistentDataType.STRING, ability.name());
        data.set(ABILITY_ITEM_CONSUMABLE_KEY, PersistentDataType.INTEGER, consumable ? 1 : 0);

        item.setItemMeta(meta);

        return item;
    }

    @NotNull public Material getItemMaterial() {
        return itemMaterial;
    }

    @NotNull public String getAbilityName() {
        return abilityName;
    }

    public boolean isConsumable() {
        return consumable;
    }

}
