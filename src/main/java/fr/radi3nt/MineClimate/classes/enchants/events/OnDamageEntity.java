package fr.radi3nt.MineClimate.classes.enchants.events;

import fr.radi3nt.MineClimate.classes.enchants.CustomsEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static fr.radi3nt.MineClimate.ClimateAPI.getTemperatureFromPlayer;
import static fr.radi3nt.MineClimate.ClimateAPI.setTemperature;

public class OnDamageEntity implements Listener {

    @EventHandler
    public void OnDamageEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (((Player) e.getDamager()).getInventory().getItemInMainHand().containsEnchantment(CustomsEnchants.COLD)) {
                if (e.getEntity() instanceof Player) {
                    setTemperature((Player) e.getEntity(), getTemperatureFromPlayer((Player) e.getEntity()) - 3);
                } else {
                    ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, ((Player) e.getDamager()).getInventory().getItemInMainHand().getEnchantmentLevel(CustomsEnchants.COLD) * 20, 5, true, false), true);
                }
            }

        }
    }

}
