package fr.radi3nt.MineClimate.classes.enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import static fr.radi3nt.MineClimate.ClimateAPI.plugin;

public class EnchantmentWarper extends Enchantment {

    private final String name;
    private final int maxlvl;

    public EnchantmentWarper(String namespace, String name, int lvl) {
        super(new NamespacedKey(plugin ,namespace));
        this.name = name;
        this.maxlvl = lvl;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return maxlvl;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return false;
    }
}
