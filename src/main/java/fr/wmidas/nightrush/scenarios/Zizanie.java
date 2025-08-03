package fr.wmidas.nightrush.scenarios;

import fr.wmidas.nightrush.NightRush;
import fr.wmidas.nightrush.game.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Zizanie implements Scenario {

    private boolean active = false;
    private Player infiltrator;

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            chooseInfiltrator();
        } else {
            infiltrator = null;
        }
    }

    @Override
    public String getName() {
        return "zizanie";
    }

    @Override
    public void onNightStart() {
        if (!active) return;
        if (infiltrator == null) return;

        Bukkit.broadcastMessage(ChatColor.RED + "⚠️ Un infiltré des chasseurs est parmi vous...");

        // Ici tu devras gérer via un listener la visibilité de l'infiltré selon les joueurs
    }

    @Override
    public void onNightEnd() {
        infiltrator = null;
    }

    private void chooseInfiltrator() {
        PlayerManager pm = NightRush.getInstance().getPlayerManager();
        List<Player> runners = new ArrayList<>();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!pm.isHunter(p)) runners.add(p);
        }

        if (runners.isEmpty()) return;

        Random random = new Random();
        infiltrator = runners.get(random.nextInt(runners.size()));
        infiltrator.sendMessage(ChatColor.GREEN + "Tu es un infiltré des chasseurs, reste discret !");
    }

    public Player getInfiltrator() {
        return infiltrator;
    }
}
