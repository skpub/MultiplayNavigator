package org.sk_dev.multiplaynavigator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NavigateCommand implements CommandExecutor, TabCompleter {
    private final MultiplayNavigator plugin;
    private final Coordinates coordinates;
    private final Map<UUID, NavigationTask> activeNavigationsRef;

    public NavigateCommand(MultiplayNavigator plugin, Map<UUID, NavigationTask> activeNavigations) throws IOException, NumberFormatException {
        this.coordinates = Coordinates.getInstance();
        this.plugin = plugin;
        this.activeNavigationsRef = activeNavigations;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String @NotNull [] args
    ) {
        if (args.length < 1) {
            commandSender.sendMessage("Specify the Waypoint name (ex: /navigate begin home).");
            return false;
        }

        if (args[0].equals("end")) {
            if (commandSender instanceof Player player) {
                this.plugin.stopNavigation(player.getUniqueId());
                commandSender.sendMessage("Navigation ended.");
                return true;
            }
            commandSender.sendMessage("Only players can stop the navigation.");
            return false;
        }

        if (args[0].equals("begin")) {
            if (args.length < 2) {
                commandSender.sendMessage("Specify the Waypoint name (ex: /navigate begin home).");
                return false;
            }
            String waypoint = args[1];
            Coordinate waypointCoord = coordinates.getAll().get(waypoint);
            if (waypointCoord == null) {
                commandSender.sendMessage("Not such waypoint.");
                return false;
            }
            commandSender.sendMessage("Beginning navigation to " + waypoint + ".");
            if (!(commandSender instanceof Player player)) {
                commandSender.sendMessage("You're not a player, huh? If you're a machine, go figure it out yourself!");
                return false;
            }
            NavigationTask navigation = new NavigationTask(this.plugin, player.getUniqueId(), waypointCoord.vector(), this.activeNavigationsRef);
            this.plugin.startNavigation(player.getUniqueId(), navigation);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String @NotNull [] args
    ) {
        if (args.length == 1) {
            return Arrays.asList(new String[] {"begin", "end"});
        }
        if (args[0].equals("begin")) {
            return coordinates.getAll().keySet().stream().toList();
        }
        return null;
    }
}
