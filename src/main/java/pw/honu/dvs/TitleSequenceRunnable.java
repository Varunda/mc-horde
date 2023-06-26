package pw.honu.dvs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minecraft.network.chat.ChatDecoration;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import pw.honu.dvs.util.TitleUtil;

import java.time.Duration;

public class TitleSequenceRunnable extends BukkitRunnable {

    @Override
    public void run() {
        TitleUtil.title(
                Title.title(
                        Component.text(ChatColor.RED + "It's time to play..."),
                        Component.text(""),
                        Title.Times.times(
                                Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1)
                        )
                )
        );

        new BukkitRunnable() {
            @Override
            public void run() {
                TitleUtil.title(
                        Title.title(
                                Component.text(ChatColor.GOLD + "Dwarves"),
                                Component.text(""),
                                Title.Times.times(
                                        Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0)
                                )
                        )
                );
            }
        }.runTaskLaterAsynchronously(DvS.instance, 20 * 4);

        new BukkitRunnable() {
            @Override
            public void run() {
                TitleUtil.title(
                        Title.title(
                                Component.text(ChatColor.YELLOW + "VS"),
                                Component.text(""),
                                Title.Times.times(
                                        Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0)
                                )
                        )
                );
            }
        }.runTaskLaterAsynchronously(DvS.instance, 20 * 5);

        new BukkitRunnable() {
            @Override
            public void run() {
                TitleUtil.title(
                        Title.title(
                                Component.text(ChatColor.STRIKETHROUGH + "Zombies " + ChatColor.RESET + ChatColor.WHITE + "Skeletons"),
                                Component.text(ChatColor.LIGHT_PURPLE + "thanks for playing <3"),
                                Title.Times.times(
                                        Duration.ofSeconds(0), Duration.ofSeconds(2), Duration.ofSeconds(1)
                                )
                        )
                );
            }
        }.runTaskLaterAsynchronously(DvS.instance, 20 * 6);
    }

}
