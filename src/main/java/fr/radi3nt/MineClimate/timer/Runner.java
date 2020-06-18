package fr.radi3nt.MineClimate.timer;

import fr.radi3nt.MineClimate.classes.Priority;
import fr.radi3nt.MineClimate.classes.Season;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

import static fr.radi3nt.MineClimate.ClimateAPI.*;
import static fr.radi3nt.MineClimate.event.ClickOnWater.CooldownFromDrinking;
import static org.bukkit.Bukkit.getServer;

public class Runner extends BukkitRunnable {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static HashMap<Player, Integer> DieBar = new HashMap<>();

    static int interval = 0;
    static int interval1 = 0;
    static int NegativeSeasonValue = 0;


    @Override
    public void run() {
        interval++;
        interval1++;


        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : players) {

            setupPlayerThirst(player);

            /////////// Logic \\\\\\\\\\

            checkCooldownSet(player);

            checkThirstSet(player);


            ///////// TEMPERATURE \\\\\\\\\

            Biome playerbiome = player.getLocation().getBlock().getBiome();

            HashMap<Biome, Double> TemperaturesBiomes = new HashMap<>();
            if (interval == TickValue / 2) {
                if (getTemperatureFromPlayer(player) > 6) {
                    player.damage(getTemperatureFromPlayer(player) / 4);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 3, true, false));
                }
                if (getTemperatureFromPlayer(player) > 9 && getTemperatureFromPlayer(player) < 10) {
                    player.setFireTicks(20);
                }
                if (getTemperatureFromPlayer(player) < -6) {
                    player.damage(getTemperatureFromPlayer(player) * -1 / 4);
                }
                if (getTemperatureFromPlayer(player) < -9 && getTemperatureFromPlayer(player) > -10) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 3, true, false));
                }



                setTemperature(player, getTemperatureFromPlayer(player) + (player.getLocation().getBlock().getTemperature() - 0.8) / 20);

                if (player.getLocation().getBlock().getType().equals(Material.WATER)) {
                    setTemperature(player, getTemperatureFromPlayer(player) - 0.05);
                }
                if (interval1 == TickValue * 2) {
                    if (!isday(player)) {
                        setTemperature(player, getTemperatureFromPlayer(player) - NightShift*2+(NegativeSeasonValue));
                    } else {
                        if (player.getLocation().getWorld().getTime()>3500 && player.getLocation().getWorld().getTime()<9500) {
                            Location location = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX(), player.getLocation().getWorld().getHighestBlockYAt(player.getLocation()), player.getLocation().getBlockZ());
                            if (player.getLocation().getWorld().getHighestBlockYAt(player.getLocation()) < player.getLocation().getBlockY() || location.getBlock().getType().equals(Material.GLASS) || location.getBlock().getType().equals(Material.BARRIER)) {
                                setTemperature(player, getTemperatureFromPlayer(player) - DayShift + (NegativeSeasonValue));
                            }
                        }
                    }
                    ItemStack[] armor = player.getInventory().getArmorContents();
                    for (ItemStack armorpiece : armor) {
                        if (armorpiece != null && !armorpiece.getType().equals(Material.AIR)) {
                            setTemperature(player, getTemperatureFromPlayer(player) + ((float) armorpiece.getDurability()/armorpiece.getType().getMaxDurability()*0.1));
                            setCooldown(player, getCooldownFromPlayer(player) - ((float) armorpiece.getDurability()/armorpiece.getType().getMaxDurability()*100)/20);
                        }
                    }
                }
                if (player.getLocation().getWorld().hasStorm()) {
                    setTemperature(player, getTemperatureFromPlayer(player) - RainShift *2+(NegativeSeasonValue));
                }
            }
            if (getTemperatureFromPlayer(player) > 10) {
                setTemperature(player, 10D);
            }
            if (getTemperatureFromPlayer(player) < -10) {
                setTemperature(player, -10D);
            }

            HashMap<Material, Double> hotMaterials = new HashMap<>();
            hotMaterials.put(Material.TORCH, 0.01);
            hotMaterials.put(Material.WALL_TORCH, 0.01);
            hotMaterials.put(Material.CAMPFIRE, 0.2);
            hotMaterials.put(Material.FIRE, 0.2);
            hotMaterials.put(Material.BEACON, 0.07);
            // Check if source of heat
            Double furnaceHeat = 0.25;

            HashMap<Material, Integer> checkedMaterials = new HashMap<>();
            if (interval >= TickValue / 2) {
                for (Location blockloc : generateSphere(player.getLocation(), 6)) {
                    Material type = blockloc.getBlock().getType();
                    if (blockloc.getBlock().getType().equals(Material.FURNACE) || blockloc.getBlock().getType().equals(Material.BLAST_FURNACE)) {
                        Furnace oldFurnace = (Furnace) blockloc.getBlock().getState();
                        if (oldFurnace.getBurnTime()!=0) {
                            int number = blocksFromTwoPoints(player.getLocation().getBlock().getLocation(), blockloc.getBlock().getLocation()).size();

                            if (player.isSneaking()) {
                                setTemperature(player, getTemperatureFromPlayer(player) + furnaceHeat / (number * checkedMaterials.getOrDefault(type, 1) * 2.25));
                                setCooldown(player, getCooldownFromPlayer(player) - furnaceHeat / (number * checkedMaterials.getOrDefault(type, 1) * 2.25));
                            } else {
                                setTemperature(player, getTemperatureFromPlayer(player) + furnaceHeat / (number * checkedMaterials.getOrDefault(type, 1) * 4));
                                setCooldown(player, getCooldownFromPlayer(player) - furnaceHeat / (number * checkedMaterials.getOrDefault(type, 1) * 4));
                            }
                            checkedMaterials.put(type, checkedMaterials.getOrDefault(type, 1) + 1);
                        }
                    }
                    if (hotMaterials.containsKey(type)) {
                        int number = blocksFromTwoPoints(player.getLocation().getBlock().getLocation(), blockloc.getBlock().getLocation()).size();

                        if (player.isSneaking()) {
                            setTemperature(player, getTemperatureFromPlayer(player) + hotMaterials.get(type) / (number * checkedMaterials.getOrDefault(type, 1) * 2.25));
                            setCooldown(player, getCooldownFromPlayer(player) - hotMaterials.get(type) / (number * checkedMaterials.getOrDefault(type, 1) * 2.25));
                        } else {
                            setTemperature(player, getTemperatureFromPlayer(player) + hotMaterials.get(type) / (number * checkedMaterials.getOrDefault(type, 1) * 4));
                            setCooldown(player, getCooldownFromPlayer(player) - hotMaterials.get(type) / (number * checkedMaterials.getOrDefault(type, 1) * 4));
                        }
                        checkedMaterials.put(type, checkedMaterials.getOrDefault(type, 1) + 1);
                    }
                }
            }


            /////////// Cooldown \\\\\\\\\\\\


            HashMap<Biome, Double> ThirstBiomes = new HashMap<>();
            ThirstBiomes.put(Biome.DESERT, -0.1);
            ThirstBiomes.put(Biome.DESERT_HILLS, -0.1);
            ThirstBiomes.put(Biome.DESERT_LAKES, -0.1);
            ThirstBiomes.put(Biome.SNOWY_MOUNTAINS, 0.1);
            ThirstBiomes.put(Biome.SNOWY_BEACH, 0.1);
            ThirstBiomes.put(Biome.SNOWY_TAIGA, 0.1);
            ThirstBiomes.put(Biome.SNOWY_TAIGA_HILLS, 0.1);
            ThirstBiomes.put(Biome.SNOWY_TAIGA_MOUNTAINS, 0.1);
            ThirstBiomes.put(Biome.SNOWY_TUNDRA, 0.1);

            if (player.getVelocity().normalize().getX() == 0 && player.getVelocity().normalize().getZ() == 0) {
                setCooldown(player, getCooldownFromPlayer(player) - CooldownDecreaseAFK);
            } else {
                if (player.isSprinting()) {
                    setCooldown(player, getCooldownFromPlayer(player) - (CooldownDecreaseSprint));
                } else {
                    if (player.isSneaking()) {
                        setCooldown(player, getCooldownFromPlayer(player) + CooldownIncreaseSneak);
                    } else {
                        setCooldown(player, getCooldownFromPlayer(player) - CooldownDecreaseWalk);
                    }
                }
            }
            if (player.isHandRaised()) {
                setCooldown(player, getCooldownFromPlayer(player) - CooldownDecreaseWalk);
            }

            setCooldown(player, getCooldownFromPlayer(player) + ThirstBiomes.getOrDefault(playerbiome, 0D));





            //DISPLAY\\
            displayActionBar(player, getThirstFromPlayer(player), getTemperatureFromPlayer(player), Priority.LOW);


            //Other\\
            if (CooldownFromDrinking.containsKey(player)) {
                if (CooldownFromDrinking.get(player) <= 0) {
                    CooldownFromDrinking.remove(player);
                } else {
                    CooldownFromDrinking.put(player, CooldownFromDrinking.get(player) - 1);
                }
            }
        }
        if (interval == TickValue / 2) {
            interval = 0;
        }
        if (interval1 == TickValue * 2) {
            interval1 = 0;
        }
    }

    public static ArrayList<Location> generateSphere(Location center, int radius) {
        ArrayList<Location> circlesBlocks = new ArrayList<>();
        int bX = center.getBlockX();
        int bY = center.getBlockY();
        int bZ = center.getBlockZ();

        for (int x = bX - radius; x <= bX + radius; x++) {
            for (int y = bY - radius; y <= bY + radius; y++) {
                for (int z = bZ - radius; z <= bZ + radius; z++) {
                    double distance = ((bX - x) * (bX - x) + ((bZ - z) * (bZ - z)) + ((bY - y) * (bY - y)));
                    if (distance < radius * radius) {
                        Location block = new Location(center.getWorld(), x, y, z);
                        circlesBlocks.add(block);
                    }
                }
            }
        }
        return circlesBlocks;
    }

    public static ArrayList<Block> blocksFromTwoPoints(Location loc1, Location loc2) {
        ArrayList<Block> blocks = new ArrayList();

        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    public boolean isday(Player player) {
        long time = player.getLocation().getWorld().getTime();

        return time < 12300 || time > 23850;
    }
    

}
