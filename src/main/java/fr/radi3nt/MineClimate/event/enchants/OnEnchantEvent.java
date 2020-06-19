package fr.radi3nt.MineClimate.event.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static fr.radi3nt.MineClimate.ClimateAPI.TemperatureEnchant;

public class OnEnchantEvent implements Listener {

    @EventHandler
    public void onEnchantEvent (EnchantItemEvent event) {
        Player player = event.getEnchanter();
        ItemStack item = event.getItem();
        Material mat = item.getType();
        if (mat.equals(Material.DIAMOND_CHESTPLATE)) {
            item.setItemMeta (addEnchantment (item, TemperatureEnchant, event.getExpLevelCost(), 10, 3));
        }
    }


    public ItemMeta addEnchantment (ItemStack item, String enchantmentName, int xp, int chance, int maxLevel) {
        if (item==null || enchantmentName==null) {
            return null;
        }
        int successRate = (int) Math.round(Math.random()*100);
        if (successRate<=chance) {
            int level = Math.round((float) xp/Math.round(30/(float)maxLevel));
            ArrayList<String> newLores = new ArrayList<String>();
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasLore()) {
                    newLores.addAll(item.getItemMeta().getLore());
                }
            }
            newLores.add(ChatColor.translateAlternateColorCodes('&', enchantmentName) + " " + level);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setLore(newLores);
            return itemMeta;
        }
        return item.getItemMeta();
    }

}
