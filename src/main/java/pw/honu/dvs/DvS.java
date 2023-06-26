package pw.honu.dvs;

import java.io.File;
import java.lang.reflect.Field;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pw.honu.dvs.commands.CommandHandler;
import pw.honu.dvs.listeners.BlockBreakListener;
import pw.honu.dvs.listeners.MonsterDeathListener;
import pw.honu.dvs.listeners.PlayerDeathListener;
import pw.honu.dvs.listeners.PlayerLoginListener;
import pw.honu.dvs.managers.BossBarManager;

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

            PluginCommand dvs = getCommand("dvs");
            if (dvs != null) {
                dvs.setExecutor(new CommandHandler(this));
            } else {
                throw new Exception("Missing PluginCommand");
            }

            ParentListener.register(this);
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

}
