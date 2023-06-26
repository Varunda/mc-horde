package pw.honu.dvs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DvSLogger {

    public static void info(String s) {
        DvS.instance.getLogger().info(s);
        logToOps("INFO", ChatColor.AQUA, s);
    }

    public static void warn(String s) {
        DvS.instance.getLogger().warning(s);
        logToOps("WARN", ChatColor.YELLOW, s);
    }

    public static void error(String s) {
        DvS.instance.getLogger().severe(s);
        logToOps("ERRO", ChatColor.RED, s);
    }

    public static void logToOps(String prefix, ChatColor color, String msg) {
        Bukkit.getOnlinePlayers().forEach(iter -> {
            if (iter.isOp() && iter.isOnline()) {
                iter.sendMessage(color + "[" + prefix + "] " + ChatColor.RESET + msg);
            }
        });
    }

}
