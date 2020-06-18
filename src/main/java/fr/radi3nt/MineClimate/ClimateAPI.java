package fr.radi3nt.MineClimate;

import fr.radi3nt.MineClimate.classes.Priority;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static fr.radi3nt.MineClimate.timer.Runner.DieBar;

public class ClimateAPI {

    public static Plugin plugin = MainMineClimate.getPlugin(MainMineClimate.class);

    private static HashMap<Player, Integer> ThirstBar = new HashMap<>();
    private static HashMap<Player, Double> Cooldown = new HashMap<>();
    private static HashMap<Player, Double> Temperature = new HashMap<>();

    private static HashMap<Player, Priority> BlockedPriority = new HashMap<>();

    public final static int DrinkFromSee = 1;
    public final static int DrinkFromWater = 2;
    public final static int DrinkFromWaterBottle = DrinkFromWater + 1;
    public final static int DrinkFromFilteredBottle = DrinkFromWater + 2;
    public final static int DrinkFromPurifiedWaterBottle = DrinkFromWater + 5;


    public final static double RainShift = 0.01;

    public final static double NightShift = 0.01;
    public final static double DayShift = 0.01;
    public final static int ThirstDecrease = 1;
    public final static double CooldownDecreaseAFK = 0.5;
    public final static double CooldownDecreaseSprint = 1.5;
    public final static double CooldownDecreaseWalk = 0.75;
    public final static double CooldownIncreaseSneak = 0.05;
    public final static int MaxThirst = 100;
    public final static double MaxCooldown = 10;

    public final static int DieInterval = 3;
    public final static double DieBaseDamage = 1;

    public final static int MaxBarDisplay = 100;
    public final static int MaxTemperatureMultiply = 100;

    public final static int TickValue = 20;


    public static Integer getThirstFromPlayer(Player player) {
        return ThirstBar.getOrDefault(player, null);
    }

    public static boolean setThirst(Player player, Integer thirst) {
        if (thirst <= MaxThirst) {
            ThirstBar.put(player, thirst);
            return true;
        } else {
            return false;
        }
    }


    public static boolean setCooldown(Player player, Double cooldown) {
        Cooldown.put(player, cooldown);
        return true;
    }

    public static double getCooldownFromPlayer(Player player) {
        if (Cooldown.containsKey(player)) {
            return Cooldown.getOrDefault(player, null);
        } else {
            throw new NullPointerException();
        }
    }

    public static boolean setTemperature(Player player, Double z) {
        if (z <= 10 && z >= -10) {
            String format = new DecimalFormat("##.##").format(z);
            z = Double.parseDouble(format);
            Temperature.put(player, z);
            return true;
        } else {
            return false;
        }
    }

    public static double getTemperatureFromPlayer(Player player) {
        if (Temperature.containsKey(player)) {
            return Temperature.getOrDefault(player, null);
        } else {
            throw new NullPointerException();
        }
    }

