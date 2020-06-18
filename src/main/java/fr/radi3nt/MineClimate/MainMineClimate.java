package fr.radi3nt.MineClimate;

import fr.radi3nt.MineClimate.event.*;
import fr.radi3nt.MineClimate.timer.Runner;
import net.minecraft.server.v1_15_R1.RecipeCrafting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Item;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

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


        Potion item = Potion.fromItemStack(new ItemStack(Material.POTION));
        item.setType(PotionType.WATER);
        getServer().addRecipe(new FurnaceRecipe(item.toItemStack(1), Material.POTION));

    }

    private void RegisterRunnables() {
        Runner run = new Runner();
        run.runTaskTimer(this, 1L, 1L);
    }

    private void RegisterCommands() {

    }

    private void RegisterEvents() {
        getServer().getPluginManager().registerEvents(new OnCraftEvent(), this);
        getServer().getPluginManager().registerEvents(new OnDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new ClickOnWater(), this);
        getServer().getPluginManager().registerEvents(new ConsumeItemEvent(), this);
        getServer().getPluginManager().registerEvents(new CraftPurifiedBottle(), this);
        getServer().getPluginManager().registerEvents(new OnRegenerateEvent(), this);
        getServer().getPluginManager().registerEvents(new PrepareItemCraftEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
