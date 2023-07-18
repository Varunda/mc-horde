package pw.honu.dvs.managers;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.honu.dvs.DvS;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.PlayerState;
import pw.honu.dvs.item.RespawnItem;
import pw.honu.dvs.monster.MonsterTemplate;

import java.util.*;

public class PlayerManager {

    static {
        instance = new PlayerManager();
    }

    public static PlayerManager instance;

    private static Random _random = new Random();

    private Map<UUID, PlayerState> players = new HashMap<>();

    private Scoreboard playerState;
    private Objective playerObjective;

    public PlayerManager() {
        this.playerState = Bukkit.getScoreboardManager().getNewScoreboard();
        this.playerObjective = this.playerState.registerNewObjective("state", Criteria.DUMMY, Component.text("State"));
        this.playerObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Get the state of a specific player
     */
    public @Nullable PlayerState getPlayer(@NotNull UUID uuid) {
        return players.get(uuid);
    }

    /**
     * Set the player state
     */
    public void setPlayer(@NotNull UUID uuid, @NotNull PlayerState state) {
        @Nullable Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            p.sendMessage("Your player state was updated to " + state);
        }

        if (players.get(uuid) == state) {
            return;
        }

        players.put(uuid, state);

        ScoreboardManager.instance.update();
    }

    public boolean sendToPlayerSpawn(@NotNull Player player) {
        if (LocationManager.instance.getPlayerStart() == null) {
            DvSLogger.warn("Cannot send player to player spawn: player spawn is not set");
            return false;
        }

        if (!player.isOnline()) {
            DvSLogger.warn("Cannot send player to player spawn: player is not online");
            return false;
        }

        player.teleport(LocationManager.instance.getPlayerStart());

        return true;
    }

    /**
     * Respawn a player to the monster lobby, where they can pick which monster they'd like to spawn at next
     * @param playerID UUID of the player
     * @return boolean If the player was successfully respawned to the monster lobby. If false, console has a warning
     */
    public boolean respawnToMonsterLobby(@NotNull UUID playerID) {
        Player p = Bukkit.getPlayer(playerID);
        if (p == null) {
            DvSLogger.warn("Cannot respawn " + playerID + " to monster lobby: failed to find player");
            return false;
        }

        if (!p.isOnline()) {
            DvSLogger.warn("Cannot respawn " + playerID + "to monster lobby: player is offline");
            return false;
        }

        p.sendMessage(ChatColor.DARK_PURPLE + "Left click to view items. Right click to select");

        PlayerManager.instance.setPlayer(p.getUniqueId(), PlayerState.RESPAWNING);
        if (LocationManager.instance.getMonsterLobby() != null) {
            p.teleport(LocationManager.instance.getMonsterLobby());
        }

        p.setHealth(20);
        p.setFoodLevel(20);
        p.setSaturation(20);
        for (PotionEffect pe : p.getActivePotionEffects()) {
            p.removePotionEffect(pe.getType());
        }
        p.setGameMode(GameMode.ADVENTURE);
        p.getInventory().clear();

        RespawnItem.giveRespawnItems(p);

        return true;
    }

    /**
     * Respawn a player to the monster spawn, using a monster template as the items they respawn with
     * @param playerID UUID of the player
     * @param template Template used to spawn the player with
     * @return False if the player could not be respawned for some reason (which is logged), else true
     */
    public boolean respawnToMonsterSpawn(@NotNull UUID playerID, @NotNull MonsterTemplate template) {
        Player p = Bukkit.getPlayer(playerID);
        if (p == null) {
            DvSLogger.warn("Cannot respawn " + playerID + " to monster spawn: failed to find player");
            return false;
        }

        if (!p.isOnline()) {
            DvSLogger.warn("Cannot respawn " + playerID + "to monster spawn: player is offline");
            return false;
        }

        if (LocationManager.instance.getMonsterSpawn() == null) {
            DvSLogger.warn("Cannot respawn " + playerID + " to monster spawn: monster spawn is null");
            return false;
        }

        PlayerManager.instance.setPlayer(playerID, PlayerState.MONSTER);

        DisguiseManager.instance.setDisguise(p, template.getDisguiseType());

        p.getEquipment().clear();
        MonsterManager.instance.setInventory(p, template);
        p.teleport(LocationManager.instance.getMonsterSpawn());
        p.setGameMode(GameMode.SURVIVAL);

        p.sendMessage("You spawned in with the " + ChatColor.DARK_PURPLE + template.getName() + ChatColor.RESET + " monster template");

        return true;
    }

    public void giveRampage(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 3, 100));
        World playerWorld = p.getLocation().getWorld();
        playerWorld.spawnParticle(Particle.WAX_OFF, p.getLocation(), 50, 0.5, 0.5, 0.5);
    }

    /**
     * Get a random alive player, or null if none could be found
     * @return
     */
    public @Nullable Player getRandomAlive() {
        List<Player> players = new ArrayList<>();

        for (UUID id : getAlive()) {
            Player p = Bukkit.getPlayer(id);

            if (p != null && p.isOnline() && p.getGameMode() == GameMode.SURVIVAL) {
                players.add(p);
            }
        }

        if (players.size() == 0) {
            return null;
        }

        return players.get(_random.nextInt(players.size()));
    }

    /**
     * Get all players that are currently alive. Once they die, they become a respawning player, then a monster player
     */
    public List<UUID> getAlive() {
        return getWithState(PlayerState.ALIVE);
    }

    /**
     * Get all players that are currently in respawn, they will become a monster player in a bit
     */
    public List<UUID> getRespawning() {
        return getWithState(PlayerState.RESPAWNING);
    }

    /**
     * Get all players that are monsters. When killed, they go to the respawning state
     */
    public List<UUID> getMonsters() {
        return getWithState(PlayerState.MONSTER);
    }

    private List<UUID> getWithState(PlayerState state) {
        return players.entrySet().stream()
                .filter(kvp -> kvp.getValue() == state)
                .map(Map.Entry::getKey)
                .toList();
    }

}
