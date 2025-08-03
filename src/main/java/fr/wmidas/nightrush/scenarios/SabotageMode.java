package fr.wmidas.nightrush.scenarios;

import fr.wmidas.nightrush.NightRush;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

public class SabotageMode implements Scenario, Listener {

    private boolean active = false;
    private final Plugin plugin;

    public SabotageMode() {
        this.plugin = NightRush.getInstance();
    }

    public void setActive(boolean active) {
        if (this.active == active) return;

        this.active = active;
        if (active) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        } else {
            InventoryOpenEvent.getHandlerList().unregister(this);
        }
    }

    @Override
    public String getName() { return "sabotage_mode"; }

    @Override
    public void onNightStart() {}

    @Override
    public void onNightEnd() {}

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!active) return;
        if (!(event.getPlayer() instanceof Player p)) return;

        if (event.getInventory().getType() == InventoryType.CHEST) {
            if (!NightRush.getInstance().getPlayerManager().isHunter(p)) {
                p.sendMessage(ChatColor.RED + "Tu ne peux pas ouvrir ce coffre la nuit !");
                event.setCancelled(true);
            }
        }
    }
}
