package pw.honu.dvs.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import pw.honu.dvs.DvS;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.managers.MatchManager;

public class PlayerInteractEntityListener implements Listener {

    @EventHandler
    public void playerEntityInteract(PlayerInteractEntityEvent ev) {
        if (MatchManager.instance.getMatchState() != MatchState.GATHERING) {
            return;
        }

        Entity target = ev.getRightClicked();

        if (!(target instanceof Animals)) {
            return;
        }

        Animals animal = (Animals) target;

        if (animal.isAdult()) {
            return;
        }

        ItemStack held = null;
        if (ev.getHand() == EquipmentSlot.HAND) {
            held = ev.getPlayer().getEquipment().getItemInMainHand();
        } else if (ev.getHand() == EquipmentSlot.OFF_HAND) {
            held = ev.getPlayer().getEquipment().getItemInOffHand();
        } else {
            DvS.instance.getLogger().warning("Unchecked hand used in player entity interact: " + ev.getHand());
            return;

        }

        boolean isBreedItem = animal.isBreedItem(held);
        if (!isBreedItem) {
            return;
        }

        animal.setAdult();

        if (held.getAmount() == 1) {
            held.setType(Material.AIR);
        } else {
            held.setAmount(held.getAmount() - 1);
        }
    }

}
