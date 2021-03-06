package fr.radi3nt.MineClimate.timer;

import fr.radi3nt.MineClimate.classes.Priority;
import fr.radi3nt.MineClimate.classes.models.Season;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static fr.radi3nt.MineClimate.ClimateAPI.*;
import static fr.radi3nt.MineClimate.MainMineClimate.SEASON_THREAD;
import static fr.radi3nt.MineClimate.event.ClickOnWater.CooldownFromDrinking;

public class Runner extends BukkitRunnable {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static HashMap<Player, Integer> DieBar = new HashMap<>();
    public static HashMap<Player, ArrayList<FallingBlock>> ArmorMap = new HashMap<>();

    static int NegativeSeasonValue = 0;

    static int interval = 0;
    static int interval1 = 0;
    static int interval2 = 0;

    static int red = 255;
    static int blue = 0;
    static boolean ascend = false;

    int ticks = 0;


    @Override
    public void run() {
        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : players) {
            if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {

                setupPlayerThirst(player);

                /////////// Logic \\\\\\\\\\

                checkCooldownSet(player);

                checkThirstSet(player);


                ///////// TEMPERATURE \\\\\\\\\

                if (ticks - interval * 10 == 10) {
                    // LOGICAL \\
                    if (getTemperatureFromPlayer(player) > 6) {
                        player.damage(getTemperatureFromPlayer(player) / 4);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 3, true, false));
                    }
                    if (getTemperatureFromPlayer(player) > 9) {
                        player.setFireTicks(20);
                    }
                    if (getTemperatureFromPlayer(player) < -6) {
                        player.damage(getTemperatureFromPlayer(player) * -1 / 4);
                        Location playerloc = new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY() - 0.5, player.getLocation().getZ());
                    }
                    if (getTemperatureFromPlayer(player) < -9) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 3, true, false));
                    }
                    // LOGICAL \\


                    setTemperature(player, getTemperatureFromPlayer(player) + (player.getLocation().getBlock().getTemperature() - 0.8) / 20);

                    if (player.getLocation().getBlock().getType().equals(Material.WATER)) {
                        setTemperature(player, getTemperatureFromPlayer(player) - 0.02);
                    }
                    if (player.getLocation().getWorld().hasStorm()) {
                        setTemperature(player, getTemperatureFromPlayer(player) - RainShift * 2 + (NegativeSeasonValue));
                    }

                    // Hot block \\
                    HashMap<Material, Double> hotMaterials = new HashMap<>();
                    Double furnaceHeat = 0.25;
                    try {
                        hotMaterials.put(Material.TORCH, 0.01);
                        hotMaterials.put(Material.WALL_TORCH, 0.01);
                        hotMaterials.put(Material.CARVED_PUMPKIN, 0.02);
                        hotMaterials.put(Material.CAMPFIRE, 0.2);
                        hotMaterials.put(Material.FIRE, 0.2);
                        hotMaterials.put(Material.BEACON, 0.07);
                        hotMaterials.put(Material.MAGMA_BLOCK, 0.5);
                        hotMaterials.put(Material.NETHER_PORTAL, 0.5);
                        hotMaterials.put(Material.LAVA, 1D);
                        hotMaterials.put(Material.SEA_LANTERN, -0.3);
                    } catch (NoSuchFieldError error) {
                    }
                    checkHotBlockAroundPlayer(player, hotMaterials, furnaceHeat);

                    ItemStack[] armor = player.getInventory().getArmorContents();
                    for (ItemStack armorpiece : armor) {
                        if (armorpiece != null && !armorpiece.getType().equals(Material.AIR)) {
                            if (armorpiece.hasItemMeta()) {
                                if (armorpiece.getItemMeta().hasLore()) {
                                    for (int i = 0; i < armorpiece.getItemMeta().getLore().size(); i++) {
                                        String[] fLore = ChatColor.stripColor(armorpiece.getItemMeta().getLore().get(i)).split(" ");
                                        String testLore = fLore[0];

                                        if (testLore.equals(TemperatureEnchant) || armorpiece.getItemMeta().getLore().contains(ChatColor.GOLD + "Ozzy Liner combined")) {
                                            if (getTemperatureFromPlayer(player) > 0) {
                                                setTemperature(player, getTemperatureFromPlayer(player) - 0.07);
                                            }
                                            if (getTemperatureFromPlayer(player) < 0) {
                                                setTemperature(player, getTemperatureFromPlayer(player) + 0.07);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


                // Seasons \\
                double relativeSeasonTemperature = Season.getTemperatureForSeasonByTime(SEASON_THREAD.getCurrentSeason(), SEASON_THREAD.getTimeInSeason());
                //We have our season value !

                if (ticks - interval1 * 20 == 20) {
                    setTemperature(player, getTemperatureFromPlayer(player) + relativeSeasonTemperature);
                    setTemperature(player, getTemperatureFromPlayer(player) + SEASON_THREAD.CurrentWeather.getTemperature());
                }
                if (ticks - interval2 * 60 == 60) {
                    ItemStack[] armor = player.getInventory().getArmorContents();
                    for (ItemStack armorpiece : armor) {
                        if (armorpiece != null && !armorpiece.getType().equals(Material.AIR)) {
                            if (armorpiece.getType() == Material.LEATHER_BOOTS || armorpiece.getType() == Material.LEATHER_LEGGINGS || armorpiece.getType() == Material.LEATHER_CHESTPLATE || armorpiece.getType() == Material.LEATHER_HELMET) {
                                setTemperature(player, getTemperatureFromPlayer(player) + 0.08);
                            } else {
                                setTemperature(player, getTemperatureFromPlayer(player) + ((float) armorpiece.getDurability() / armorpiece.getType().getMaxDurability() * 100) / 1000);
                                setCooldown(player, getCooldownFromPlayer(player) - ((float) armorpiece.getDurability() / armorpiece.getType().getMaxDurability() * 100) / 20);
                            }
                        }
                    }
                }
                if (getTemperatureFromPlayer(player) > 10) {
                    setTemperature(player, 10D);
                }
                if (getTemperatureFromPlayer(player) < -10) {
                    setTemperature(player, -10D);
                }


                /////////// Cooldown \\\\\\\\\\\\


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


                // Special Items \\
                for (int x = 0; x < player.getInventory().getContents().length; x++) {
                    ItemStack[] contents = player.getInventory().getContents();
                    if (contents[x] != null && contents[x].getItemMeta() != null) {
                        ItemMeta itemMeta = contents[x].getItemMeta();
                        if (itemMeta.getDisplayName().equals(TemperatureItemName)) {
                            if (contents[x].getType().equals(Material.LEATHER_CHESTPLATE)) {
                                LeatherArmorMeta armorMeta = (LeatherArmorMeta) contents[x].getItemMeta();
                                armorMeta.setColor(Color.fromRGB(red, 0, blue));
                                armorMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                contents[x].setItemMeta(armorMeta);
                                for (int z = 0; z < player.getInventory().getArmorContents().length; z++) {
                                    ItemStack[] armor = player.getInventory().getArmorContents();
                                    if (armor[z] != null) {
                                        if (armor[z].isSimilar(contents[x])) {
                                            armor[z].setType(Material.AIR);
                                            player.getInventory().setArmorContents(armor);
                                            player.getInventory().addItem(contents[x]);
                                        }
                                    }
                                }
                            }
                        }
                        if (contents[x].getType().equals(Material.CLOCK) && itemMeta.getDisplayName().contains(ChatColor.GOLD + "Season Clock: ")) {
                            itemMeta.setDisplayName(ChatColor.GOLD + "Season Clock: " + ChatColor.BOLD + SEASON_THREAD.getCurrentSeason().toString());
                            contents[x].setItemMeta(itemMeta);
                        }
                    }
                }



                File locations = new File("plugins/MineClimate", "data.yml");
                if (!locations.exists()) {
                    try {
                        locations.createNewFile();
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                }
                FileConfiguration loc = YamlConfiguration.loadConfiguration(locations);
                loc.set("Players." + player.getName() + ".thirst", getThirstFromPlayer(player));
                loc.set("Players." + player.getName() + ".temperature", getTemperatureFromPlayer(player));

                try {
                    loc.save(locations);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (ticks - interval * 10 == 10) {
            interval++;
        }
        if (ticks - interval1 * 20 == 20) {
            interval1++;
        }
        if (ticks - interval2 * 60 == 60) {
            interval2++;
        }
        ticks++;

        if (!ascend) {
            if (blue>=255) {
                ascend=true;
            } else {
                red=red-1;
                blue++;
            }
        } else {
            if (red >= 255) {
                ascend = false;
            } else {
                red++;
                blue = blue - 1;
            }
        }

    }

    private void checkHotBlockAroundPlayer(Player player, HashMap<Material, Double> hotMaterials, Double furnaceHeat) {
        HashMap<Material, Integer> checkedMaterials = new HashMap<>();
        for (Location blockloc : generateSphere(player.getLocation(), 6)) {
            Material type = blockloc.getBlock().getType();
            try {

                if (blockloc.getBlock().getType().equals(Material.FURNACE) || blockloc.getBlock().getType().equals(Material.BLAST_FURNACE)) {
                    Furnace oldFurnace = (Furnace) blockloc.getBlock().getState();
                    if (oldFurnace.getBurnTime() != 0) {
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
            } catch (NoSuchFieldError error) {

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
