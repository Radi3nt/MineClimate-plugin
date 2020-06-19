package fr.radi3nt.MineClimate.event.crafts;


import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftInventoryCrafting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;


import static fr.radi3nt.MineClimate.ClimateAPI.createGourd;

public class OnCraftEvent implements Listener {

    @EventHandler
    public void OnCraftEvent(InventoryClickEvent e) {
        if (e.getSlotType().equals(InventoryType.SlotType.RESULT)) {
            if(e.getView().getTopInventory() instanceof CraftInventoryCrafting) {
                CraftingInventory inventory = (CraftingInventory) e.getClickedInventory();
                if (e.getCurrentItem().getType() != Material.AIR) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Gourd")) {
                        ItemStack[] matrix = inventory.getMatrix();

                            for (ItemStack itemStack : matrix) {
                                if (itemStack.getAmount() - 1 >= 0) {
                                    itemStack.setAmount(itemStack.getAmount() - 1);
                                } else {
                                    itemStack.setType(Material.AIR);
                                }
                            }
                        String lore = e.getCurrentItem().getItemMeta().getLore().get(0);
                        char[] chars = new char[4];
                        lore.getChars(2, lore.indexOf('%'), chars, 0);
                        String percentageS = "";
                        for (char aChar : chars) {
                            percentageS = percentageS + aChar;
                        }
                        percentageS = percentageS.trim();
                        int percentage = Integer.parseInt(percentageS);

                        if (percentage!=50) {
                            inventory.setMatrix(matrix);
                            ItemStack item = createGourd(percentage, 5);
                            e.getWhoClicked().setItemOnCursor(item);

                        } else {
                            inventory.setMatrix(matrix);
                            int Min = 30;
                            int Max = 60;
                            int random = Min + (int) (Math.random() * ((Max - Min) + 1));
                            ItemStack item = createGourd(random, 3);
                            e.getWhoClicked().setItemOnCursor(item);
                        }
                    }
                }
            }
        }
    }

}
