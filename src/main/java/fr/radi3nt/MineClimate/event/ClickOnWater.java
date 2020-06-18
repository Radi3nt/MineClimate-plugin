package fr.radi3nt.MineClimate.event;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.List;

import static fr.radi3nt.MineClimate.ClimateAPI.*;

public class ClickOnWater implements Listener {

    public static HashMap<Player, Integer> CooldownFromDrinking = new HashMap<>();
    Integer CooldownTime = 2;

    @EventHandler
    public void ClickOnWater(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_AIR && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR) && e.getPlayer().isSneaking()  ) {
            List<Block> los = e.getPlayer().getLineOfSight(null, 5);
            Player player = e.getPlayer();
            for (Block b : los) {
                if (b.getType() == Material.WATER || b.getType() == Material.LEGACY_STATIONARY_WATER) {
                    if (!CooldownFromDrinking.containsKey(player)) {
                        if (getThirstFromPlayer(player) != null) {
                            if (e.getClickedBlock().getBiome().equals(Biome.OCEAN)) {
                                if (getThirstFromPlayer(player) != MaxThirst) {
                                    poisonCompact(player, 80);
                                }
                                addThirst(player, DrinkFromSee);
                            } else {
                                if (getThirstFromPlayer(player) != MaxThirst) {
                                    poisonCompact(player, 60);
                                }
                                addThirst(player, DrinkFromWater);
                            }
                            CooldownFromDrinking.put(player, CooldownTime * TickValue);
                        } else {
                            setupPlayerThirst(player);
                        }
                    }
                    break;
                }
            }
        }
    }
}
