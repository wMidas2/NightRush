package fr.wmidas.nightrush.game;

import fr.wmidas.nightrush.NightRush;
import fr.wmidas.nightrush.scenarios.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class GameManager {

    private final NightRush plugin;
    private final ScenarioManager scenarioManager;
    private final PlayerManager playerManager;
    private final LocationManager locationManager;

    private GameState state = GameState.WAITING;

    private final Set<Player> players = new HashSet<>();
    private final Random random = new Random();

    private int countdownTaskId = -1;
    private int gameDurationTaskId = -1;

    public GameManager(NightRush plugin) {
        this.plugin = plugin;
        this.scenarioManager = plugin.getScenarioManager();
        this.playerManager = plugin.getPlayerManager();
        this.locationManager = plugin.getLocationManager();
    }

    public GameState getState() {
        return state;
    }

    public boolean addPlayer(Player p) {
        if (players.contains(p)) {
            p.sendMessage(ChatColor.RED + "Tu es déjà dans la partie.");
            return false;
        }
        if (state != GameState.WAITING && state != GameState.STARTING) {
            p.sendMessage(ChatColor.RED + "La partie est déjà lancée, attends la prochaine !");
            return false;
        }
        players.add(p);
        locationManager.teleportToLobby(p);
        Bukkit.broadcastMessage(ChatColor.GREEN + p.getName() + " a rejoint la partie (" + players.size() + " joueurs)");
        return true;
    }

    public boolean removePlayer(Player p) {
        if (!players.contains(p)) return false;
        players.remove(p);
        playerManager.removePlayer(p);
        locationManager.teleportToLobby(p);
        Bukkit.broadcastMessage(ChatColor.YELLOW + p.getName() + " a quitté la partie (" + players.size() + " joueurs)");
        return true;
    }

    public void startGame() {
        if (state != GameState.WAITING && state != GameState.STARTING) return;

        if (players.size() < 2) {
            Bukkit.broadcastMessage(ChatColor.RED + "Pas assez de joueurs pour démarrer !");
            return;
        }

        state = GameState.STARTING;
        Bukkit.broadcastMessage(ChatColor.GOLD + "La partie démarre dans 10 secondes...");

        countdownTaskId = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            int count = 10;

            @Override
            public void run() {
                if (count == 0) {
                    Bukkit.getScheduler().cancelTask(countdownTaskId);
                    countdownTaskId = -1;
                    launchDay();
                    return;
                }
                if (count % 5 == 0 || count <= 5) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "Début dans " + count + " secondes...");
                }
                count--;
            }
        }, 0L, 20L).getTaskId();
    }

    private void launchDay() {
        state = GameState.INGAME_DAY;
        Bukkit.broadcastMessage(ChatColor.YELLOW + "☀️ Le jour commence !");

        assignTeams();

        // Téléportation et setup joueurs
        for (Player p : players) {
            p.teleport(locationManager.getLocation("spawn_runner"));
            playerManager.setPlayerTeam(p, Team.RUNNERS);
            p.getInventory().clear();
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(20);
            p.removePotionEffect(PotionEffectType.SPEED);
            p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }

        // Lance la nuit dans 1 minute
        gameDurationTaskId = Bukkit.getScheduler().runTaskLater(plugin, this::launchNight, 20 * 60).getTaskId();
    }

    private void launchNight() {
        state = GameState.INGAME_NIGHT;
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "🌙 La nuit tombe...");

        assignHunters();

        scenarioManager.activateNightScenarios();

        // Lance le jour dans 45 secondes
        gameDurationTaskId = Bukkit.getScheduler().runTaskLater(plugin, this::launchDay, 20 * 45).getTaskId();
    }

    private void assignTeams() {
        // Tous sont coureurs au départ
        for (Player p : players) {
            playerManager.setPlayerTeam(p, Team.RUNNERS);
        }
    }

    private void assignHunters() {
        // Choisir 2 chasseurs au hasard (ou moins si peu de joueurs)
        List<Player> list = new ArrayList<>(players);
        Collections.shuffle(list);

        int huntersCount = Math.max(1, players.size() / 4);
        huntersCount = Math.min(huntersCount, 2);

        for (int i = 0; i < list.size(); i++) {
            Player p = list.get(i);
            if (i < huntersCount) {
                playerManager.setPlayerTeam(p, Team.HUNTERS);
                p.teleport(locationManager.getLocation("spawn_hunter"));
                p.sendMessage(ChatColor.RED + "Tu es un chasseur !");
                p.getInventory().clear();
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 50, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 50, 0));
            } else {
                playerManager.setPlayerTeam(p, Team.RUNNERS);
                p.teleport(locationManager.getLocation("spawn_runner"));
                p.getInventory().clear();
            }
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(20);
        }
    }

    public void endGame() {
        state = GameState.ENDING;
        Bukkit.broadcastMessage(ChatColor.GOLD + "🎉 La partie est terminée !");
        for (Player p : players) {
            locationManager.teleportToLobby(p);
            p.getInventory().clear();
            playerManager.removePlayer(p);
        }
        players.clear();

        // Reset les scénarios
        scenarioManager.deactivateAll();

        state = GameState.WAITING;
    }
}
