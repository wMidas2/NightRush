package fr.wmidas.nightrush.game;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private final Map<Player, Team> playerTeams = new HashMap<>();

    public void setPlayerTeam(Player player, Team team) {
        playerTeams.put(player, team);
    }

    public Team getPlayerTeam(Player player) {
        return playerTeams.getOrDefault(player, Team.RUNNERS);
    }

    public boolean isHunter(Player player) {
        return getPlayerTeam(player) == Team.HUNTERS;
    }
}
