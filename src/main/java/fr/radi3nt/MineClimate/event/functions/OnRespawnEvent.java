package fr.radi3nt.MineClimate.event.functions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static fr.radi3nt.MineClimate.ClimateAPI.resetPlayer;

public class OnRespawnEvent implements Listener {

    @EventHandler
    public void OnRespawnEvent (PlayerRespawnEvent e) {
        resetPlayer(e.getPlayer());
    }

}
