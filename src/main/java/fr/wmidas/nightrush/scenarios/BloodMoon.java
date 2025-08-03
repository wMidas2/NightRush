package fr.wmidas.nightrush.scenarios;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BloodMoon implements Scenario {

    private boolean active = false;

    public void setActive(boolean active) { this.active = active; }

    @Override
    public String getName() { return "blood_moon"; }

    @Override
    public void onNightStart() {
        if (!active) return;
        Bukkit.broadcastMessage("🩸 Blood Moon activée ! Tous les joueurs ont Force I !");
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 45, 0));
        }
    }

    @Override
    public void onNightEnd() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
    }
}
