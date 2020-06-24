package fr.radi3nt.MineClimate.classes.models;

import java.util.*;

public enum Weather {

    BEAUTIFUL("Beautiful",
            false, false,
            Arrays.asList(Season.SPRING, Season.SUMMER), 0.02),
    BREEZY("Breezy",
            false, false,
            Arrays.asList(Season.SPRING, Season.AUTUMN), 0D),
    CHILLY("Chilly",
            false, false,
            Collections.singletonList(Season.SPRING), 0.01),
    RAINY("Rainy",
            false, true,
            Arrays.asList(Season.SPRING, Season.AUTUMN, Season.WINTER), -0.02),
    SCORCHING("Scorching",
            false, false,
            Collections.singletonList(Season.SUMMER), 0.5),
    HOT("Hot",
            false, false,
            Collections.singletonList(Season.SUMMER), 0.2),
    WARM("Warm",
            false, false,
            Collections.singletonList(Season.SUMMER), 0.03),
    COLD("Cold",
            false, false,
            Arrays.asList(Season.AUTUMN, Season.WINTER), -0.03),
    STORMY("Stormy",
            true, true,
            Collections.singletonList(Season.AUTUMN), -0.02),
    FREEZING("Freezing",
            true, false,
            Collections.singletonList(Season.WINTER), -0.2),
    SNOWY("Snowy",
            false, true,
            Collections.singletonList(Season.WINTER), -0.1),
    NIGHT("Calm",
            false, false, -0.03);

    private static final Random random = new Random();

    private final String name; // Name shown to players
    private final boolean catastrophic; // Is there is a high risk of this weather killing a player?
    private final boolean storm;
    private final List<Season> seasons; // List of seasons this weather can be triggered on
    private final Double temperature;

    Weather(String name, boolean catastrophic, boolean storm, List<Season> seasons, Double temperature) {
        this.name = name;
        this.catastrophic = catastrophic;
        this.storm = storm;
        this.seasons = seasons;
        this.temperature = temperature;
    }

    Weather(String name, boolean catastrophic, boolean storm, Double temperature) {
        this.name = name;
        this.catastrophic = catastrophic;
        this.storm = storm;
        this.seasons = Collections.emptyList();
        this.temperature = temperature;
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

    public Double getTemperature() {
        return temperature;
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
