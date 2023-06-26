package pw.honu.dvs.monster;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.managers.MonsterManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MonsterTemplate {

    private @NotNull String name;
    private @NotNull EntityType entityType;
    private @NotNull DisguiseType disguiseType;

    private @Nullable ItemStack mainHand;
    private @Nullable ItemStack offHand;
    private @Nullable ItemStack helmet;
    private @Nullable ItemStack chest;
    private @Nullable ItemStack legs;
    private @Nullable ItemStack boots;

    private @NotNull List<ItemStack> inventory;

    private @Nullable String abilityName;
    private int abilityCooldown;
    private int abilityCount;
    private ChatColor glowColor;

    public MonsterTemplate(@NotNull String name, @NotNull EntityType type, @NotNull DisguiseType disguiseType) {
        this.name = name;
        this.entityType = type;
        this.disguiseType = disguiseType;
        inventory = new ArrayList<>();
        MonsterManager.instance.add(this);
    }

    public @NotNull List<ItemStack> getInventory() {
        return inventory;
    }

    public void addInventoryItem(@NotNull ItemStack item) {
        inventory.add(item);
    }

    @NotNull public String getName() {
        return name;
    }

    @NotNull public EntityType getEntityType() {
        return entityType;
    }

    public @Nullable ItemStack getMainHand() {
        return mainHand;
    }

    public void setMainHand(@NotNull ItemStack mainHand) {
        this.mainHand = mainHand;
    }

    public @Nullable ItemStack getOffHand() {
        return offHand;
    }

    public void setOffHand(@NotNull ItemStack offHand) {
        this.offHand = offHand;
    }

    public @Nullable ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(@NotNull ItemStack helmet) {
        this.helmet = helmet;
    }

    public @Nullable ItemStack getChest() {
        return chest;
    }

    public void setChest(@NotNull ItemStack chest) {
        this.chest = chest;
    }

    public @Nullable ItemStack getLegs() {
        return legs;
    }

    public void setLegs(@NotNull ItemStack legs) {
        this.legs = legs;
    }

    public @Nullable ItemStack getBoots() {
        return boots;
    }

    public void setBoots(@NotNull ItemStack boots) {
        this.boots = boots;
    }

    @NotNull public DisguiseType getDisguiseType() {
        return disguiseType;
    }

    @Nullable
    public String getAbilityName() {
        return abilityName;
    }

    public void setAbilityName(@Nullable String abilityName) {
        this.abilityName = abilityName;
    }

    public int getAbilityCooldown() {
        return abilityCooldown;
    }

    public void setAbilityCooldown(int abilityCooldown) {
        this.abilityCooldown = abilityCooldown;
    }

    public int getAbilityCount() {
        return abilityCount;
    }

    public void setAbilityCount(int abilityCount) {
        this.abilityCount = abilityCount;
    }

    public void setGlowColor(ChatColor value) {
        this.glowColor = value;
    }

    public ChatColor getGlowColor() {
        return glowColor;
    }
}
