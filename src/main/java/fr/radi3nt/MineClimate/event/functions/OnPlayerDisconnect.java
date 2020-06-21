package fr.radi3nt.MineClimate.event.functions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static fr.radi3nt.MineClimate.timer.Runner.ArmorMap;

public class OnPlayerDisconnect implements Listener {

    @EventHandler
    public void OnPlayerDisconnect(PlayerQuitEvent e) {
        if (ArmorMap.containsKey(e.getPlayer())) {
            ArmorMap.get(e.getPlayer()).get(0).remove();
            ArmorMap.get(e.getPlayer()).get(1).remove();
            ArmorMap.remove(e.getPlayer());
        }
    }

}
