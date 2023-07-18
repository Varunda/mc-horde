package pw.honu.dvs.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.honu.dvs.DvS;
import pw.honu.dvs.commands.debug.*;
import pw.honu.dvs.commands.setup.*;
import pw.honu.dvs.managers.MatchManager;

import java.util.*;
import java.util.stream.Collectors;

public class CommandHandler implements CommandExecutor, TabCompleter {

    private DvS plugin;

    private Map<String, Command> commands;

    public CommandHandler(final DvS plugin) {
        this.plugin = plugin;

        try {
            registerCommands();
        } catch (Exception e) {
            plugin.getLogger().severe(e.toString());
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        String base = (args.length > 0 ? args[0] : "").toLowerCase();

        MatchManager match = MatchManager.instance;

        List<Command> matches = getMatchingCommands(base);

        if (matches.size() > 1) {
            sender.sendMessage("Multiple commands");
            for (Command cmd : matches) {
                CommandInfo info = cmd.getClass().getAnnotation(CommandInfo.class);
                sender.sendMessage("Usage: "  + info.usage() + " " + ChatColor.YELLOW + info.desc());
            }
            return true;
        }

        if (matches.size() == 0) {
            sender.sendMessage("No commands found");
            return true;
        }

        Command cmd = matches.get(0);

        if (!sender.isOp()) {
            return true;
        }

        String[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);
        if (!cmd.run(match, sender, cmdArgs)){
            CommandInfo info = cmd.getClass().getAnnotation(CommandInfo.class);
            sender.sendMessage("Usage: "  + info.usage() + " " + ChatColor.YELLOW + info.desc());
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (!Commands.isPlayer(sender)) {
            return null;
        }

        Player p = Commands.unwrap(sender);

        String base = (args.length > 0 ? args[0] : "").toLowerCase();

        // If there's no base argument, show it all
        if (base.equals("")) {
            return commands.values()
                    .stream()
                    .map(cmd -> cmd.getClass().getAnnotation(CommandInfo.class))
                    .filter(info -> info != null && p.hasPermission(info.permission()))
                    .map(CommandInfo::name)
                    .sorted()
                    .collect(Collectors.toList());
        }

        // Reloads are terminal
        if (base.equals("reload") || (base.equals("config") && args.length > 1 && args[1].equals("reload"))) {
            return Collections.emptyList();
        }

        // If we only have the base, terminate
        if (args.length == 1) {
            return commands.values().stream()
                    .map(cmd -> cmd.getClass().getAnnotation(CommandInfo.class))
                    .filter(info -> info.name().startsWith(base))
                    .map(CommandInfo::name)
                    .sorted()
                    .collect(Collectors.toList());
        }

        // Otherwise, find the command
        List<Command> matches = getMatchingCommands(base);
        if (matches.size() != 1) {
            return Collections.emptyList();
        }

        // And pass completion
        Command cmd = matches.get(0);
        String[] params = Arrays.copyOfRange(args, 1, args.length);
        return cmd.tab(MatchManager.instance, p, params);
    }

    private List<Command> getMatchingCommands(String arg) {
        List<Command> result = new ArrayList<>();

        // Grab the commands that match the argument.
        for (Map.Entry<String,Command> entry : commands.entrySet()) {
            if (arg.matches(entry.getKey())) {
                result.add(entry.getValue());
            }
        }

        return result;
    }

    private void registerCommands() throws Exception {
        commands = new HashMap<>();

        register(LocCommand.class);
        register(WaveCommand.class);
        register(MonsterCommand.class);
        register(StartCommand.class);
        register(ChestCommand.class);
        register(PlayerCommand.class);
        register(MatchCommand.class);
        register(AbilityCommand.class);
        register(SpawnCommand.class);
        register(ItemCommand.class);
        register(MobCommand.class);
        register(WorldCommand.class);
        register(MapCommand.class);
    }

    public void register(Class<? extends Command> cmd) throws Exception {
        CommandInfo info = cmd.getAnnotation(CommandInfo.class);
        if (info == null) {
            throw new Exception("Missing CommandInfo on " + cmd.getCanonicalName());
        }

        commands.put(info.name(), cmd.getDeclaredConstructor().newInstance());
    }

}
