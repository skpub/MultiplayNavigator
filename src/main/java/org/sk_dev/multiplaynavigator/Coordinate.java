package org.sk_dev.multiplaynavigator;

import org.bukkit.World;
import org.bukkit.util.Vector;

public record Coordinate(World.Environment world, Vector vector) {}
