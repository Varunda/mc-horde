package pw.honu.dvs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import pw.honu.dvs.commands.CommandHandler;
import pw.honu.dvs.managers.BossBarManager;
import pw.honu.dvs.managers.MapManager;

public class DvS extends JavaPlugin {

    public static DvS instance = null;

    @Override
    public void onLoad() {

    }

    public void onEnable() {
        instance = this;
        setup();
    }

    public void onDisable() {
        BossBarManager.instance.removeAll();
    }

    private void setup() {
        try {
            createDataFolder();
            createMapsFolder();
            loadMaps();

            PluginCommand dvs = getCommand("dvs");
            if (dvs != null) {
                dvs.setExecutor(new CommandHandler(this));
            } else {
                throw new Exception("Missing PluginCommand");
            }

            ParentListener.register(this);

            World lobby = Bukkit.getWorld("world");
            if (lobby == null) {
                DvSLogger.error("no world/lobby found");
            } else {
                lobby.setDifficulty(Difficulty.PEACEFUL);
            }

        } catch (Exception ex) {
            getLogger().severe(ex.toString());
        }
    }

    private void createDataFolder() {
        File dir = getDataFolder();
        if (!dir.exists()) {
            if (dir.mkdir()) {
                getLogger().info("Data folder plugins made");
            } else {
                getLogger().warning("failed to create data folder");
            }
        }
    }

    private void createMapsFolder() {
        File dir = new File("./maps");
        if (!dir.exists()) {
            if (dir.mkdir()) {
                getLogger().info("Maps folder made");
            } else {
                getLogger().warning("failed to create maps folder");
            }
        }
    }

    public void loadMaps() {
        File mapsFolder = new File("./maps");
        DvS.instance.getLogger().info("Loading maps from " + mapsFolder.getAbsolutePath());

        File[] files = mapsFolder.listFiles();
        assert files != null;

        List<HordeMap> maps = new ArrayList<>();
        for (File map : files) {
            DvS.instance.getLogger().info("Loading map in folder " + map.getName() + "...");
            try {
                maps.add(HordeMap.loadFromFolder(map));
            } catch (IOException ex) {
                DvS.instance.getLogger().severe("Failed to load map " + map.getName() + ":\n" + ex.getLocalizedMessage());
            }
        }

        MapManager.instance.setMaps(maps);
    }

}
