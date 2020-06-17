package fr.radi3nt.thirstplugin.event;

import net.minecraft.server.v1_15_R1.Vector3f;
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
import java.util.Objects;
import java.util.Vector;

import static fr.radi3nt.thirstplugin.ThirstAPI.*;

public class ClickOnWater implements Listener {

    public static HashMap<Player, Integer> CooldownFromDrinking = new HashMap<>();
    Integer CooldownTime = 2;

    @EventHandler
    public void ClickOnWater(PlayerInteractEvent e) {
        if (e.getAction()!=Action.LEFT_CLICK_BLOCK && e.getAction()!=Action.LEFT_CLICK_AIR && e.getAction()!=Action.RIGHT_CLICK_AIR && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            List<Block> los = e.getPlayer().getLineOfSight(null, 5);
            Player player = e.getPlayer();
            for (Block b : los) {
                if (b.getType() == Material.WATER || b.getType() == Material.LEGACY_STATIONARY_WATER) {
                    if (!CooldownFromDrinking.containsKey(player)) {
                        if (getThirstFromPlayer(player)!=null) {
                            if (e.getClickedBlock().getBiome().equals(Biome.OCEAN)) {
                                if (getThirstFromPlayer(player)!=MaxThirst) {
                                    poisonCompact(player, 80);
                                }
                                addThirst(player, DrinkFromSee);
                            } else {
                                if (getThirstFromPlayer(player)!=MaxThirst) {
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
