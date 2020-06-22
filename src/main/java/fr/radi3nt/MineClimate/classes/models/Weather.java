package fr.radi3nt.MineClimate.classes.models;

import java.util.*;

public enum Weather {

    BEAUTIFUL("Beautiful",
            false, false,
            Arrays.asList(Season.SPRING, Season.SUMMER)),
    BREEZY("Breezy",
            false, false,
            Arrays.asList(Season.SPRING, Season.AUTUMN)),
    CHILLY("Chilly",
            false, false,
            Collections.singletonList(Season.SPRING)),
    RAINY("Rainy",
            false, true,
            Arrays.asList(Season.SPRING, Season.AUTUMN, Season.WINTER)),
    SCORCHING("Scorching",
            false, false,
            Collections.singletonList(Season.SUMMER)),
    HOT("Hot",
            false, false,
            Collections.singletonList(Season.SUMMER)),
    WARM("Warm",
            false, false,
            Collections.singletonList(Season.SUMMER)),
    COLD("Cold",
            false, false,
            Arrays.asList(Season.AUTUMN, Season.WINTER)),
    STORMY("Stormy",
            true, true,
            Collections.singletonList(Season.AUTUMN)),
    FREEZING("Freezing",
            true, false,
            Collections.singletonList(Season.WINTER)),
    SNOWY("Snowy",
            false, true,
            Collections.singletonList(Season.WINTER)),
    NIGHT("Calm",
            false, false);

    private static final Random random = new Random();

    private final String name; // Name shown to players
    private final boolean catastrophic; // Is there is a high risk of this weather killing a player?
    private final boolean storm;
    private final List<Season> seasons; // List of seasons this weather can be triggered on

    Weather(String name, boolean catastrophic, boolean storm, List<Season> seasons) {
        this.name = name;
        this.catastrophic = catastrophic;
        this.storm = storm;
        this.seasons = seasons;
    }

    Weather(String name, boolean catastrophic, boolean storm) {
        this.name = name;
        this.catastrophic = catastrophic;
        this.storm = storm;
        this.seasons = Collections.emptyList();
    }

    public static Weather fromName(String name) {
        for (Weather weather : values()) {
            if (weather.getName().equalsIgnoreCase(name.toLowerCase())) {
                return weather;
            }
        }

        return null;
    }

    public static Weather randomWeather() {
        return values()[random.nextInt(values().length)];
    }

    public static Weather randomWeather(Season season) {
        List<Weather> applicableWeathers = new ArrayList<>();
        for (Weather weather : values()) {
            if (weather.seasons.contains(season)) { // Whether the weather can be used with the season
                applicableWeathers.add(weather);
            }
        }

        return applicableWeathers.get(random.nextInt(applicableWeathers.size()));
    }

    public static List<String> getWeatherList() {
        List<String> list = new ArrayList<>();
        for (Weather weather : Weather.values()) {
            list.add(weather.getName().toLowerCase());
        }
        return list;
    }

    public String getName() {
        return name;
    }

    public boolean isCatastrophic() {
        return catastrophic;
    }

    public boolean isStorm() {
        return storm;
    }

    public List<Season> getAffectedSeasons() {
        return seasons;
    }

}
