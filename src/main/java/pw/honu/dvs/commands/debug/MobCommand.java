package pw.honu.dvs.commands.debug;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.reflections.Reflections;
import pw.honu.dvs.commands.Command;
import pw.honu.dvs.commands.CommandInfo;
import pw.honu.dvs.commands.Commands;
import pw.honu.dvs.managers.AbilityManager;
import pw.honu.dvs.managers.MatchManager;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CommandInfo(
        name = "mob",
        usage = "/dvs mob kill|goals|ability",
        desc = "Modify a single mob",
        permission = "",
        pattern = "mob"
)
public class MobCommand implements Command {
    @Override
    public boolean run(MatchManager match, CommandSender sender, String... args) {
        if (!Commands.isPlayer(sender)) {
            sender.sendMessage("Must be a player");
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        Player p = (Player) sender;
        RayTraceResult result = p.rayTraceEntities(20);
        if (result == null || result.getHitEntity() == null) {
            sender.sendMessage(ChatColor.RED + "Not looking at a mob");
            return true;
        }

        Entity target = result.getHitEntity();
        if (!(target instanceof Mob)) {
            sender.sendMessage(ChatColor.YELLOW + "not looking at a mob");
            return true;
        }

        Mob mob = (Mob) target;

        String action = args[0].toLowerCase();

        switch (action) {
            case "kill":
                mob.setHealth(0);
                break;

            case "goals":
                String goalAction = (args.length == 1) ? "list" : args[1].toLowerCase();

                if ("list".equals(goalAction)) {
                    Collection<Goal<Mob>> goals = Bukkit.getMobGoals().getAllGoals(mob);

                    StringBuilder s = new StringBuilder("Goals for " + mob.getType() + ": " + mob.getUniqueId() + "\n");
                    for (Goal<Mob> goal : goals) {
                        s.append(goal.getKey().getNamespacedKey().asString()).append(": ");
                        s.append(goal.getTypes().stream().map(Enum::name).collect(Collectors.joining(", ")));
                        s.append("\n");
                    }
                    sender.sendMessage(s.toString());
                } else if ("rm".equals(goalAction) || "remove".equals(goalAction)) {
                    Collection<Goal<Mob>> goals = Bukkit.getMobGoals().getAllGoals(mob);
                    Bukkit.getMobGoals().removeAllGoals(mob);
                    sender.sendMessage("Removed " + goals.size() + " goals from");
                }

                break;

            case "target":
                LivingEntity mobTarget = mob.getTarget();
                if (mobTarget == null) {
                    sender.sendMessage("Mob has no target");
                } else {
                    Location mbLoc = mobTarget.getLocation();
                    String playerName = (mobTarget instanceof  Player) ? (String.format(" (%s)", ((Player)mobTarget).getName())) : "";

                    sender.sendMessage(String.format("Mob is targeting a %s%s at [%d %d %d]", mobTarget.getType(), playerName, mbLoc.getBlockX(), mbLoc.getBlockY(), mbLoc.getBlockZ()));
                }
                break;

            case "path":
                Pathfinder.PathResult path = mob.getPathfinder().getCurrentPath();
                if (path == null) {
                    sender.sendMessage("Mob has no path");
                } else {
                    Location next = path.getNextPoint();
                    if (next != null) {
                        sender.sendMessage(String.format("Mob is currently going to [%d %d %d]", next.getBlockX(), next.getBlockY(), next.getBlockZ()));
                    }
                    Location last = path.getFinalPoint();
                    if (last != null) {
                        sender.sendMessage(String.format("Mob end target is [%d %d %d]", last.getBlockX(), last.getBlockY(), last.getBlockZ()));
                    }
                }
                break;

            case "ability":
                sender.sendMessage("Not yet supported lol");
                break;
        }

        return true;
    }

    @Override
    public List<String> tab(MatchManager match, Player player, String... args) {
        if (args.length > 1) {

            if ("ability".equals(args[1].toLowerCase())) {
                return AbilityManager.instance.getNames();
            } else if ("goals".equals(args[1].toLowerCase())) {
                return List.of("list", "rm");
            }

            return List.of();
        }

        return List.of("kill", "goals", "path", "target", "ability");
    }
}
