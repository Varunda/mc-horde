package pw.honu.dvs.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.PlayerState;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.PlayerManager;

public class InventoryListener implements Listener {

    @EventHandler
    public void inventoryClick(InventoryClickEvent ev) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        HumanEntity who = ev.getWhoClicked();

        if (PlayerManager.instance.getPlayer(who.getUniqueId()) != PlayerState.MONSTER) {
            return;
        }

        if (ev.getSlotType() == InventoryType.SlotType.ARMOR) {
            ev.setCancelled(true);
        }
    }

}
