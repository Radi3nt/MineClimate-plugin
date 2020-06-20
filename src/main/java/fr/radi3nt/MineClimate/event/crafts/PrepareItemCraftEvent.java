package fr.radi3nt.MineClimate.event.crafts;

import net.minecraft.server.v1_15_R1.ItemArmor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import static fr.radi3nt.MineClimate.ClimateAPI.*;

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
        if (checkForArmorRecipe(e.getInventory().getMatrix()).getType()!=Material.AIR) {
            e.getInventory().setResult(checkForArmorRecipe(e.getInventory().getMatrix()));
        }

    }


    public static Integer checkForGourdRecipe(ItemStack[] itemStacks) {
        Material air = Material.LEATHER;
        if (itemStacks[0]==null || itemStacks[0].getType()!=air) {
            return 0;
        }
        if (itemStacks[1]==null || itemStacks[1].getType()!=air) {
            return 0;
        }
        if (itemStacks[2]==null || itemStacks[2].getType()!=air) {
            return 0;
        }
        if (itemStacks[3]==null || itemStacks[3].getType()!=air) {
            return 0;
        }
        if (itemStacks[5]==null || itemStacks[5].getType()!=air) {
            return 0;
        }
        if (itemStacks[6]==null || itemStacks[6].getType()!=air) {
            return 0;
        }
        if (itemStacks[7]==null || itemStacks[7].getType()!=air) {
            return 0;
        }
        if (itemStacks[8]==null || itemStacks[8].getType()!=air) {
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

    public static ItemStack checkForArmorRecipe(ItemStack[] itemStacks) {
        ItemStack armor = new ItemStack(Material.AIR);
        for (ItemStack item : itemStacks) {
            if (isArmor(item)) {
                if (item!=null) {
                    ItemMeta itemMeta = item.getItemMeta();
                    if (!itemMeta.getDisplayName().equals(TemperatureItemName)) {
                        if (armor.getType()!=Material.AIR) {
                            return new ItemStack(Material.AIR);
                        } else {
                            if (itemMeta.hasLore()) {
                                if (!item.getItemMeta().getLore().contains(ChatColor.GOLD + "Ozzy Liner combined")) {
                                    armor = item;
                                }
                            } else {
                                armor = item;
                            }

                        }
                    }
                }
            }
        }
        if (armor.getType() == Material.AIR) {
            return armor;
        }
        if (itemStacks[4] != null) {
            ItemMeta itemMeta = itemStacks[4].getItemMeta();
            if (itemMeta.getDisplayName().equals(TemperatureItemName)) {
                if (itemStacks[4].getType().equals(Material.LEATHER_CHESTPLATE)) {
                    return combineOzzyLiner(armor);
                }
            }
        }
        return new ItemStack(Material.AIR);
    }

    public static boolean isArmor(ItemStack item) {
        return (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemArmor);
    }

}
