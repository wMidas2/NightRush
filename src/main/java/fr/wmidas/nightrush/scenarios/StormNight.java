package fr.wmidas.nightrush.scenarios;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class StormNight implements Scenario {

    private boolean active = false;

    public void setActive(boolean active) { this.active = active; }

    @Override
    public String getName() { return "storm_night"; }

    @Override
    public void onNightStart() {
        if (!active) return;
        for (World w : Bukkit.getWorlds()) {
            w.setStorm(true);
            w.setThundering(true);
            w.setWeatherDuration(20 * 60);
            w.setThunderDuration(20 * 60);
        }
    }

    @Override
    public void onNightEnd() {
        for (World w : Bukkit.getWorlds()) {
            w.setStorm(false);
            w.setThundering(false);
        }
    }
}
