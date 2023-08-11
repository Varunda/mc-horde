package pw.honu.dvs.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.managers.MatchManager;

public class BlockItemDropListener implements Listener {

    @EventHandler
    public void itemDropEvent(BlockDropItemEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.GATHERING) {
            return;
        }

        if (!MatchManager.instance.getRunningMap().getMap().getSettings().getIncreaseOreDrops()) {
            return;
        }

        Material type = event.getBlockState().getType();
        if (type == Material.IRON_ORE || type == Material.DEEPSLATE_IRON_ORE
            || type == Material.GOLD_ORE || type == Material.DEEPSLATE_GOLD_ORE
            || type == Material.COAL_ORE || type == Material.DEEPSLATE_COAL_ORE
            || type == Material.COPPER_ORE || type == Material.DEEPSLATE_COPPER_ORE
            || type == Material.DIAMOND_ORE || type == Material.DEEPSLATE_DIAMOND_ORE
            || type == Material.EMERALD_ORE || type == Material.DEEPSLATE_EMERALD_ORE
            || type == Material.REDSTONE_ORE || type == Material.DEEPSLATE_REDSTONE_ORE
            || type == Material.LAPIS_ORE || type == Material.DEEPSLATE_LAPIS_ORE) {

            for (Item i : event.getItems()) {
                i.getItemStack().setAmount(i.getItemStack().getAmount() * 3);
            }
        }

    }

}
