package pw.honu.dvs.listeners;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import pw.honu.dvs.DvS;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.PlayerState;
import pw.honu.dvs.managers.DisguiseManager;
import pw.honu.dvs.managers.MatchManager;
import pw.honu.dvs.managers.PlayerManager;
import pw.honu.dvs.managers.PlayerStatsManager;
import pw.honu.dvs.util.TitleUtil;

import java.time.Duration;

public class PlayerDeathListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void playerDamage(EntityDamageEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL
                    || event.getCause() == EntityDamageEvent.DamageCause.CRAMMING
                    || event.getCause() == EntityDamageEvent.DamageCause.DROWNING
                    || event.getCause() == EntityDamageEvent.DamageCause.DRYOUT) {

                event.setCancelled(true);
            }
            return;
        }

        final Player p = (Player) event.getEntity();
        final PlayerState state = PlayerManager.instance.getPlayer(p.getUniqueId());

        if (state == PlayerState.MONSTER && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void entityDeath(PlayerDeathEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        final Player p = event.getPlayer();

        final Component comp = event.deathMessage();

        event.setCancelled(true);

        final PlayerState state = PlayerManager.instance.getPlayer(p.getUniqueId());
        DvS.instance.getLogger().info(p.getName() + " died!");

        p.setGameMode(GameMode.SPECTATOR);
        PlayerManager.instance.setPlayer(p.getUniqueId(), PlayerState.RESPAWNING);

        if (state == PlayerState.ALIVE) {
            // drop the players inventory cause they aren't dying
            PlayerInventory inv = p.getInventory();
            ItemStack[] items = inv.getContents();
            if (items != null) {
                for (ItemStack i : items) {
                    if (i != null) {
                        p.getLocation().getWorld().dropItem(p.getLocation(), i);
                    }
                }
            }
            inv.clear();

            if (comp != null) {
                Bukkit.broadcast(comp);
            }

            // announce when a player dies, except if there are now 0 left
            int playersLeft = PlayerManager.instance.getAlive().size();
            if (playersLeft == 0) {
                MatchManager.instance.endMatch();
            } else {
                TitleUtil.title(
                        Title.title(
                                Component.text(""),
                                Component.text(ChatColor.RED + p.getName() + " died!"),
                                Title.Times.times(
                                        Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)
                                )
                        )
                );

                int kills = PlayerStatsManager.instance.getKills(p.getUniqueId());
                if (kills > 0) {
                    p.sendMessage(ChatColor.WHITE + "Your rampage lasted " + ChatColor.RED + kills + " kills");
                }
            }
        } else if (state == PlayerState.MONSTER) {
            // if the player was disguised as a mob, it's somewhat confusing to have the thing you were attacking just disappear
            // so get the disguise type of the player who died, and spawn an entity with the matching EntityType
            // to kill so the player gets feedback that they kill something
            Location deathSpot = p.getLocation();
            MobDisguise type = DisguiseManager.instance.getDisguise(p);
            if (type != null) {
                type.stopDisguise();

                EntityType disguiseType = type.getType().getEntityType();
                DvS.instance.getLogger().info("Spawning a " + disguiseType + " where " + p.getName() + " died");

                Entity dier = deathSpot.getWorld().spawnEntity(deathSpot, disguiseType);
                if (dier instanceof LivingEntity) {
                    LivingEntity le = (LivingEntity) dier;

                    EntityEquipment equip = le.getEquipment();
                    if (equip != null) {
                        EntityEquipment playerEquip = p.getEquipment();
                        equip.setItemInMainHand(playerEquip.getItemInMainHand());
                        equip.setItemInOffHand(playerEquip.getItemInOffHand());
                        equip.setHelmet(playerEquip.getHelmet());
                        equip.setChestplate(playerEquip.getChestplate());
                        equip.setLeggings(playerEquip.getLeggings());
                        equip.setBoots(playerEquip.getBoots());
                    }

                    le.setHealth(0);
                } else {
                    dier.remove();
                }
            }

            if (p.getKiller() != null) {
                PlayerManager.instance.giveRampage(p.getKiller());
                PlayerStatsManager.instance.incrementKills(p);
            }
        }

        // after 5 seconds send the player to the respawn
        new BukkitRunnable() {
            @Override
            public void run() {
                if (MatchManager.instance.getMatchState() == MatchState.RUNNING) {
                    PlayerManager.instance.respawnToMonsterLobby(p.getUniqueId());
                }
            }
        }.runTaskLater(DvS.instance, 20 * 5);
    }

}
