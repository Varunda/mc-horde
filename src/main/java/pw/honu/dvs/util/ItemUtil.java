package pw.honu.dvs.util;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import pw.honu.dvs.DvS;

public class ItemUtil {

    /**
     * Create a colored leather helmet
     * @param color Color to set the helmet to
     * @return a new ItemStack colored with the color passed
     */
    public static ItemStack getColoredLeatherHelmet(Color color) {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        if (helmet.getItemMeta() instanceof LeatherArmorMeta meta) {
            meta.setColor(color);
            helmet.setItemMeta(meta);
        }

        return helmet;
    }

    /**
     * Perform a relative change in durability of the item, and break the item if the durability if the item goes below 0.
     * @param player Player who owns the item being changed
     * @param hand Which hand the item is in
     * @param item Item being modified
     * @param damage The relative damage done to the item
     */
    public static void changeDurabilityAndMaybeBreak(Player player, EquipmentSlot hand, ItemStack item, int damage) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        if (!(meta instanceof Damageable)) {
            return;
        }

        Damageable d = (Damageable) meta;
        d.setDamage(d.getDamage() + damage);
        item.setItemMeta(d);

        if (d.getDamage() >= item.getType().getMaxDurability()) {
            if (hand == EquipmentSlot.HAND) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            } else if (hand == EquipmentSlot.OFF_HAND) {
                player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            } else {
                DvS.instance.getLogger().warning("cannot remove item: unchecked EquipmentSlot " + hand);
            }

            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
        }

    }

}
