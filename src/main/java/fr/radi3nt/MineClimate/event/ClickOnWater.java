package fr.radi3nt.MineClimate.event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static fr.radi3nt.MineClimate.ClimateAPI.*;

public class ClickOnWater implements Listener {

    public static HashMap<Player, Integer> CooldownFromDrinking = new HashMap<>();
    Integer CooldownTime = 2;

    @EventHandler
    public void ClickOnWater(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_AIR && e.getPlayer().isSneaking()  ) {
            List<Block> los = e.getPlayer().getLineOfSight(null, 5);
            Player player = e.getPlayer();
            for (Block b : los) {
                if (b.getType() == Material.WATER || b.getType() == Material.LEGACY_STATIONARY_WATER) {
                    if (!CooldownFromDrinking.containsKey(player)) {
                        if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {

                            if (getThirstFromPlayer(player) != null) {
                                if (e.getClickedBlock().getBiome().equals(Biome.OCEAN)) {
                                    if (getThirstFromPlayer(player) != MaxThirst) {
                                        player.playSound(player.getLocation(), Sound.ITEM_BUCKET_FILL, SoundCategory.PLAYERS, 1F, 1F);
                                        if (poisonCompact(player, 80) != 0) {
                                            player.playSound(player.getLocation(), Sound.ENTITY_CAT_HISS, SoundCategory.PLAYERS, 1F, 1F);
                                        } else {
                                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1F, 1F);
                                        }
                                    }
                                    addThirst(player, DrinkFromSee);
                                } else {
                                    if (getThirstFromPlayer(player) != MaxThirst) {
                                        player.playSound(player.getLocation(), Sound.ITEM_BUCKET_FILL, SoundCategory.PLAYERS, 1F, 1F);
                                        if (poisonCompact(player, 60) != 0) {
                                            player.playSound(player.getLocation(), Sound.ENTITY_CAT_HISS, SoundCategory.PLAYERS, 1F, 1F);
                                        } else {
                                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1F, 1F);
                                        }
                                    }
                                    addThirst(player, DrinkFromWater);
                                }
                                CooldownFromDrinking.put(player, CooldownTime * TickValue);
                            } else {
                                setupPlayerThirst(player);
                            }
                        } else {
                            if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Gourd")) {
                                ItemMeta itemMeta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
                                ItemStack item1 = e.getPlayer().getInventory().getItemInMainHand().clone();

                                String lore = item1.getItemMeta().getLore().get(0);
                                char[] chars = new char[4];
                                lore.getChars(2, lore.indexOf('%'), chars, 0);
                                String percentageS = "";
                                for (char aChar : chars) {
                                    percentageS = percentageS + aChar;
                                }
                                percentageS = percentageS.trim();
                                int percentage = Integer.parseInt(percentageS);

                                int Min = 0;
                                int Max = 4;
                                int random = Min + (int) (Math.random() * ((Max - Min) + 1));
                                percentage= percentage+random;
                                if (percentage>=100) {
                                    percentage=99;
                                }

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
                                if (reaming<5) {
                                    reaming = reaming + 1;
                                    ItemStack finaly = new ItemStack(Material.POTION);

                                    if (reaming <= 0) {
                                        finaly.setType(Material.GLASS_BOTTLE);
                                    } else {
                                        finaly.setItemMeta(createGourd(percentage, reaming).getItemMeta());
                                    }
                                    player.playSound(player.getLocation(), Sound.ITEM_BUCKET_FILL, SoundCategory.PLAYERS, 100, 1);
                                    player.getInventory().setItemInMainHand(finaly);
                                    player.updateInventory();

                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
}
