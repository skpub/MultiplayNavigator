package org.sk_dev.waypointnav;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Coordinates {
    private static Coordinates INSTANCE;

    private Map<String, Coordinate> data;
    private File dataFile;
    private JavaPlugin plugin;

    private Coordinates(JavaPlugin plugin) throws IOException, NumberFormatException {
        this.plugin = plugin;
        this.data = new LinkedHashMap<String, Coordinate>();
        this.initialize();
    }

    public static Coordinates getInstance(JavaPlugin plugin) throws IOException, NumberFormatException {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = new Coordinates(plugin);
        return INSTANCE;
    }

    public void put(String name, Coordinate coord) throws IOException{
        this.data.put(name, coord);
        this.write();
    }

    public Map<String, Coordinate> getAll() {
        return this.data;
    }

    public void delete(String name) throws IOException {
        this.data.remove(name);
        this.write();
    }

    private void write() throws IOException {
        try (PrintWriter p = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(this.dataFile)
        )))) {
            this.data.forEach((k, v) -> {
                Vector vec = v.vector();
                p.printf("%s,%s,%d,%d,%d\n", k, v.world(), vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
            });
        }
    }

    private void initialize() throws IOException, NumberFormatException {
        this.dataFile = new File(this.plugin.getDataFolder(), "coordinates.csv");

        if (!this.dataFile.exists()) {
            this.dataFile.createNewFile();
        }
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(this.dataFile))) {
            while ((line = reader.readLine()) != null) {
                // name,world,x,y,z
                // home,world,103,68,-25; for ex.
                String[] parts = line.split(",");
                if (parts.length != 5) continue;
                String name = parts[0];
                String world = parts[1];
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[3]);
                int z = Integer.parseInt(parts[4]);

                Coordinate coord = new Coordinate(WorldEnum.valueOf(world), new Vector(x,y,z));
                this.data.put(name, coord);
            }
        }
    }
}
