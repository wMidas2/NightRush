package fr.wmidas.nightrush.commandes;

import fr.wmidas.nightrush.NightRush;
import fr.wmidas.nightrush.game.GameManager;
import fr.wmidas.nightrush.scenarios.ScenarioManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class NightRushCommand implements CommandExecutor {

    private final NightRush plugin;
    private final GameManager gameManager;
    private final ScenarioManager scenarioManager;

    public NightRushCommand(NightRush plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        this.scenarioManager = plugin.getScenarioManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Seul un joueur peut utiliser cette commande.");
            return true;
        }
        Player p = (Player) sender;

        if (args.length == 0) {
            sendHelp(p);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join":
                gameManager.addPlayer(p);
                break;

            case "leave":
                gameManager.removePlayer(p);
                break;

            case "start":
                gameManager.startGame();
                break;

            case "scenario":
                if (args.length != 3) {
                    p.sendMessage(ChatColor.RED + "Usage : /nightrush scenario <nom> <on/off>");
                    return true;
                }
                String scen = args[1].toLowerCase();
                boolean state = args[2].equalsIgnoreCase("on");
                boolean ok = scenarioManager.toggleScenario(scen, state);
                p.sendMessage(ok
                        ? ChatColor.GREEN + "Scénario " + scen + " " + (state ? "activé" : "désactivé")
                        : ChatColor.RED + "Scénario inconnu : " + scen);
                break;

            case "setlobby":
                plugin.getLocationManager().setLocation("lobby", p.getLocation());
                p.sendMessage(ChatColor.GREEN + "Lobby défini !");
                break;

            case "setspawn":
                if (args.length != 2) {
                    p.sendMessage(ChatColor.RED + "Usage : /nightrush setspawn <hunter|runner>");
                    return true;
                }
                String team = args[1].toLowerCase();
                if (!team.equals("hunter") && !team.equals("runner")) {
                    p.sendMessage(ChatColor.RED + "Equipe invalide : hunter ou runner seulement.");
                    return true;
                }
                plugin.getLocationManager().setLocation("spawn_" + team, p.getLocation());
                p.sendMessage(ChatColor.GREEN + "Spawn " + team + " défini !");
                break;

            default:
                sendHelp(p);
                break;
        }

        return true;
    }

    private void sendHelp(Player p) {
        p.sendMessage(ChatColor.GOLD + "---- NightRush Commandes ----");
        p.sendMessage(ChatColor.YELLOW + "/nightrush join - Rejoindre la partie");
        p.sendMessage(ChatColor.YELLOW + "/nightrush leave - Quitter la partie");
        p.sendMessage(ChatColor.YELLOW + "/nightrush start - Démarrer la partie");
        p.sendMessage(ChatColor.YELLOW + "/nightrush scenario <nom> <on/off> - Activer/désactiver scénario");
        p.sendMessage(ChatColor.YELLOW + "/nightrush setlobby - Définir le lobby");
        p.sendMessage(ChatColor.YELLOW + "/nightrush setspawn <hunter|runner> - Définir les spawns");
    }
}
