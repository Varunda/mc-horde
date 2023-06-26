package pw.honu.dvs.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.PlayerState;
import pw.honu.dvs.ability.Ability;
import pw.honu.dvs.item.AbilityItem;
import pw.honu.dvs.item.RespawnItem;
import pw.honu.dvs.managers.AbilityManager;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.MonsterManager;
import pw.honu.dvs.managers.PlayerManager;
import pw.honu.dvs.monster.MonsterTemplate;

import javax.annotation.Nullable;

public class ItemListener implements Listener {

    @EventHandler
    public void blockItemDrop(BlockDropItemEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        if (PlayerManager.instance.getPlayer(event.getPlayer().getUniqueId()) != PlayerState.ALIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void itemPickup(PlayerAttemptPickupItemEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        if (PlayerManager.instance.getPlayer(event.getPlayer().getUniqueId()) != PlayerState.ALIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void itemDrop(PlayerDropItemEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        if (PlayerManager.instance.getPlayer(event.getPlayer().getUniqueId()) != PlayerState.ALIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void itemUseEvent(PlayerInteractEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        PlayerState state = PlayerManager.instance.getPlayer(event.getPlayer().getUniqueId());
        if (state != PlayerState.RESPAWNING) {
            return;
        }

        @Nullable ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            DvSLogger.warn("No meta for player interaction");
            return;
        }

        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (!data.has(RespawnItem.RESPAWN_ITEM_TEMPLATE_NAME_KEY)) {
            return;
        }

        String templateName = data.get(RespawnItem.RESPAWN_ITEM_TEMPLATE_NAME_KEY, PersistentDataType.STRING);
        if (templateName == null) {
            DvSLogger.warn("persistent data was there, but lacked the template name key");
            return;
        }

        @Nullable MonsterTemplate template = MonsterManager.instance.get(templateName);
        if (template == null) {
            DvSLogger.error("Failed to find template with name '" + templateName + "'");
            return;
        }

        Action action = event.getAction();

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            MonsterManager.instance.setInventory(event.getPlayer(), template);
            RespawnItem.giveRespawnItems(event.getPlayer());
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            PlayerManager.instance.respawnToMonsterSpawn(event.getPlayer().getUniqueId(), template);
        } else {
            DvSLogger.warn("Unchecked action in ItemListener: " + action);
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void abilityItemHandler(PlayerInteractEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING && !event.getPlayer().isOp()) {
            return;
        }

        @Nullable ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        Material itemMaterial = item.getType();
        if (itemMaterial == Material.WATER_BUCKET || itemMaterial == Material.LAVA_BUCKET) {
            event.setCancelled(true);
            return;
        }

        @Nullable ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (!data.has(AbilityItem.ABILITY_ITEM_NAME_KEY)) {
            return;
        }

        String abilityName = data.get(AbilityItem.ABILITY_ITEM_NAME_KEY, PersistentDataType.STRING);
        if (abilityName == null) {
            DvSLogger.warn("persistent data was there, but lacked the ability name key");
            return;
        }

        Integer conusmableInt = data.get(AbilityItem.ABILITY_ITEM_CONSUMABLE_KEY, PersistentDataType.INTEGER);
        if (conusmableInt == null) {
            DvSLogger.warn("persistent data was there, but lacked the consumable key");
            return;
        }
        boolean consumable = conusmableInt == 1;

        @Nullable Ability ability = AbilityManager.instance.get(abilityName);
        if (ability == null) {
            DvSLogger.error("Failed to find ability with name '" + abilityName + "'");
            return;
        }

        Action action = event.getAction();
        Player p = event.getPlayer();

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            p.sendMessage(ability.description());
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ability.execute(p);
            if (consumable) {
                int count = event.getItem().getAmount();
                if (count == 1) {
                    event.getPlayer().getInventory().setItemInMainHand(null);
                } else {
                    event.getItem().setAmount(count - 1);
                }
            }
        } else {
            DvSLogger.warn("Unchecked action in ItemListener: " + action);
        }

        event.setCancelled(true);

    }


}
