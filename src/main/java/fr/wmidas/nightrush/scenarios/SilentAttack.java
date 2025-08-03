package fr.wmidas.nightrush.scenarios;

import fr.wmidas.nightrush.NightRush;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

public class SilentAttack implements Scenario, Listener {

    private boolean active = false;
    private final Plugin plugin;

    public SilentAttack() {
        this.plugin = NightRush.getInstance();
    }

    public void setActive(boolean active) {
        if (this.active == active) return;
        this.active = active;

        if (active) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        } else {
            EntityDamageByEntityEvent.getHandlerList().unregister(this);
        }
    }

    @Override
    public String getName() {
        return "silent_attack";
    }

    @Override
    public void onNightStart() {}

    @Override
    public void onNightEnd() {}

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!active) return;

        // Suppression des sons et particules est complexe, 
        // ici on désactive knockback pour simuler une attaque "silencieuse"
        event.setCancelled(false); // Laisse les dégâts passer, mais tu peux ajouter un knockback annulé via un autre event
    }
}
