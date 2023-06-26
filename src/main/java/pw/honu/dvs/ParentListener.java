package pw.honu.dvs;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pw.honu.dvs.listeners.*;

public class ParentListener {

    public static void register(JavaPlugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new PlayerDeathListener(), plugin);
        pm.registerEvents(new MonsterDeathListener(), plugin);
        pm.registerEvents(new PlayerLoginListener(), plugin);
        pm.registerEvents(new BlockBreakListener(), plugin);
        pm.registerEvents(new ItemListener(), plugin);
        pm.registerEvents(new CreatureSpawnListener(), plugin);
        pm.registerEvents(new HungerListener(), plugin);
        pm.registerEvents(new EntityTargetListener(), plugin);
        pm.registerEvents(new BlockItemDropListener(), plugin);
        pm.registerEvents(new SpellListener(), plugin);
        pm.registerEvents(new InventoryListener(), plugin);
        pm.registerEvents(new EntityDamageListener(), plugin);
        pm.registerEvents(new EntityMountListener(), plugin);
    }

}
