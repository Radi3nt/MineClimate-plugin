package fr.radi3nt.MineClimate.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static fr.radi3nt.MineClimate.ClimateAPI.getTemperatureFromPlayer;
import static fr.radi3nt.MineClimate.timer.Runner.ArmorMap;

public class OnPlayerMoveEvent implements Listener {

    @EventHandler
    public void OnPlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (getTemperatureFromPlayer(player) < -6) {
            Location playerloc = new Location(player.getLocation().getWorld(), e.getTo().getX(), e.getTo().getY() - 0.75, e.getTo().getZ());
            Location playerloc1 = new Location(player.getLocation().getWorld(), e.getTo().getX(), e.getTo().getY() - 1.75, e.getTo().getZ());
            if (ArmorMap.containsKey(player)) {
                ArmorMap.get(player).get(0).teleport(playerloc);
                ArmorMap.get(player).get(1).teleport(playerloc1);
            } else {
                ArrayList<ArmorStand> armorlist = new ArrayList<>();
                ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(playerloc, EntityType.ARMOR_STAND);
                armorStand.setCollidable(false);
                armorStand.setInvulnerable(true);
                armorStand.setGravity(false);
                armorStand.setSilent(true);
                armorStand.setAI(false);
                armorStand.setVisible(false);
                armorStand.setHelmet(new ItemStack(Material.ICE));
                armorlist.add(armorStand);
                armorlist.add(armorStand);
                ArmorMap.put(player, armorlist);
            }
        } else {
            if (ArmorMap.containsKey(player)) {
                ArmorMap.get(player).get(0).remove();
                ArmorMap.get(player).get(1).remove();
                ArmorMap.remove(player);
            }
        }
    }

}
