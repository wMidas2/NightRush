package fr.wmidas.nightrush.game;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class LocationManager {

    private final Plugin plugin;
    private final File file;
    private final YamlConfiguration config;

    public LocationManager(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "locations.yml");

        if (!file.exists()) {
            plugin.saveResource("locations.yml", false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void setLocation(String key, Location loc) {
        config.set(key, loc);
        save();
    }

    public Location getLocation(String key) {
        return config.getLocation(key);
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void teleportToLobby(Player player) {
        Location lobby = getLocation("lobby");
        if (lobby != null) player.teleport(lobby);
    }
}
