package fr.radi3nt.MineClimate.event;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import static fr.radi3nt.MineClimate.ClimateAPI.*;

public class CraftPurifiedBottle implements Listener {

    @EventHandler
    private void furnaceCanceller(FurnaceSmeltEvent event)
    {
        if(event.getSource() != null) {
            Potion potion = Potion.fromItemStack(new ItemStack(Material.POTION));
            potion.setType(PotionType.WATER);
            if (event.getSource().getType() == potion.toItemStack(1).getType()) {
                if (!event.getSource().isSimilar(potion.toItemStack(1))) {
                    event.setCancelled(true);
                } else {
                    int Min = 1;
                    int Max = 20;
                    int random = Min + (int) (Math.random() * ((Max - Min) + 1));
                    event.setResult(createPurifiedWater(random));
                }
            }
        }
    }

    @EventHandler
    private void furnaceCanceller(FurnaceBurnEvent event) {
        Furnace furnace = (Furnace) event.getBlock().getState();
        if (furnace != null) {
            if (furnace.getInventory() != null) {
                if (furnace.getInventory().getSmelting() != null) {
                    Potion potion = Potion.fromItemStack(new ItemStack(Material.POTION));
                    potion.setType(PotionType.WATER);
                    if (furnace.getInventory().getSmelting().getType() == potion.toItemStack(1).getType()) {
                        if (!furnace.getInventory().getSmelting().isSimilar(potion.toItemStack(1))) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
