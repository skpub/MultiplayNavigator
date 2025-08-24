package org.sk_dev.waypointnav;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.logging.Level;

public final class WaypointNav extends JavaPlugin {
    private Coordinates coordinates;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            try {
                getDataFolder().mkdirs();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, e.toString());
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
        try {
            coordinates = Coordinates.getInstance(this);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, e.toString());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
