package fr.radi3nt.MineClimate.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static fr.radi3nt.MineClimate.ClimateAPI.getThirstFromPlayer;
import static fr.radi3nt.MineClimate.ClimateAPI.resetPlayerThirst;
import static fr.radi3nt.MineClimate.timer.Runner.DieBar;

public class OnDeathEvent implements Listener {

    @EventHandler
    public void OnDeathEvent(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (getThirstFromPlayer(player)<=5) {
            e.setDeathMessage(e.getEntity().getDisplayName() + " die from thirst");
        }
        resetPlayerThirst(player);
    }

}
