package fr.radi3nt.MineClimate.event;

import fr.radi3nt.MineClimate.MainMineClimate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

import static fr.radi3nt.MineClimate.ClimateAPI.*;

public class ConsumeItemEvent implements Listener {

    Plugin plugin = MainMineClimate.getPlugin(MainMineClimate.class);

    @EventHandler
    public void ConsumeItemEvent(PlayerItemConsumeEvent e) {
        ItemStack item = e.getItem();
        for (String food : plugin.getConfig().getConfigurationSection("FoodValue").getKeys(false)) {
            if (food.equals(item.getType().toString().toLowerCase())) {
                    if (item.getItemMeta() instanceof PotionMeta) {
                        final PotionMeta meta = (PotionMeta) item.getItemMeta();
                        final PotionData data = meta.getBasePotionData();
                        if(data.getType() == PotionType.WATER) {
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
                                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1F, 1F);

                            } else if (item.getItemMeta().getDisplayName().contains("Gourd") && item.getItemMeta().hasLore()) {
                                e.setCancelled(true);
                                addThirst(e.getPlayer(), 3);
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


                                ItemStack item1 = e.getItem();
                                ItemMeta itemMeta = item1.getItemMeta();
                                String lore3 = "";
                                try {
                                    lore3 = itemMeta.getLore().get(1);
                                } catch (Exception event) {
                                    ArrayList<String> lore1 = (ArrayList<String>) itemMeta.getLore();
                                    lore1.add(ChatColor.GRAY + "5 drink remaining");
                                    itemMeta.setLore(lore1);
                                    item1.setItemMeta(itemMeta);
                                    lore3 = ChatColor.GRAY + "5 drink remaining";
                                }
                                char[] chars1 = new char[10];
                                lore3.getChars(2, lore3.indexOf('d'), chars1, 0);
                                String reamingS = "";
                                for (char aChar : chars1) {
                                    reamingS = reamingS + aChar;
                                }
                                reamingS = reamingS.trim();
                                int reaming = Integer.parseInt(reamingS);
                                reaming=reaming-1;
                                ItemStack finaly = new ItemStack(Material.POTION);
                                if (reaming <= 0) {
                                    finaly.setType(Material.GLASS_BOTTLE);
                                } else {
                                    finaly.setItemMeta(createGourd(percentage, reaming).getItemMeta());
                                }
                                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1F, 1F);
                                e.getPlayer().getInventory().setItemInMainHand(finaly);
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
            }
        }
    }
}
