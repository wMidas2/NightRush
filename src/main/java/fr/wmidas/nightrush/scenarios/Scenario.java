package fr.wmidas.nightrush.scenarios;

import org.bukkit.entity.Player;

public interface Scenario {

    String getName();

    void onNightStart();

    void onNightEnd();

    // Méthode optionnelle pour effets sur attaque, déplacements, etc
    default void onPlayerAttack(Player attacker, Player victim) {}

    default void onPlayerMove(Player player) {}
}