    public static boolean displayActionBar(Player player, Integer thirst, Double temperature, Priority priority) {
        if (!checkBlockedPriorityFromPlayer(player, priority)) {
            if (thirst <= MaxThirst) {
                StringBuilder message = new StringBuilder();
                message.append(ChatColor.BLUE + "" + ChatColor.BOLD + "[ ");
                for (int x = 0; x < MaxBarDisplay; x++) {
                    String m;
                    if (x >= ((float) thirst / MaxThirst) * MaxBarDisplay) {
                        if ((float) x / 10 == x / 10) {
                            m = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "|";
                        } else {
                            m = ChatColor.GRAY + "|";
                        }
                    } else {
                        if ((float) x / 10 == x / 10) {
                            m = ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "|";
                        } else {
                            m = ChatColor.DARK_BLUE + "|";
                        }
                    }
                    message.append(m);
                }
                message.append(ChatColor.BLUE + "" + ChatColor.BOLD + " ]");
                //Temperature
                message.append("   ");
                message.append(ChatColor.GOLD + "" + ChatColor.BOLD + "[ " + ChatColor.RESET);
                int temperatureFormat = (int) (temperature / 10 * MaxTemperatureMultiply / 2);
                for (int z = -MaxTemperatureMultiply / 2; z < MaxTemperatureMultiply / 2; z++) {
                    String m = "";
                    if (z >= (float) -100 / 100 * MaxTemperatureMultiply / 2 && z < -(float) 75 / 100 * MaxTemperatureMultiply / 2) {
                        m = ChatColor.BLUE + "";
                    }
                    if (z >= (float) -75 / 100 * MaxTemperatureMultiply / 2 && z < (float) -50 / 100 * MaxTemperatureMultiply / 2) {
                        m = ChatColor.DARK_AQUA + "";
                    }
                    if (z >= (float) -50 / 100 * MaxTemperatureMultiply / 2 && z < (float) -25 / 100 * MaxTemperatureMultiply / 2) {
                        m = ChatColor.AQUA + "";
                    }
                    if (z >= (float) -25 / 100 * MaxTemperatureMultiply / 2 && z < (float) 25 / 100 * MaxTemperatureMultiply / 2) {
                        m = ChatColor.GREEN + "";
                    }
                    if (z >= (float) -3 / 100 * MaxTemperatureMultiply / 2 && z < (float) 3 / 100 * MaxTemperatureMultiply / 2) {
                        m = ChatColor.DARK_GREEN + "";
                    }
                    if (z >= (float) 25 / 100 * MaxTemperatureMultiply / 2 && z < (float) 50 / 100 * MaxTemperatureMultiply / 2) {
                        m = ChatColor.GOLD + "";
                    }
                    if (z >= (float) 50 / 100 * MaxTemperatureMultiply / 2 && z < (float) 75 / 100 * MaxTemperatureMultiply / 2) {
                        m = ChatColor.RED + "";
                    }
                    if (z >= (float) 75 / 100 * MaxTemperatureMultiply / 2 && z < (float) 100 / 100 * MaxTemperatureMultiply / 2) {
                        m = ChatColor.DARK_RED + "";
                    }
                    if (z == temperatureFormat) {
                        m = m + ChatColor.BOLD + "|";
                    } else {
                        if (z == temperatureFormat - 1) {
                            m = ChatColor.GRAY + "" + ChatColor.BOLD + "[";
                        } else if (z == temperatureFormat + 1) {
                            m = ChatColor.GRAY + "" + ChatColor.BOLD + "]";
                        } else {
                            m = m + "|";
                        }
                    }
                    message.append(m);
                }
                message.append(ChatColor.GOLD + "" + ChatColor.BOLD + " ]");
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message.toString()));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void setBlockedPriorityForPlayer(Player player, Priority priority) {
        if (priority == null) {
            BlockedPriority.remove(player);
        } else {
            BlockedPriority.put(player, priority);
        }
    }

    public static Priority getBlockedPriorityFromPlayer(Player player) {
        return BlockedPriority.getOrDefault(player, null);
    }

    public static boolean checkBlockedPriorityFromPlayer(Player player, Priority priority) {
        if (priority == Priority.LOW && getBlockedPriorityFromPlayer(player) == Priority.HIGH || getBlockedPriorityFromPlayer(player) == Priority.MEDIUM || getBlockedPriorityFromPlayer(player) == Priority.LOW) {
            return true;
        }
        if (priority == Priority.MEDIUM && getBlockedPriorityFromPlayer(player) == Priority.HIGH || getBlockedPriorityFromPlayer(player) == Priority.MEDIUM) {
            return true;
        }
        if (priority == Priority.HIGH && getBlockedPriorityFromPlayer(player) == Priority.HIGH) {
            return true;
        }
        return false;
    }


    public static void setupPlayerThirst(Player player) {
        if (!ThirstBar.containsKey(player)) {
            setThirst(player, MaxThirst);
        }
        if (!Cooldown.containsKey(player)) {
            setCooldown(player, MaxCooldown * TickValue);
        }
        if (!Temperature.containsKey(player)) {
            setTemperature(player, 0D);
        }
        setBlockedPriorityForPlayer(player, null);
    }

    public static void resetPlayerThirst(Player player) {
        setThirst(player, MaxThirst);
        setCooldown(player, MaxCooldown * TickValue);
        setTemperature(player, 0D);
    }

    public static boolean checkCooldownSet(Player player) {
        if (getCooldownFromPlayer(player) <= 0) {
            setCooldown(player, MaxCooldown * TickValue);
            setThirst(player, getThirstFromPlayer(player) - ThirstDecrease);
            return true;
        } else {
            if (getCooldownFromPlayer(player) >= MaxCooldown * TickValue * 2) {
                setCooldown(player, MaxCooldown * TickValue);
                setThirst(player, getThirstFromPlayer(player) + ThirstDecrease);
            }
            return false;
        }
    }

    public static boolean checkCooldown(Player player) {
        return getCooldownFromPlayer(player) <= 0;
    }


    public static boolean checkThirstSet(Player player) {
        if (getThirstFromPlayer(player) <= 0) {
            if (!DieBar.containsKey(player)) {
                DieBar.put(player, DieInterval);
            } else {
                if (DieBar.get(player) <= 0) {
                    player.damage(DieBaseDamage);
                    DieBar.put(player, DieInterval * TickValue);
                } else {
                    DieBar.put(player, DieBar.get(player) - 1);
                }
            }
            setThirst(player, 0);
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkThirst(Player player) {
        return getThirstFromPlayer(player) <= 0;
    }


    public static void addThirst(Player player, Integer thirst) {
        if (getTemperatureFromPlayer(player) > 0 && getThirstFromPlayer(player) != MaxThirst) {
            setTemperature(player, getTemperatureFromPlayer(player) - 0.2);
        }
        if (getTemperatureFromPlayer(player) < 0 && getThirstFromPlayer(player) != MaxThirst) {
            setTemperature(player, getTemperatureFromPlayer(player) + 0.2);
        }
        setBlockedPriorityForPlayer(player, Priority.LOW);
        new BukkitRunnable() {

            int i = getThirstFromPlayer(player);
            boolean finshed = false;

            @Override
            public void run() {
                i++;
                displayActionBar(player, i, getTemperatureFromPlayer(player), Priority.MEDIUM);
                if (finshed) {
                    setBlockedPriorityForPlayer(player, null);
                    if (getThirstFromPlayer(player) + thirst >= MaxThirst) {
                        setThirst(player, MaxThirst);
                    } else {
                        setThirst(player, getThirstFromPlayer(player) + thirst);
                    }
                    cancel();
                }
                if (i == getThirstFromPlayer(player) + thirst || i == MaxThirst) {
                    i = i - 1;
                    finshed = true;
                }
            }
        }.runTaskTimer(plugin, 4L, 0L);
    }

    public static void removeThirst(Player player, Integer thirst) {
        setBlockedPriorityForPlayer(player, Priority.LOW);
        new BukkitRunnable() {

            int i = getThirstFromPlayer(player);
            boolean finshed = false;

            @Override
            public void run() {
                i = i - 1;
                displayActionBar(player, i, getTemperatureFromPlayer(player), Priority.MEDIUM);
                if (finshed) {
                    setBlockedPriorityForPlayer(player, null);
                    if (getThirstFromPlayer(player) - thirst >= MaxThirst) {
                        setThirst(player, MaxThirst);
                    } else {
                        setThirst(player, getThirstFromPlayer(player) - thirst);
                    }
                    cancel();
                }
                if (i == getThirstFromPlayer(player) - thirst || i == 0) {
                    i = i + 1;
                    finshed = true;
                }
            }
        }.runTaskTimer(plugin, 4L, 0L);
    }


    public static double isPoisoned(int chance) {
        double random = Math.random();
        if (random * 100 < chance) {
            return 0;
        } else {
            return Math.random();
        }
    }

    public static void poisonSet(Player player, int force) {
        if (force == 1) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10 * TickValue, 1));
        }
        if (force == 2) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * TickValue, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * TickValue, 1));
        }
        if (force == 3) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 30 * TickValue, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * TickValue, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 30 * TickValue, 1));
        }
        if (force == 4) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 35 * TickValue, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 25 * TickValue, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 35 * TickValue, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10 * TickValue, 1));
        }
        if (force == 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 40 * TickValue, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30 * TickValue, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40 * TickValue, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 15 * TickValue, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * TickValue, 255));

        }

    }

    public static double poisonCompact(Player player, int chance) {
        double poi = isPoisoned(chance);
        if (poi != 0) {
            poisonSet(player, (int) Math.round(poi * 5));
        }
        return poi;
    }

    public static ItemStack createPurifiedWater(int chance) {
        ItemStack item = new ItemStack(Material.POTION);
        Potion potion = Potion.fromItemStack(item);
        potion.setType(PotionType.WATER);
        item = potion.toItemStack(1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Purified water bottle");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + chance + "% of chance to be infected");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        return item;
    }
}
