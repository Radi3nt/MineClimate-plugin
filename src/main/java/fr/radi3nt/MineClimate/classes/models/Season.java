package fr.radi3nt.MineClimate.classes.models;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Season {
    SPRING("Spring", ChatColor.GREEN, 70),
    SUMMER("Summer", ChatColor.DARK_GREEN, 10),
    AUTUMN("Autumn", ChatColor.YELLOW, 45),
    WINTER("Winter", ChatColor.WHITE, 80);

    private final String name;
    private final int chanceToRain;
    private final ChatColor color;

    Season(String name, ChatColor color, Integer chanceToRain) {
        this.name = name;
        this.color = color;
        this.chanceToRain = chanceToRain;
    }

    public static Season next(Season currentSeason) {
        Validate.notNull(currentSeason);

        List<Season> seasons = Arrays.asList(values());
        int index = seasons.indexOf(currentSeason);
        if (index + 1 >= seasons.size()) { // If there are no more seasons after the current one
            return seasons.get(0); // Return the start of the list
        } else {
            return seasons.get(index + 1); // Return the next season
        }
    }


    public static List<String> getSeasonsList() {
        List<String> list = new ArrayList<>();
        for (Season season : Season.values()) {
            list.add(season.getName().toLowerCase());
        }
        return list;
    }

    public static Double getTemperatureForSeasonByTime(Season season, Integer time) {
        double relativeSeasonTemperature = 0;
        double relativeSeasonTime = 0;

        if (time < 24000 * 31 / 2) {
            relativeSeasonTime = ((float) time / ((float) 24000 * 31 / 2));
        } else {
            relativeSeasonTime = ((float) 24000 * 31 / 2 - time / ((float) 24000 * 31 / 2));
        }
        //relativeSeasonTime is on 1 so ...
        switch (season) {
            case SPRING:
                relativeSeasonTemperature = relativeSeasonTime * 0.1;
                break;
            case SUMMER:
                relativeSeasonTemperature = relativeSeasonTime * 0.5;
                break;
            case AUTUMN:
                relativeSeasonTemperature = -relativeSeasonTime * 0.1;
                break;
            case WINTER:
                relativeSeasonTemperature = -relativeSeasonTime * 0.5;
                break;
        }
        return relativeSeasonTemperature;
    }

    public static Double getTemperatureForSeasonByDay(Season season, Integer day) {
        double relativeSeasonTemperature = 0;
        double relativeSeasonTime = 0;

        if (day < 31 * 4 / 2) {
            relativeSeasonTime = ((float) day / ((float) 31 * 4 / 2));
        } else {
            relativeSeasonTime = ((float) 31 * 4 / 2 - day / ((float) 31 * 4 / 2));
        }
        //relativeSeasonTime is on 1 so ...
        switch (season) {
            case SPRING:
                relativeSeasonTemperature = relativeSeasonTime * 0.1;
                break;
            case SUMMER:
                relativeSeasonTemperature = relativeSeasonTime * 0.5;
                break;
            case AUTUMN:
                relativeSeasonTemperature = -relativeSeasonTime * 0.1;
                break;
            case WINTER:
                relativeSeasonTemperature = -relativeSeasonTime * 0.5;
                break;
        }
        return relativeSeasonTemperature;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public Integer getChanceToRain() {
        return chanceToRain;
    }

    @Override
    public String toString() {
        return color + name;
    }


}
