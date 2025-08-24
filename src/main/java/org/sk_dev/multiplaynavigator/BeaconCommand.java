package org.sk_dev.multiplaynavigator;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BeaconCommand implements CommandExecutor, TabCompleter {
    private final Coordinates coordinates;

    public BeaconCommand() throws IOException, NumberFormatException {
        this.coordinates = Coordinates.getInstance();
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String @NotNull [] args
    ) {
        if (args.length < 1) {
            commandSender.sendMessage("ex: /beacon add world home 0 64 0");
            commandSender.sendMessage("ex: /beacon delete fortress");
            commandSender.sendMessage("ex: /beacon list");
            commandSender.sendMessage("ex: /beacon list end");
            return false;
        }
        switch (args[0]) {
            case "add" -> {
                if (args.length != 6) {
                    commandSender.sendMessage("ex: /beacon add home 0 64 0");
                    return false;
                }
                WorldEnum world;
                switch (args[1]) {
                    case "world", "World", "WORLD" -> world = WorldEnum.WORLD;
                    case "nether", "Nether", "NETHER" -> world = WorldEnum.NETHER;
                    case "end", "End", "END" -> world = WorldEnum.END;
                    default -> {
                        commandSender.sendMessage("ex: /beacon add world home 33 64 -38");
                        commandSender.sendMessage("ex: /beacon add nether fortress -340 50 30");
                        commandSender.sendMessage("ex: /beacon add end city 1300 60 30");
                        return false;
                    }
                }
                String wayPointName = args[2];
                int x = Integer.parseInt(args[3]);
                int y = Integer.parseInt(args[4]);
                int z = Integer.parseInt(args[5]);

                try {
                    coordinates.put(wayPointName, new Coordinate(world, new Vector(x, y, z)));
                    commandSender.sendMessage("Added " + wayPointName + ".");
                } catch (IOException e) {
                    commandSender.sendMessage("Couldn't add the waypoint.");
                    return false;
                }
                return true;
            }
            case "delete" -> {
                if (args.length != 2) {
                    commandSender.sendMessage("ex: /beacon delete home");
                    commandSender.sendMessage("ex: /beacon delete fortress");
                    return false;
                }
                String waypointName = args[1];
                try {
                    coordinates.delete(waypointName);
                    commandSender.sendMessage("Deleted " + waypointName + ".");
                } catch (IOException e) {
                    commandSender.sendMessage("Couldn't delete the waypoint.");
                    return false;
                }
                return true;
            }
            case "list" -> {
                if (args.length == 1) {
                    String[] list = coordinates.getAll().entrySet().stream().map(e ->
                            String.format("%s %s X: %d Y: %d Z: %d",
                                    e.getKey(),
                                    e.getValue().world(),
                                    e.getValue().vector().getBlockX(),
                                    e.getValue().vector().getBlockY(),
                                    e.getValue().vector().getBlockZ()
                            )).toArray(String[]::new);
                    commandSender.sendMessage(list);
                    return true;
                }
                if (args.length == 2) {
                    WorldEnum world;
                    switch (args[1]) {
                        case "world", "World", "WORLD" -> world = WorldEnum.WORLD;
                        case "nether", "Nether", "NETHER" -> world = WorldEnum.NETHER;
                        case "end", "End", "END" -> world = WorldEnum.END;
                        default -> {
                            commandSender.sendMessage("ex: /beacon list world");
                            commandSender.sendMessage("ex: /beacon list nether");
                            commandSender.sendMessage("ex: /beacon list end");
                            return false;
                        }
                    }
                    String[] list = coordinates.getAll().entrySet().stream()
                            .filter(e -> e.getValue().world() == world)
                            .map(e -> String.format("%s %s X: %d Y: %d Z: %d",
                                    e.getKey(),
                                    e.getValue().world(),
                                    e.getValue().vector().getBlockX(),
                                    e.getValue().vector().getBlockY(),
                                    e.getValue().vector().getBlockZ()
                            )).toArray(String[]::new);
                    commandSender.sendMessage(list);
                    return true;
                }
            }
        }
        commandSender.sendMessage("ex: /beacon add world home 0 64 0");
        commandSender.sendMessage("ex: /beacon delete fortress");
        commandSender.sendMessage("ex: /beacon list");
        commandSender.sendMessage("ex: /beacon list end");
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
            return Arrays.asList(new String[] {"add", "list", "delete"});
        }
        if (args[0].equals("list")) {
            return coordinates.getAll().entrySet().stream().map(e ->
                    String.format("%s %s X: %d Y: %d Z: %d",
                            e.getKey(),
                            e.getValue().world(),
                            e.getValue().vector().getBlockX(),
                            e.getValue().vector().getBlockY(),
                            e.getValue().vector().getBlockZ()
                            )).collect(Collectors.toList());
        }
        if (args[0].equals("add")) {
            if (args.length == 2) {
                return Arrays.asList(new String[] {
                        WorldEnum.WORLD.toString(),
                        WorldEnum.NETHER.toString(),
                        WorldEnum.END.toString()
                });
            }
            /* SPECIFY YOUR OWN WAYPOINT NAME.
            if (args.length == 2) {
            }
            */
            if (args.length == 4) {
                if (commandSender instanceof Player player) {
                    Block target = player.getTargetBlockExact(5);
                    Location targetLoc = target == null ? player.getLocation() : target.getLocation();
                    return Arrays.asList(new String[] {String.valueOf(targetLoc.getBlockX())});
                }
            }
            if (args.length == 5) {
                if (commandSender instanceof Player player) {
                    Block target = player.getTargetBlockExact(5);
                    Location targetLoc = target == null ? player.getLocation() : target.getLocation();
                    return Arrays.asList(new String[] {String.valueOf(targetLoc.getBlockY())});
                }
            }
            if (args.length == 6) {
                if (commandSender instanceof Player player) {
                    Block target = player.getTargetBlockExact(5);
                    Location targetLoc = target == null ? player.getLocation() : target.getLocation();
                    return Arrays.asList(new String[] {String.valueOf(targetLoc.getBlockZ())});
                }
            }
        }
        return null;
    }
}
