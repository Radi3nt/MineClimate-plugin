package fr.radi3nt.thirstplugin.event;

import fr.radi3nt.thirstplugin.MainThirstPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import static fr.radi3nt.thirstplugin.ThirstAPI.*;

public class ConsumeItemEvent implements Listener {

    Plugin plugin = MainThirstPlugin.getPlugin(MainThirstPlugin.class);

    @EventHandler
    public void ConsumeItemEvent (PlayerItemConsumeEvent e) {
        ItemStack item = e.getItem();
        for (String food : plugin.getConfig().getConfigurationSection("FoodValue").getKeys(false)) {
            if (food.equals(item.getType().toString().toLowerCase())) {
                if (item.getType().equals(Material.POTION)) {
                    Potion potion = Potion.fromItemStack(item);
                    if (potion.getType().equals(PotionType.WATER)) {
                        addThirst(e.getPlayer(), 4);
                        poisonCompact(e.getPlayer(), 59);
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
