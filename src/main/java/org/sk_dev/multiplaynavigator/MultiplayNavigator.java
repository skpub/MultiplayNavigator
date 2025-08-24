package org.sk_dev.multiplaynavigator;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

public final class MultiplayNavigator extends JavaPlugin {
    private Coordinates coordinates;
    private static final Map<UUID, NavigationTask> activeNavigations = new LinkedHashMap<>();

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            try {
                getDataFolder().mkdirs();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "Couldn't get Plugin data folder.", e);
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
        try {
            this.coordinates = Coordinates.getInstance(this);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Couldn't get Coordinates.", e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
        try {
            this.getCommand("navigate").setExecutor(new NavigateCommand(this));
            this.getCommand("beacon").setExecutor(new BeaconCommand());
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Couldn't register some command(s).",e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void startNavigation(UUID playerID, NavigationTask task) {
        if (activeNavigations.containsKey(playerID)) {
            activeNavigations.get(playerID).cancel();
        }
        task.runTaskTimer(this, 0L, 1L);
        activeNavigations.put(playerID, task);
    }

    public void stopNavigation(UUID playerID) {
        NavigationTask task = activeNavigations.remove(playerID);
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
