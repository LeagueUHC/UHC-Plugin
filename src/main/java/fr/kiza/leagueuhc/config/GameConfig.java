package fr.kiza.leagueuhc.config;

import fr.kiza.leagueuhc.LeagueUHC;

public final class GameConfig {

    private static final LeagueUHC instance = LeagueUHC.getInstance();

    private GameConfig() {}

    public static final String PLUGIN_NAME = "LeagueUHC";
    public static final String DEFAULT_WORLD = "world";
    public static final String GAME_WORLD = "uhc_world";

    public static final int MAX_PLAYERS = 32;

    public static final int COUNTDOWN_SECONDS = 10;

    public static final int UNFREEZE_PLAYER_SECONDS = 0;
    public static final int CHAMPION_ASSIGN_DELAY_SECONDS = 10;
    public static final int PVP_ENABLE_SECONDS = 20;

    public static final int FIRST_HEAL_MINUTES = 10;
    public static final int SECOND_HEAL_MINUTES = 20;

    public static final int RECONNECT_TIMEOUT_SECONDS = 300;

    public static final int MAP_RADIUS = 500;
    public static final int BORDER_SIZE = MAP_RADIUS * 2;
    public static final int FOREST_RADIUS = 350;

    public static final double SPAWN_SAFE_RADIUS = MAP_RADIUS - 20;
    public static final double MIN_SPAWN_DISTANCE = 10.0;

    public static final int DAY_DURATION_SECONDS = 300;
    public static final int NIGHT_DURATION_SECONDS = 300;
    public static final int EPISODE_DURATION_SECONDS = DAY_DURATION_SECONDS + NIGHT_DURATION_SECONDS;

    public static final int CHUNKS_PER_TICK_DEFAULT = 6;

    public static final String DATABASE_HOST = instance.getConfig().getString("database.host");
    public static final int DATABASE_PORT = instance.getConfig().getInt("database.port");
    public static final String DATABASE_NAME = instance.getConfig().getString("database.name");
    public static final String DATABASE_USER = instance.getConfig().getString("database.user");
    public static final String DATABASE_PASSWORD = instance.getConfig().getString("database.password");

    public static final double KILL_BASE = 500.0;
    public static final double KILL_ASSIST = 150.0;

    public static final double DRAKE_KILL = 1000.0;

    public static final double QUEST_EASY = 200.0;
    public static final double QUEST_MEDIUM = 500.0;
    public static final double QUEST_HARD = 1000.0;

    public static final double FIRST_BLOOD = 250.0;
    public static final double SHUTDOWN = 300.0;

    public static final long ASSIST_TIME_WINDOW = 10000;

    public static final int POINTS_PER_KILL = 100;
    public static final int POINTS_PER_ASSIST = 50;
    public static final int POINTS_WIN_BONUS = 500;
    public static final int POINTS_PARTICIPATION = 10;
}
