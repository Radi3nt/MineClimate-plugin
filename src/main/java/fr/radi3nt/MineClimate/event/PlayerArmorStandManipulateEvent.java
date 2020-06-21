package fr.radi3nt.MineClimate.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static fr.radi3nt.MineClimate.timer.Runner.ArmorMap;

public class PlayerArmorStandManipulateEvent implements Listener {

    @EventHandler
    public void PlayerArmorStandManipulateEvent(org.bukkit.event.player.PlayerArmorStandManipulateEvent e) {
        if (ArmorMap.containsKey(e.getPlayer())) {
            if (e.getRightClicked() == ArmorMap.get(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

}
