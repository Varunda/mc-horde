package pw.honu.dvs.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.*;
import pw.honu.dvs.item.AbilityItem;
import pw.honu.dvs.map.HordeMap;
import pw.honu.dvs.map.RunningHordeMap;
import pw.honu.dvs.util.LocationUtil;
import pw.honu.dvs.util.TitleUtil;
import pw.honu.dvs.util.WorldUtil;
import pw.honu.dvs.wave.Wave;
import pw.honu.dvs.wave.WaveEntry;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MatchManager {

    public static MatchManager instance;

    static {
        instance = new MatchManager(DvS.instance);
    }

    private @NotNull MatchState matchState;
    private int monsterCount;
    private @Nullable Chest playerChest;

    private DvS plugin;

    private BukkitTask gatheringCountdownRunnable;
    private BukkitTask monsterTargetTask;
    private BukkitTask waveRepeatTask;

    private RunningHordeMap map;

    private MatchManager(DvS plugin) {
        this.plugin = plugin;
        matchState = MatchState.PRE_GAME;
    }

    /**
     * Spawn a wave in a match
     * @param wave Wave to spawn
     * @return If the wave was spawned or not
     */
    public boolean spawnWave(Wave wave) {
        if (matchState != MatchState.RUNNING) {
            DvSLogger.warn("Cannot spawn wave: match is not RUNNING, is currently " + matchState);
            return false;
        }

        Location monsterSpawn = this.map.getRandomMonsterSpawn();

        int spawnedCount = 0;

        boolean b = false;

        for (WaveEntry entry : wave.getEntries()) {
            if (b) {
                break;
            }

            for (int i = 0; i < entry.getCount(); ++i) {
                if (MonsterManager.instance.spawn(entry.getTemplate(), monsterSpawn)) {
                    ++spawnedCount;
                    setMonsterCount(spawnedCount);
                }

                if (spawnedCount >= 200) {
                    DvSLogger.warn("Ending spawn early: spawned " + spawnedCount + " monster. Capping at 200");
                    b = true;
                    break;
                }
            }
        }

        return true;
    }

    public @NotNull MatchState getMatchState() {
        return matchState;
    }

    public void setMatchState(@NotNull MatchState matchState) {
        this.matchState = matchState;
    }

    /**
     * Start a match
     * @return If the match was successfully started
     */
    public boolean startMatch() {
        if (MapManager.instance.getNextMap() == null) {
            DvS.instance.getLogger().warning("Cannot start match: map is not set");
            return false;
        }

        if (getMatchState() != MatchState.PRE_GAME) {
            DvS.instance.getLogger().warning("State is " + this.getMatchState() + " not " + MatchState.PRE_GAME + ". Might cause unexpected behavior");
        }

        HordeMap map = MapManager.instance.getNextMap();
        assert map != null;

        World gameWorld = setupMatchWorld(map);
        gameWorld.setPVP(false);
        this.map = new RunningHordeMap(map, gameWorld);

        Bukkit.getOnlinePlayers().forEach(iter -> {
            PlayerManager.instance.setPlayer(iter.getUniqueId(), PlayerState.ALIVE);
            BossBarManager.instance.addPlayer(iter);

            if (!iter.isOp()) {
                iter.setGameMode(GameMode.SURVIVAL);
            }
            iter.teleport(this.map.getPlayerStart());
            iter.sendMessage(ChatColor.DARK_PURPLE + "Match started!");

            iter.getInventory().addItem(new ItemStack(Material.IRON_AXE));
            iter.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
            iter.getInventory().addItem(new ItemStack(Material.IRON_SHOVEL));
            iter.getInventory().addItem(new ItemStack(Material.IRON_HOE));
            iter.getInventory().addItem(new ItemStack(Material.OAK_LOG, 8));
        });

        setMatchState(MatchState.GATHERING);

        this.gatheringCountdownRunnable = new GatheringCountdownRunnable(this, map.getSetupTicks()).runTaskTimer(this.plugin, 0, 1);
        ScoreboardManager.instance.update();

        return true;
    }

    /**
     * Create the bukkit World for the match
     * @param map Map info, used to set locations
     * @return a Bukkit World, ready for use
     */
    private World setupMatchWorld(HordeMap map) {
        String matchId = UUID.randomUUID() + "-" + map.getName();

        DvSLogger.info("Cloning world " + map.getName() + " into " + matchId);
        World matchWorld = WorldUtil.cloneWorld(map.getFolder(), matchId);

        Location chestLoc = LocationUtil.create(matchWorld, map.getChest());
        if (matchWorld.getBlockAt(chestLoc).getType() != Material.CHEST) {
            DvS.instance.getLogger().severe("MISSING CHEST AT " + chestLoc.toString() + " from map " + map.getName());
            matchWorld.getBlockAt(chestLoc).setType(Material.CHEST);
        }

        Chest chest = (Chest) matchWorld.getBlockAt(chestLoc).getState();

        MatchManager.instance.setPlayerChest(chest);

        matchWorld.setTime(0);
        matchWorld.setSpawnLocation(map.getPlayerStart().toLocation(matchWorld));
        matchWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
        matchWorld.setGameRule(GameRule.KEEP_INVENTORY, false);
        matchWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        matchWorld.setGameRule(GameRule.BLOCK_EXPLOSION_DROP_DECAY, true);
        matchWorld.setGameRule(GameRule.DO_INSOMNIA, false);
        matchWorld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        matchWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        matchWorld.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        matchWorld.setDifficulty(Difficulty.EASY);

        return matchWorld;
    }

    public void startCombatPhase() {
        if (this.getMatchState() != MatchState.GATHERING) {
            DvS.instance.getLogger().warning("State is " + this.getMatchState() + " not " + MatchState.GATHERING + ". Might cause unexpected behavior");
        }

        assert this.getPlayerChest() != null;

        World gameWorld = this.map.getMatchWorld();
        gameWorld.setTime(18_000);
        gameWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        gameWorld.setGameRule(GameRule.BLOCK_EXPLOSION_DROP_DECAY, true);
        gameWorld.setGameRule(GameRule.DO_INSOMNIA, false);
        gameWorld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        gameWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        gameWorld.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        gameWorld.setPVP(true); // pvp must be on to allow monster players to hit alive players
        gameWorld.setDifficulty(Difficulty.EASY);

        Bukkit.getOnlinePlayers().forEach(iter -> {
            PlayerManager.instance.setPlayer(iter.getUniqueId(), PlayerState.ALIVE);

            if (iter.isOp()) {
                iter.getInventory().addItem(AbilityItem.create(AbilityItem.PATHFIND_ITEM, 1));
                iter.getInventory().addItem(AbilityItem.create(AbilityItem.TARGET_ITEM, 1));
                iter.getInventory().addItem(AbilityItem.create(AbilityItem.LAUNCHER_ITEM, 1));
                iter.getInventory().addItem(AbilityItem.create(AbilityItem.SPAWN_ZOMBIE_HORDE, 1));
                iter.getInventory().addItem(AbilityItem.create(AbilityItem.SPAWN_CREEPER, 1));
                iter.getInventory().addItem(AbilityItem.create(AbilityItem.SPAWN_PIGLIN_ITEM, 1));
                iter.getInventory().addItem(AbilityItem.create(AbilityItem.SPAWN_PIGLIN_ARCHER_ITEM, 1));
            } else {
                assert playerChest != null;
                ItemStack[] items = playerChest.getBlockInventory().getContents();
                if (iter.getPlayer() != null && items != null) {
                    iter.getPlayer().getInventory().setContents(items);
                }
                iter.clearActivePotionEffects();
            }

            iter.teleport(this.map.getPlayerStart());
            iter.sendMessage(ChatColor.DARK_PURPLE + "Combat has begun!");
        });

        this.getPlayerChest().getBlockInventory().clear();

        if (this.gatheringCountdownRunnable != null) {
            this.gatheringCountdownRunnable.cancel();
            this.gatheringCountdownRunnable = null;
        }

        for (Entity e : gameWorld.getEntities()) {
            if (e.getType() == EntityType.DROPPED_ITEM
                || e.getType() == EntityType.ITEM_FRAME
                || e.getType() == EntityType.GLOW_ITEM_FRAME
                || e.getType() == EntityType.ARMOR_STAND) {

                e.remove();
            }
        }

        this.monsterTargetTask = new MonsterTargetRunnable(this).runTaskTimer(DvS.instance, 0, 20);
        this.waveRepeatTask = new WaveRepeatRunnable(this).runTaskTimer(DvS.instance, 0, 20);

        this.setMatchState(MatchState.RUNNING);
        ScoreboardManager.instance.update();
        BossBarManager.instance.setProgress(1d);
    }

    /**
     * End the match. Kills all monsters, cancels and runnables, sets everyone to spectator mode, removes boss bars
     */
    public void endMatch() {
        MonsterManager.instance.killAllMonsters();

        TitleUtil.title(Title.title(
                Component.text("Match ended"),
                Component.text(""),
                Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1)))
        );

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setGameMode(GameMode.SPECTATOR);

            DisguiseManager.instance.removeDisguise(p);
            p.clearActivePotionEffects();
            p.getInventory().clear();
        }

        BossBarManager.instance.removeAll();

        setMatchState(MatchState.POST_GAME);

        if (this.gatheringCountdownRunnable != null) {
            this.gatheringCountdownRunnable.cancel();
            this.gatheringCountdownRunnable = null;
        }

        if (this.monsterTargetTask != null) {
            this.monsterTargetTask.cancel();
            this.monsterTargetTask = null;
        }

        if (this.waveRepeatTask != null) {
            this.waveRepeatTask.cancel();
            this.waveRepeatTask = null;
        }

        ScoreboardManager.instance.update();
    }

    public void resetMatch() {
        if (this.getMatchState() != MatchState.POST_GAME) {
            DvS.instance.getLogger().warning("State is " + this.getMatchState() + " not " + MatchState.POST_GAME + ". Might cause unexpected behavior");
        }

        World lobbyWorld = Bukkit.getWorld("world");
        if (lobbyWorld == null) {
            DvSLogger.error("Cannot reset match, lobby world 'world' is missing");
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.teleport(lobbyWorld.getSpawnLocation());
            p.setGameMode(GameMode.ADVENTURE);
            p.clearActivePotionEffects();
            p.getInventory().clear();
        }

        setMatchState(MatchState.PRE_GAME);
        ScoreboardManager.instance.update();
    }

    public void updateBossBar() {
        double count = Bukkit.getOnlinePlayers().size();
        double left = PlayerManager.instance.getAlive().size();

        BossBarManager.instance.setProgress(left / count);
        BossBarManager.instance.setTitle((int)left + " players left");
    }

    public int getMonsterCount() {
        return monsterCount;
    }

    public void setMonsterCount(int monsterCount) {
        this.monsterCount = monsterCount;
    }

    @Nullable public Chest getPlayerChest() {
        return playerChest;
    }

    public void setPlayerChest(@Nullable Chest playerChest) {
        this.playerChest = playerChest;
    }

    public RunningHordeMap getRunningMap() {
        return this.map;
    }

}
