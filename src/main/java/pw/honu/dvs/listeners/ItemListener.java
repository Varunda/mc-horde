package pw.honu.dvs.listeners;

import com.destroystokyo.paper.MaterialTags;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import pw.honu.dvs.DvS;
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
import java.util.Collection;

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

    @EventHandler
    public void hoeItemHandler(PlayerInteractEvent ev) {
        if (MatchManager.instance.getMatchState() != MatchState.GATHERING) {
            return;
        }

        if (ev.getItem() == null) { return; }
        if (ev.getClickedBlock() == null) { return; }
        if (ev.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }

        Material itemType = ev.getItem().getType();

        // not a hoe
        if (itemType != Material.WOODEN_HOE && itemType != Material.STONE_HOE
                && itemType != Material.IRON_HOE && itemType != Material.GOLDEN_HOE
                && itemType != Material.DIAMOND_HOE && itemType != Material.NETHERITE_HOE) {
            return;
        }

        final Block b = ev.getClickedBlock();
        BlockData data = b.getBlockData();

        if (!(data instanceof Ageable)) {
            return;
        }

        final Ageable crop = (Ageable) data;
        if (crop.getAge() >= crop.getMaximumAge()) {
            // a crop that was fully grown was right clicked, drop the drops and set the age back to the minimum
            Collection<ItemStack> drops = b.getDrops();

            crop.setAge(0);
            b.setBlockData(crop);

            for (ItemStack item : drops) {
                b.getWorld().dropItem(b.getLocation().toCenterLocation(), item);
            }
        } else {
            int previousAge = crop.getAge();

            // a crop that was not fully grown was right clicked, apply bonemeal and take one durability away from the hoe
            if (b.applyBoneMeal(ev.getBlockFace())) {
                final EquipmentSlot hand = ev.getHand();
                final Player player = ev.getPlayer();
                final ItemStack item = ev.getItem();

                // have to get the block data again to see the bonemeal taking effect
                BlockData data2 = b.getBlockData();
                if (!(data2 instanceof Ageable)) {
                    DvS.instance.getLogger().severe("block is not Ageable after 1 tick?");
                    return;
                }

                Ageable crop2 = (Ageable) data2;
                int afterAge = crop2.getAge();

                ItemMeta meta = item.getItemMeta();
                if (meta == null) {
                    DvS.instance.getLogger().warning("Missing item meta in hoeItemHandler");
                    return;
                }

                if (!(meta instanceof Damageable)) {
                    DvS.instance.getLogger().info("ItemType " + itemType + " is not damageable?");
                    return;
                }

                Damageable d = (Damageable) meta;
                d.setDamage(d.getDamage() + Math.max(1, (afterAge - previousAge)));
                item.setItemMeta(d);

                if (d.getDamage() >= item.getType().getMaxDurability()) {
                    if (hand == EquipmentSlot.HAND) {
                        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    } else if (hand == EquipmentSlot.OFF_HAND) {
                        player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                    } else {
                        DvS.instance.getLogger().warning("hoeItemHandler> cannot remove hoe item: unchecked hand " + hand);
                    }

                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                }
            }
        }

        ev.setCancelled(true);
    }

}
