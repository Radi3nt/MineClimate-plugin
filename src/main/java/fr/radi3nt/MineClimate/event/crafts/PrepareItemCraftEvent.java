package fr.radi3nt.MineClimate.event.crafts;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import static fr.radi3nt.MineClimate.ClimateAPI.createGourd;
import static fr.radi3nt.MineClimate.ClimateAPI.createPurifiedWater;

public class PrepareItemCraftEvent implements Listener {

    @EventHandler
    public void PrepareItemCraftEvent (org.bukkit.event.inventory.PrepareItemCraftEvent e) {
        if (checkForGourdRecipe(e.getInventory().getMatrix())==1) {
            e.getInventory().setResult(createGourd(50, 3));
        }
        if (checkForGourdRecipe(e.getInventory().getMatrix())==2) {
            String lore = e.getInventory().getMatrix()[4].getItemMeta().getLore().get(0);
            char[] chars = new char[4];
            lore.getChars(2, lore.indexOf('%'), chars, 0);
            String percentageS = "";
            for (char aChar : chars) {
                percentageS = percentageS + aChar;
            }
            percentageS = percentageS.trim();
            int percentage = Integer.parseInt(percentageS);
            e.getInventory().setResult(createGourd(percentage, 5));
        }
    }


    public static Integer checkForGourdRecipe(ItemStack[] itemStacks) {
        Material leather = Material.LEATHER;
        if (itemStacks[0]==null || itemStacks[0].getType()!=leather) {
            return 0;
        }
        if (itemStacks[1]==null || itemStacks[1].getType()!=leather) {
            return 0;
        }
        if (itemStacks[2]==null || itemStacks[2].getType()!=leather) {
            return 0;
        }
        if (itemStacks[3]==null || itemStacks[3].getType()!=leather) {
            return 0;
        }
        if (itemStacks[5]==null || itemStacks[5].getType()!=leather) {
            return 0;
        }
        if (itemStacks[6]==null || itemStacks[6].getType()!=leather) {
            return 0;
        }
        if (itemStacks[7]==null || itemStacks[7].getType()!=leather) {
            return 0;
        }
        if (itemStacks[8]==null || itemStacks[8].getType()!=leather) {
            return 0;
        }
        if (itemStacks[4]!=null && itemStacks[4].getType() == Material.POTION) {
            if (itemStacks[4].getItemMeta().hasLore() && itemStacks[4].getItemMeta().getDisplayName().contains("Purified water bottle")) {
                return 2;

            } else {

                if (itemStacks[4].getItemMeta() instanceof PotionMeta) {
                    final PotionMeta meta = (PotionMeta) itemStacks[4].getItemMeta();
                    final PotionData data = meta.getBasePotionData();
                    if(data.getType() == PotionType.WATER) {
                        if (!itemStacks[4].getItemMeta().hasLore() || !itemStacks[4].getItemMeta().getDisplayName().contains("Gourd")) {
                            return 1;
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        } else {
            return 0;
        }
    }

}
