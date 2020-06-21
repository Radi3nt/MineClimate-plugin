package fr.radi3nt.MineClimate.classes.enchants;

import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CustomsEnchants {

    public static final Enchantment GLOW = new EnchantmentWarper("glow", "", 1);


    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(GLOW);

        if (!registered) {
            registerEnchantments(GLOW);
        }
    }

    public static void registerEnchantments(Enchantment enchantment) {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Enchantment.registerEnchantment(enchantment);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
