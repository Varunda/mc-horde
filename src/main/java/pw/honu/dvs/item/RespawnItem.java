package pw.honu.dvs.item;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.DvS;
import pw.honu.dvs.DvSLogger;

import java.util.ArrayList;
import java.util.List;

public class RespawnItem {

    private @NotNull String templateName;

    private @NotNull Material itemMaterial;

    private @NotNull ChatColor tooltipColor;

    public RespawnItem(@NotNull String templateName, @NotNull Material itemMaterial, @NotNull ChatColor tooltipColor) {
        this.templateName = templateName;
        this.itemMaterial = itemMaterial;
        this.tooltipColor = tooltipColor;
    }

    public static final RespawnItem RESPAWN_BASIC_MELEE = new RespawnItem("player_monster_melee", Material.IRON_SWORD, ChatColor.GRAY);
    public static final RespawnItem RESPAWN_BASIC_RANGED = new RespawnItem("player_monster_ranged", Material.BOW, ChatColor.BLUE);
    public static final RespawnItem RESPAWN_SAPPER = new RespawnItem("player_monster_sapper", Material.WOODEN_PICKAXE, ChatColor.DARK_RED);
    public static final RespawnItem RESPAWN_CONTROLLER = new RespawnItem("player_monster_controller", Material.BONE, ChatColor.GOLD);

    public static final NamespacedKey RESPAWN_ITEM_TEMPLATE_NAME_KEY = new NamespacedKey(DvS.instance, "is-respawn-item");

    /**
     * Give the respawn items to a player
     * @param p Player to give the items to
     */
    public static void giveRespawnItems(Player p) {
        DvSLogger.info("Giving respawn items to " + p.getName());

        p.getInventory().addItem(
                RespawnItem.create(RespawnItem.RESPAWN_BASIC_MELEE),
                RespawnItem.create(RespawnItem.RESPAWN_BASIC_RANGED),
                RespawnItem.create(RespawnItem.RESPAWN_SAPPER)
                //RespawnItem.create(RespawnItem.RESPAWN_CONTROLLER)
        );
    }

    /**
     * Create an ItemStack that represents the respawn item
     * @param item Respawn item to generate the ItemStack for
     * @return An ItemStack
     */
    public static ItemStack create(@NotNull RespawnItem item) {
        ItemStack stack = new ItemStack(item.itemMaterial, 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(Component.text(ChatColor.WHITE + "Respawn as a " + item.tooltipColor + item.templateName));

        meta.addEnchant(Enchantment.DURABILITY, 10, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Left click - Preview items"));
        lore.add(Component.text("Right click - Spawn as this monster"));
        meta.lore(lore);

        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(RESPAWN_ITEM_TEMPLATE_NAME_KEY, PersistentDataType.STRING, item.getTemplateName());

        stack.setItemMeta(meta);

        return stack;
    }

    public @NotNull String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(@NotNull String templateName) {
        this.templateName = templateName;
    }

    public @NotNull Material getItemMaterial() {
        return itemMaterial;
    }

    public void setItemMaterial(@NotNull Material material) {
        itemMaterial = material;
    }

}
