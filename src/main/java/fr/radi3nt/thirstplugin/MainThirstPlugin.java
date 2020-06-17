package fr.radi3nt.thirstplugin;

import fr.radi3nt.thirstplugin.event.ClickOnWater;
import fr.radi3nt.thirstplugin.event.ConsumeItemEvent;
import fr.radi3nt.thirstplugin.event.OnDeathEvent;
import fr.radi3nt.thirstplugin.timer.Runner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class MainThirstPlugin extends JavaPlugin {

    //--------------------------------------------------//
    public static final String VERSION = "1.0";
    private final ConsoleCommandSender console = Bukkit.getConsoleSender();
    //--------------------------------------------------//




    @Override
    public void onEnable() {
        console.sendMessage(ChatColor.GOLD + "[CustomMobs] " + ChatColor.YELLOW + "Starting up !");
        console.sendMessage(ChatColor.GOLD + "[CustomMobs] " + ChatColor.YELLOW + "CustomMobs Plugin by " + ChatColor.AQUA + ChatColor.BOLD + "Radi3nt");
        console.sendMessage(ChatColor.GOLD + "[CustomMobs] " + ChatColor.YELLOW + "If you have any issues, please report it");

        RegisterEvents();
        console.sendMessage(ChatColor.GOLD + "[CustomMobs] " + ChatColor.RED + "Registered Events");
        RegisterCommands();
        console.sendMessage(ChatColor.GOLD + "[CustomMobs] " + ChatColor.RED + "Registered Commands");
        RegisterRunnables();
        console.sendMessage(ChatColor.GOLD + "[CustomMobs] " + ChatColor.RED + "Registered Runnables");

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getConfig().set("version", VERSION);

    }

    private void RegisterRunnables() {
        Runner run = new Runner();
        run.runTaskTimer(this, 1L, 1L);
    }

    private void RegisterCommands() {

    }

    private void RegisterEvents() {
        getServer().getPluginManager().registerEvents(new OnDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new ClickOnWater(), this);
        getServer().getPluginManager().registerEvents(new ConsumeItemEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
