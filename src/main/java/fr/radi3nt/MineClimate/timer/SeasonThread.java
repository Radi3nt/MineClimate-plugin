package fr.radi3nt.MineClimate.timer;

import fr.radi3nt.MineClimate.classes.models.Season;
import fr.radi3nt.MineClimate.classes.models.Weather;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static fr.radi3nt.MineClimate.classes.models.Weather.randomWeather;

public class SeasonThread extends BukkitRunnable {

    public static Weather CurrentWeather = Weather.NIGHT;
    private static Season SeasonValue = Season.SPRING;
    private static int TimeForSeasons = 0;
    private static int Day = 0;

    public static Season getCurrentSeason() {
        return SeasonValue;
    }

    public static void setCurrentSeason(Season season) {
        SeasonValue = season;
    }

    public static Integer getTimeInSeason() {
        return TimeForSeasons;
    }

    public static Integer getDayInSeason() {
        return Day;
    }

    public static void setDayInSeason(Integer day) {
        Day = day;
    }

    public static void setCurrentTimeInSeason(Integer Time) {
        if (Time >= 0 && Time <= 24000 * 20) {
            TimeForSeasons = Time;
        }
    }

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
        loc.set("Season.season", SeasonValue.getName().toUpperCase());
        loc.set("Season.time", TimeForSeasons);
        loc.set("Season.day", Day);

        if (Day == 31 * 6) {
            SeasonValue = Season.next(SeasonValue);
            Day = 0;
        }
        TimeForSeasons++;
        try {
            loc.save(locations);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Bukkit.getWorlds().get(0).getTime() == 0) {
            Day++;
            for (int i = 0; i < Bukkit.getWorlds().size(); ++i) {
                if (random(1000) <= getCurrentSeason().getChanceToRain()) {
                    Weather newWeather = randomWeather(getCurrentSeason());
                    while (!newWeather.isStorm()) {
                        newWeather = randomWeather(getCurrentSeason());
                    }
                    Bukkit.getWorlds().get(i).setStorm(true);
                    if (newWeather.isCatastrophic()) {
                        Bukkit.getWorlds().get(i).setThundering(false);
                        Bukkit.getWorlds().get(i).setThunderDuration(random(23000));
                    }
                    Bukkit.getWorlds().get(i).setWeatherDuration(random(23000));
                } else {
                    Weather newWeather = randomWeather(getCurrentSeason());
                    while (newWeather.isStorm()) {
                        newWeather = randomWeather(getCurrentSeason());
                    }
                    Bukkit.getWorlds().get(i).setStorm(false);
                    Bukkit.getWorlds().get(i).setThundering(false);
                    Bukkit.getWorlds().get(i).setThundering(false);
                    Bukkit.getWorlds().get(i).setThunderDuration(random(23000));
                    Bukkit.getWorlds().get(i).setWeatherDuration(random(23000));
                }
            }
        } else if (Bukkit.getWorlds().get(0).getTime() == 13000) {
            CurrentWeather = Weather.NIGHT;
        }
    }

    private int random(final int length) {
        return new Random().nextInt(length) + 1;
    }
}
