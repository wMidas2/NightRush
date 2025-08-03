package fr.wmidas.nightrush.scenarios;

import fr.wmidas.nightrush.NightRush;
import fr.wmidas.nightrush.game.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class WMidas implements Scenario {

    private boolean active = false;
    private Player king;

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            chooseKing();
        } else {
            if (king != null) removeBuffs(king);
            king = null;
        }
    }

    @Override
    public String getName() {
        return "wmidas";
    }

    @Override
    public void onNightStart() {
        if (!active || king == null) return;

        Bukkit.broadcastMessage(ChatColor.GOLD + "👑 Le roi chasseur est " + king.getName() + " !");
        applyBuffs(king);
    }

    @Override
    public void onNightEnd() {
        if (king != null) removeBuffs(king);
    }

    private void chooseKing() {
        PlayerManager pm = NightRush.getInstance().getPlayerManager();
        Random random = new Random();

        var hunters = Bukkit.getOnlinePlayers().stream()
                .filter(pm::isHunter)
                .toList();

        if (hunters.isEmpty()) {
            king = null;
            return;
        }

        king = hunters.get(random.nextInt(hunters.size()));
        king.sendMessage(ChatColor.GOLD + "Tu es le roi chasseur, tu as Force I, Speed I et Resistance I !");
    }

    private void applyBuffs(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60, 0));
    }

    private void removeBuffs(Player p) {
        p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        p.removePotionEffect(PotionEffectType.SPEED);
        p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
    }
}
