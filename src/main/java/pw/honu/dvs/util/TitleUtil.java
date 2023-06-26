package pw.honu.dvs.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.time.Duration;

public class TitleUtil {

    public static void title(Title title) {
        Bukkit.getOnlinePlayers().forEach(iter -> iter.showTitle(title));
    }

}
