package fr.radi3nt.MineClimate.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import static fr.radi3nt.MineClimate.ClimateAPI.*;

public class OnRegenerateEvent implements Listener {

    @EventHandler
    public void OnRegenerateEvent (EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player && !e.getEntity().isDead()) {
            Player player = (Player) e.getEntity();
            if (getThirstFromPlayer(player) > 60) {
                setCooldown(player, getCooldownFromPlayer(player) - 5);
            } else {
                e.setCancelled(true);
            }
        }
    }
}
