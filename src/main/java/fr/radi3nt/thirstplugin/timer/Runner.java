package fr.radi3nt.thirstplugin.timer;

import fr.radi3nt.thirstplugin.classes.Priority;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

import static fr.radi3nt.thirstplugin.ThirstAPI.*;
import static fr.radi3nt.thirstplugin.event.ClickOnWater.CooldownFromDrinking;

public class Runner extends BukkitRunnable {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static HashMap<Player, Integer> DieBar = new HashMap<>();

    static int interval = 0;
    static int interval1 = 0;



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
            if (interval==TickValue/2) {
                setTemperature(player, getTemperatureFromPlayer(player) + (player.getLocation().getBlock().getTemperature()-0.6)/20);

                if (getTemperatureFromPlayer(player) < 5 && getTemperatureFromPlayer(player) > 2 && getThirstFromPlayer(player) <= 0) {
                    setTemperature(player, getTemperatureFromPlayer(player) - 0.05);
                    if (interval1==TickValue*2) {
                        removeThirst(player, getThirstFromPlayer(player)/5);
                    }
                }
                if (getTemperatureFromPlayer(player) > -5 && getTemperatureFromPlayer(player) < 2 && getThirstFromPlayer(player) <= 0) {
                    setTemperature(player, getTemperatureFromPlayer(player) + 0.05);
                    if (interval1 == TickValue * 2) {
                        removeThirst(player, getThirstFromPlayer(player)/5);
                    }
                }
                if (getTemperatureFromPlayer(player) > 6 && getTemperatureFromPlayer(player) < 9) {
                    player.damage(getTemperatureFromPlayer(player)/4);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 3, true, false));
                }
                if (getTemperatureFromPlayer(player) > 9 && getTemperatureFromPlayer(player) < 10) {
                    player.setFireTicks(20);
                }
                if (getTemperatureFromPlayer(player) < -6) {
                    player.damage(getTemperatureFromPlayer(player)*-1/4);
                }
                if (getTemperatureFromPlayer(player) < -9 && getTemperatureFromPlayer(player) > -10) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 3, true, false));
                }
                if (player.getLocation().getBlock().getType().equals(Material.WATER)) {
                    setTemperature(player, getTemperatureFromPlayer(player) - 0.05);
                }
            }
            if (getTemperatureFromPlayer(player) > 10) {
                setTemperature(player, 10D);
            }
            if (getTemperatureFromPlayer(player) < -10) {
                setTemperature(player, -10D);
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
                        setCooldown(player, getCooldownFromPlayer(player) + CooldownIncreaseSneak );
                    } else {
                        setCooldown(player, getCooldownFromPlayer(player) - CooldownDecreaseWalk );
                    }
                }
            }
            if (player.isHandRaised()) {
                setCooldown(player, getCooldownFromPlayer(player) - CooldownDecreaseWalk );
            }

            setCooldown(player, getCooldownFromPlayer(player) + ThirstBiomes.getOrDefault(playerbiome, 0D));


            HashMap<Material, Double> hotMaterials = new HashMap<>();
            hotMaterials.put(Material.TORCH, 0.01);
            hotMaterials.put(Material.WALL_TORCH, 0.01);
            hotMaterials.put(Material.CAMPFIRE, 0.2);
            hotMaterials.put(Material.LEGACY_BURNING_FURNACE, 0.1);
            hotMaterials.put(Material.FIRE, 0.2);
            hotMaterials.put(Material.BEACON, 0.07);
            // Check if source of heat
            Double furnaceHeat = 0.1;

            HashMap<Material, Integer> checkedMaterials = new HashMap<>();
            if (interval==TickValue/2) {
                for (Location blockloc : generateSphere(player.getLocation(), 5)) {
                    Material type = blockloc.getBlock().getType();
                    if (hotMaterials.containsKey(type)) {
                        if (!checkedMaterials.containsKey(type)) {
                            int number = blocksFromTwoPoints(player.getLocation().getBlock().getLocation(), blockloc.getBlock().getLocation()).size();
                            if (player.isSneaking()) {
                                setTemperature(player, getTemperatureFromPlayer(player) + hotMaterials.get(type) / (number * 3));
                                setCooldown(player, getCooldownFromPlayer(player) - hotMaterials.get(type) / (number * 3));
                            } else {
                                setTemperature(player, getTemperatureFromPlayer(player) + hotMaterials.get(type) / (number * 4));
                                setCooldown(player, getCooldownFromPlayer(player) - hotMaterials.get(type) / (number * 4));
                            }
                            checkedMaterials.put(type, checkedMaterials.getOrDefault(type, 0) + 1);

                        } else {
                            int number = blocksFromTwoPoints(player.getLocation().getBlock().getLocation(), blockloc.getBlock().getLocation()).size();

                            if (player.isSneaking()) {
                                setTemperature(player, getTemperatureFromPlayer(player) + hotMaterials.get(type) / (number * checkedMaterials.get(type) * 3));
                                setCooldown(player, getCooldownFromPlayer(player) - hotMaterials.get(type) / (number * checkedMaterials.get(type) * 3));
                            } else {
                                setTemperature(player, getTemperatureFromPlayer(player) + hotMaterials.get(type) / (number * checkedMaterials.get(type) * 4));
                                setCooldown(player, getCooldownFromPlayer(player) - hotMaterials.get(type) / (number * checkedMaterials.get(type) * 4));
                            }
                            checkedMaterials.put(type, checkedMaterials.getOrDefault(type, 0) + 1);
                        }
                    }
                }
            }



            //DISPLAY\\
            displayActionBar(player, getThirstFromPlayer(player), getTemperatureFromPlayer(player), Priority.LOW);


            //Other\\
            if (CooldownFromDrinking.containsKey(player)) {
                if (CooldownFromDrinking.get(player)<=0) {
                    CooldownFromDrinking.remove(player);
                } else {
                    CooldownFromDrinking.put(player, CooldownFromDrinking.get(player) - 1);
                }
            }
        }
        if (interval == TickValue/2) {
            interval = 0;
        }
        if (interval1 == TickValue*2) {
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
                    double distance = ((bX - x) * (bX -x) + ((bZ - z) * (bZ - z)) + ((bY - y) * (bY - y)));
                    if (distance < radius * radius) {
                        Location block = new Location(center.getWorld(), x, y, z);
                        circlesBlocks.add(block);
                    }
                }
            }
        }
        return circlesBlocks;
    }

    public static ArrayList<Block> blocksFromTwoPoints(Location loc1, Location loc2)
    {
        ArrayList<Block> blocks = new ArrayList();

        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

        for(int x = bottomBlockX; x <= topBlockX; x++)
        {
            for(int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for(int y = bottomBlockY; y <= topBlockY; y++)
                {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

}
