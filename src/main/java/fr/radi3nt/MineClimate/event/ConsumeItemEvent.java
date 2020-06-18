package fr.radi3nt.MineClimate.event;

import fr.radi3nt.MineClimate.MainMineClimate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import static fr.radi3nt.MineClimate.ClimateAPI.*;

public class ConsumeItemEvent implements Listener {

    Plugin plugin = MainMineClimate.getPlugin(MainMineClimate.class);

    @EventHandler
    public void ConsumeItemEvent(PlayerItemConsumeEvent e) {
        ItemStack item = e.getItem();
        for (String food : plugin.getConfig().getConfigurationSection("FoodValue").getKeys(false)) {
            if (food.equals(item.getType().toString().toLowerCase())) {
                if (getThirstFromPlayer(e.getPlayer()) != MaxThirst) {
                    if (item.getType().equals(Material.POTION)) {
                        Potion potion = Potion.fromItemStack(item);
                        if (potion.getType().equals(PotionType.WATER)) {
                            if (item.getItemMeta().getDisplayName().contains("Purified water bottle") && item.getItemMeta().hasLore()) {
                                addThirst(e.getPlayer(), 5);
                                String lore = item.getItemMeta().getLore().get(0);
                                char[] chars = new char[4];
                                lore.getChars(2, lore.indexOf('%'), chars, 0);
                                String percentageS = "";
                                for (char aChar : chars) {
                                    percentageS = percentageS + aChar;
                                }
                                percentageS = percentageS.trim();
                                int percentage = Integer.parseInt(percentageS);
                                poisonCompact(e.getPlayer(), percentage);
                            } else {
                                addThirst(e.getPlayer(), 4);
                                poisonCompact(e.getPlayer(), 59);
                            }
                        }
                    } else {
                        int value = plugin.getConfig().getInt("FoodValue." + food);
                        if (value > 0) {
                            addThirst(e.getPlayer(), value);
                        } else {
                            removeThirst(e.getPlayer(), value * -1);
                        }
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }
}
