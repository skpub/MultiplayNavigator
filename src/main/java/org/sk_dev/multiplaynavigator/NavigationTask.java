package org.sk_dev.multiplaynavigator;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;

public class NavigationTask extends BukkitRunnable {
    private final JavaPlugin plugin;
    private final UUID playerID;
    private final Vector destination;
    private final Map<UUID, NavigationTask> activeNavigationsRef;

    public NavigationTask(JavaPlugin plugin, UUID playerID, Vector destination, Map<UUID, NavigationTask> activeNavigations) {
        this.plugin = plugin;
        this.playerID = playerID;
        this.destination = destination;
        this.activeNavigationsRef = activeNavigations;
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
        if (source.distance(this.destination) < 10) {
            this.cancel();
        }
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

    @Override
    public void cancel() throws IllegalStateException {
        super.cancel();
        this.activeNavigationsRef.remove(this.playerID);
    }
}
