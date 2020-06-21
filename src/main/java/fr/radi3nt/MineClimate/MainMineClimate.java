package fr.radi3nt.MineClimate;

import fr.radi3nt.MineClimate.classes.Season;
import fr.radi3nt.MineClimate.classes.enchants.CustomsEnchants;
import fr.radi3nt.MineClimate.classes.enchants.EnchantmentWarper;
import fr.radi3nt.MineClimate.classes.enchants.Glow;
import fr.radi3nt.MineClimate.event.*;
import fr.radi3nt.MineClimate.event.crafts.CraftPurifiedBottle;
import fr.radi3nt.MineClimate.event.crafts.OnCraftEvent;
import fr.radi3nt.MineClimate.event.crafts.PrepareItemCraftEvent;
import fr.radi3nt.MineClimate.timer.Runner;
import fr.radi3nt.MineClimate.timer.SeasonThread;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static fr.radi3nt.MineClimate.ClimateAPI.*;
import static fr.radi3nt.MineClimate.timer.SeasonThread.*;

public final class MainMineClimate extends JavaPlugin {

    //--------------------------------------------------//
    public static final String VERSION = "1.0";
    private final ConsoleCommandSender console = Bukkit.getConsoleSender();
    //--------------------------------------------------//


    @Override
    public void onEnable() {
        console.sendMessage(ChatColor.GOLD + "[MineClimate] " + ChatColor.YELLOW + "Starting up !");
        console.sendMessage(ChatColor.GOLD + "[MineClimate] " + ChatColor.YELLOW + "MineClimate Plugin by " + ChatColor.AQUA + ChatColor.BOLD + "Radi3nt");
        console.sendMessage(ChatColor.GOLD + "[MineClimate] " + ChatColor.YELLOW + "If you have any issues, please report it");

        RegisterEvents();
        console.sendMessage(ChatColor.GOLD + "[MineClimate] " + ChatColor.RED + "Registered Events");
        RegisterCommands();
        console.sendMessage(ChatColor.GOLD + "[MineClimate] " + ChatColor.RED + "Registered Commands");
        RegisterRunnables();
        console.sendMessage(ChatColor.GOLD + "[MineClimate] " + ChatColor.RED + "Registered Runnables");

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getConfig().set("version", VERSION);

        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : players) {
            File locations = new File("plugins/MineClimate", "data.yml");
            if (!locations.exists()) {
                try {
                    locations.createNewFile();
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }
            FileConfiguration loc = YamlConfiguration.loadConfiguration(locations);
            if (loc.get("Players." + player.getName()) != null) {
                setThirst(player, loc.getInt("Players." + player.getName() + ".thirst"));
                setTemperature(player, loc.getDouble("Players." + player.getName() + ".temperature"));
            }
        }


        Potion item = Potion.fromItemStack(new ItemStack(Material.POTION));
        item.setType(PotionType.WATER);
        getServer().addRecipe(new FurnaceRecipe(item.toItemStack(1), Material.POTION));

        ItemStack ozzy = createOzzyLiner();
        ShapedRecipe ozzyRecipe = new ShapedRecipe(ozzy);
        ozzyRecipe.shape("MAI","MTI","MEI");
        ozzyRecipe.setIngredient('M', Material.MAGMA_BLOCK);
        ozzyRecipe.setIngredient('A', Material.AIR);
        ozzyRecipe.setIngredient('I', Material.ICE);
        ozzyRecipe.setIngredient('T', Material.LEATHER_CHESTPLATE);
        ozzyRecipe.setIngredient('E', Material.EMERALD_BLOCK);
        getServer().addRecipe(ozzyRecipe);

        ItemStack seasonClock = createSeasonClock();
        ShapedRecipe seasonClockRecipe = new ShapedRecipe(seasonClock);
        seasonClockRecipe.shape("GDG","DED","GDG");
        seasonClockRecipe.setIngredient('G', Material.GOLD_BLOCK);
        seasonClockRecipe.setIngredient('D', Material.DIAMOND);
        seasonClockRecipe.setIngredient('E', Material.EMERALD);
        getServer().addRecipe(seasonClockRecipe);

        CustomsEnchants.register();
        registerGlow();

    }

    private void RegisterRunnables() {
        Runner run = new Runner();
        run.runTaskTimer(this, 1L, 1L);

        File locations = new File("plugins/MineClimate", "data.yml");
        if (!locations.exists()) {
            try {
                locations.createNewFile();
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
        FileConfiguration loc = YamlConfiguration.loadConfiguration(locations);
        SeasonThread seasonThread = new SeasonThread();
        seasonThread.runTaskTimer(plugin, 1L, 1L);
        if (loc.get("Season")!=null) {
            SeasonValue = Season.valueOf(loc.getString("Season.season"));
            TimeForSeasons = loc.getInt("Season.time");
        }
    }

    private void RegisterCommands() {

    }

    private void RegisterEvents() {
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new OnCraftEvent(), this);
        getServer().getPluginManager().registerEvents(new OnDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new ClickOnWater(), this);
        getServer().getPluginManager().registerEvents(new ConsumeItemEvent(), this);
        getServer().getPluginManager().registerEvents(new CraftPurifiedBottle(), this);
        getServer().getPluginManager().registerEvents(new OnRegenerateEvent(), this);
        getServer().getPluginManager().registerEvents(new PrepareItemCraftEvent(), this);
        getServer().getPluginManager().registerEvents(new OnRegenerateEvent(), this);
        getServer().getPluginManager().registerEvents(new OnRespawnEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(new NamespacedKey(plugin, "glow"));
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
