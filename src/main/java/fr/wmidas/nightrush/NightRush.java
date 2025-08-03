package fr.wmidas.nightrush;

import fr.wmidas.nightrush.game.PlayerManager;
import fr.wmidas.nightrush.game.Team;
import fr.wmidas.nightrush.scenarios.ScenarioListener;
import fr.wmidas.nightrush.scenarios.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NightRush extends JavaPlugin {

    private static NightRush instance;
    private PlayerManager playerManager;
    private ScenarioManager scenarioManager;

    private boolean isNight = false;

    @Override
    public void onEnable() {
        instance = this;
        playerManager = new PlayerManager();
        scenarioManager = new ScenarioManager();

        getServer().getPluginManager().registerEvents(new ScenarioListener(this), this);

        startDayNightCycle();

        getLogger().info("NightRush activé !");
    }

    @Override
    public void onDisable() {
        getLogger().info("NightRush désactivé !");
    }

    private void startDayNightCycle() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isNight) {
                    isNight = false;
                    scenarioManager.onNightEnd();
                    Bukkit.broadcastMessage("§eLe jour se lève !");
                } else {
                    isNight = true;
                    scenarioManager.onNightStart();
                    Bukkit.broadcastMessage("§2La nuit tombe, préparez-vous...");
                }
            }
        }.runTaskTimer(this, 0L, 20 * 60 * 5); // alterne toutes les 5 minutes (configurable)
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ScenarioManager getScenarioManager() {
        return scenarioManager;
    }

    public static NightRush getInstance() {
        return instance;
    }
}
