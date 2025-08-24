package org.sk_dev.waypointnav;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class NavigationTask extends BukkitRunnable {
    private final JavaPlugin plugin;
    private final UUID playerID;
    private final Vector destination;

    public NavigationTask(JavaPlugin plugin, UUID playerID, Vector destination) {
        this.plugin = plugin;
        this.playerID = playerID;
        this.destination = destination;
    }

    @Override
    public void run() {
        Player player = plugin.getServer().getPlayer(playerID);
        if (player == null) {
            this.cancel();
            return;
        }
        if (!player.isOnline()) {
            this.cancel();
            return;
        }

        Vector source = player.getLocation().toVector();
        Vector direction = destination.clone().subtract(source).normalize();

        for (int i = 1; i <= 10; i++) {
            Vector step = source.clone().add(direction.clone().multiply(i));
            Location loc = new Location(
                    player.getWorld(),
                    step.getX(),
                    step.getY() + 1.0,
                    step.getZ()
            );
            player.getWorld().spawnParticle(Particle.BUBBLE, loc, 2);
        }
    }
}
