package fr.radi3nt.MineClimate.event;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class StackEvent implements Listener {

    @EventHandler
    public void StackEvent(InventoryClickEvent e) {
        if (e.getCursor().hasItemMeta() && e.getCurrentItem().hasItemMeta()) {
            if (e.getCurrentItem().getType().equals(Material.POTION) && e.getCursor().getType().equals(Material.POTION)) {
                if (e.getCurrentItem().getItemMeta() instanceof PotionMeta && e.getCursor().getItemMeta() instanceof PotionMeta) {
                    final PotionMeta meta1 = (PotionMeta) e.getCurrentItem().getItemMeta();
                    final PotionData data1 = meta1.getBasePotionData();

                    final PotionMeta meta = (PotionMeta) e.getCursor().getItemMeta();
                    final PotionData data = meta.getBasePotionData();

                    if (data1.getType() == PotionType.WATER && data.getType() == PotionType.WATER) {
                        e.getCurrentItem().setAmount(e.getCursor().getAmount() + e.getCurrentItem().getAmount());
                        e.getCursor().setAmount(0);
                    }
                }
            }
        }

    }
}
