package fr.radi3nt.MineClimate.event;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;

import static fr.radi3nt.MineClimate.ClimateAPI.*;

public class OnPlayerJoin implements Listener {

    @EventHandler
    public void OnPlayerLeave(PlayerJoinEvent e) {
        File locations = new File("plugins/MineClimate", "data.yml");
        if (!locations.exists()) {
            try {
                locations.createNewFile();
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
        FileConfiguration loc = YamlConfiguration.loadConfiguration(locations);
        Player player = e.getPlayer();
        if (loc.get("Players." + player.getName()) != null) {
            setThirst(player, loc.getInt("Players." + player.getName() + ".thirst"));
            setTemperature(player, loc.getDouble("Players." + player.getName() + ".temperature"));
        }
    }

}
