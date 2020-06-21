package fr.radi3nt.MineClimate.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static fr.radi3nt.MineClimate.ClimateAPI.*;
import static fr.radi3nt.MineClimate.timer.Runner.DieBar;

public class OnDeathEvent implements Listener {

    @EventHandler
    public void OnDeathEvent(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (e.getDeathMessage().equals(player.getName() + " died")) {
            if (getTemperatureFromPlayer(player) > 6) {
                e.setDeathMessage(e.getEntity().getName() + " died from hot");
            } else if (getTemperatureFromPlayer(player) < -6) {
                e.setDeathMessage(e.getEntity().getName() + " died from cold");
            } else if (getThirstFromPlayer(player) <= 1) {
                e.setDeathMessage(e.getEntity().getDisplayName() + " died from thirst");
            }
        }
    }

}
