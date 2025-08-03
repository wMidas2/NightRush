package fr.wmidas.nightrush.scenarios;

import fr.wmidas.nightrush.NightRush;
import fr.wmidas.nightrush.game.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HunterBeast implements Scenario, Listener {

    private boolean active = false;
    private boolean hunterKilledSomeone = false;

    public void setActive(boolean active) {
        if (this.active == active) return;

        this.active = active;
        hunterKilledSomeone = false;

        if (active) {
            Bukkit.getPluginManager().registerEvents(this, NightRush.getInstance());
        } else {
            EntityDamageByEntityEvent.getHandlerList().unregister(this);
            PlayerDeathEvent.getHandlerList().unregister(this);
            PlayerQuitEvent.getHandlerList().unregister(this);
            // Cleanup buffs
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                p.removePotionEffect(PotionEffectType.SPEED);
            }
        }
    }

    @Override
    public String getName() { return "hunter_beast"; }

    @Override
    public void onNightStart() {
        if (!active) return;

        hunterKilledSomeone = false;

        Bukkit.broadcastMessage(ChatColor.RED + "🦁 Hunter Beast activé ! Les chasseurs sont surpuissants mais doivent tuer !");
        PlayerManager pm = NightRush.getInstance().getPlayerManager();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (pm.isHunter(p)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60, 1));
            }
        }
    }

    @Override
    public void onNightEnd() {
        if (!active) return;

        if (!hunterKilledSomeone) {
            Bukkit.broadcastMessage(ChatColor.RED + "⚠️ Les chasseurs n'ont tué personne cette nuit. Ils meurent !");
            PlayerManager pm = NightRush.getInstance().getPlayerManager();

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (pm.isHunter(p)) {
                    p.setHealth(0.0);
                }
            }
        }
        hunterKilledSomeone = false;

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            p.removePotionEffect(PotionEffectType.SPEED);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!active) return;
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player damager)) return;

        PlayerManager pm = NightRush.getInstance().getPlayerManager();
        if (pm.isHunter(damager)) {
            // On attend la mort pour valider kill
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!active) return;
        Player dead = event.getEntity();
        Player killer = dead.getKiller();

        if (killer == null) return;

        PlayerManager pm = NightRush.getInstance().getPlayerManager();

        if (pm.isHunter(killer)) {
            hunterKilledSomeone = true;
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!active) return;
        // Pas d'action spéciale
    }
}
