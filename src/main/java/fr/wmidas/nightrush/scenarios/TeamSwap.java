package fr.wmidas.nightrush.scenarios;

import fr.wmidas.nightrush.NightRush;
import fr.wmidas.nightrush.game.PlayerManager;
import fr.wmidas.nightrush.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamSwap implements Scenario {

    private boolean active = false;

    public void setActive(boolean active) { this.active = active; }

    @Override
    public String getName() { return "team_swap"; }

    @Override
    public void onNightStart() {
        if (!active) return;

        Bukkit.broadcastMessage(ChatColor.DARK_RED + "⚠️ Team Swap activé ! Les équipes changent !");

        PlayerManager pm = NightRush.getInstance().getPlayerManager();
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);

        for (Player p : players) {
            if (pm.isHunter(p)) {
                pm.setPlayerTeam(p, Team.RUNNERS);
                p.sendMessage(ChatColor.GREEN + "Tu es maintenant un Runner !");
            } else {
                pm.setPlayerTeam(p, Team.HUNTERS);
                p.sendMessage(ChatColor.RED + "Tu es maintenant un Chasseur !");
            }
        }
    }

    @Override
    public void onNightEnd() {}
}
