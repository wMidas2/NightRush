package fr.wmidas.nightrush.scenarios;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FogOfWar implements Scenario {

    private boolean active = false;

    public void setActive(boolean active) { this.active = active; }

    @Override
    public String getName() { return "fog_of_war"; }

    @Override
    public void onNightStart() {
        if (!active) return;
        Bukkit.broadcastMessage("🌫️ Fog of War activé ! La vision est réduite sauf pour les chasseurs.");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("nightrush.hunter")) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 45, 1));
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 45, 4));
            }
        }
    }

    @Override
    public void onNightEnd() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            p.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }
}
