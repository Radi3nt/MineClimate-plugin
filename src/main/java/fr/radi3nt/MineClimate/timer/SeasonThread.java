package fr.radi3nt.MineClimate.timer;

import fr.radi3nt.MineClimate.classes.models.Season;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class SeasonThread extends BukkitRunnable {

    public static Season SeasonValue = Season.SPRING;
    public static int TimeForSeasons = 0;



    @Override
    public void run() {
        File locations = new File("plugins/MineClimate", "data.yml");
        if (!locations.exists()) {
            try {
                locations.createNewFile();
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
        FileConfiguration loc = YamlConfiguration.loadConfiguration(locations);
        loc.set("Season.season", String.valueOf(SeasonValue));
        loc.set("Season.time", TimeForSeasons);

        if (TimeForSeasons==24000*20) {
            SeasonValue = Season.next(SeasonValue);
        } else {
            TimeForSeasons++;
        }
        try {
            loc.save(locations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
