package pw.honu.dvs.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.*;
import pw.honu.dvs.item.AbilityItem;
import pw.honu.dvs.util.TitleUtil;
import pw.honu.dvs.wave.Wave;
import pw.honu.dvs.wave.WaveEntry;
import ru.xezard.glow.data.glow.Glow;

import javax.annotation.Nullable;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import java.awt.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MatchManager {

    public static MatchManager instance;

    static {
        instance = new MatchManager(DvS.instance);
    }

    private @NotNull MatchState matchState;
    private int monsterCount;
    private @Nullable Chest playerChest;

    private DvS plugin;

    private MatchManager(DvS plugin) {
        this.plugin = plugin;
        matchState = MatchState.GATHERING;
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

        @Nullable Location monsterSpawn = LocationManager.instance.getMonsterSpawn();
        if (monsterSpawn == null) {
            DvSLogger.warn("Cannot spawn wave: Monster spawn is not set");
            return false;
        }

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
        if (getMatchState() != MatchState.GATHERING) {
            DvS.instance.getLogger().warning("Cannot start match, state is not " + MatchState.GATHERING + ", is currently: " + getMatchState());
            return false;
        }

        @Nullable Location monsterSpawn = LocationManager.instance.getMonsterSpawn();
        @Nullable Location playerSpawn = LocationManager.instance.getPlayerStart();
        @Nullable Location monsterLobby = LocationManager.instance.getMonsterLobby();
        @Nullable Chest playerChest = getPlayerChest();

        List<String> errors = new ArrayList<>();
        if (monsterSpawn == null) { errors.add("Missing monster spawn"); }
        if (playerSpawn == null) { errors.add("Missing player spawn"); }
        if (monsterLobby == null) { errors.add("Missing monster lobby"); }
        if (playerChest == null) { errors.add("Missing player chest"); }

        if (errors.size() > 0) {
            DvSLogger.warn("Cannot start the match:");
            for (String s : errors) {
                DvSLogger.warn(s);
            }
            return false;
        }

        assert playerSpawn != null;

        World gameWorld = playerSpawn.getWorld();

        gameWorld.setTime(18_000);
        gameWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
        gameWorld.setGameRule(GameRule.KEEP_INVENTORY, false);
        gameWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        gameWorld.setGameRule(GameRule.BLOCK_EXPLOSION_DROP_DECAY, true);
        gameWorld.setGameRule(GameRule.DO_INSOMNIA, false);
        gameWorld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        gameWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        gameWorld.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        gameWorld.setPVP(true);
        gameWorld.setDifficulty(Difficulty.EASY);

        setMatchState(MatchState.RUNNING);

        Bukkit.getOnlinePlayers().forEach(iter -> {
            PlayerManager.instance.setPlayer(iter.getUniqueId(), PlayerState.ALIVE);
            BossBarManager.instance.addPlayer(iter);

            if (iter.isOp()) {
                iter.getInventory().addItem(AbilityItem.create(AbilityItem.PATHFIND_ITEM, 1));
                iter.getInventory().addItem(AbilityItem.create(AbilityItem.TARGET_ITEM, 1));
                iter.getInventory().addItem(AbilityItem.create(AbilityItem.LAUNCHER_ITEM, 1));
                iter.getInventory().addItem(AbilityItem.create(AbilityItem.SPAWN_ZOMBIE, 1));
                iter.getInventory().addItem(AbilityItem.create(AbilityItem.SPAWN_PIGLIN_ITEM, 1));
                iter.getInventory().addItem(AbilityItem.create(AbilityItem.SPAWN_PIGLIN_ARCHER_ITEM, 1));
            } else {
                assert playerChest != null;
                ItemStack[] items = playerChest.getBlockInventory().getContents();
                if (iter.getPlayer() != null && items != null) {
                    iter.getPlayer().getInventory().setContents(items);
                }
            }

            iter.teleport(playerSpawn);
            iter.sendMessage(ChatColor.DARK_PURPLE + "Match started!");
        });

        new MonsterTargetRunnable().runTaskTimer(DvS.instance, 20 * 1, 20 * 10);
        new MonsterTargetCountRunnable().runTaskTimer(DvS.instance, 0, 20);

        return true;
    }

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
        }

        setMatchState(MatchState.POST_GAME);
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
}
