package fr.kiza.leagueuhc.core.database.data;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData {

    public static final Map<UUID, PlayerData> PLAYER = new ConcurrentHashMap<>();

    private final UUID uuid;
    private String name;

    private int totalGames;
    private int totalWins;
    private int totalKills;
    private int totalDeaths;
    private int totalAssists;

    private double winRate;
    private double kdRatio;
    private double kdaRatio;

    private int bestKillStreak;
    private long totalPlaytime;
    private int totalDamageDealt;
    private int totalDamageTaken;

    private int totalDrakesKilled;
    private int totalDrakeBuffsObtained;
    private String favoriteChampion;

    private Timestamp firstJoin;
    private Timestamp lastPlayed;

    public PlayerData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.totalGames = 0;
        this.totalWins = 0;
        this.totalKills = 0;
        this.totalDeaths = 0;
        this.totalAssists = 0;
        this.winRate = 0.0;
        this.kdRatio = 0.0;
        this.kdaRatio = 0.0;
        this.bestKillStreak = 0;
        this.totalPlaytime = 0;
        this.totalDamageDealt = 0;
        this.totalDamageTaken = 0;
        this.totalDrakesKilled = 0;
        this.totalDrakeBuffsObtained = 0;
        this.favoriteChampion = null;
        this.firstJoin = new Timestamp(System.currentTimeMillis());
        this.lastPlayed = new Timestamp(System.currentTimeMillis());
    }

    public PlayerData(UUID uuid, String name, int totalGames, int totalWins, int totalKills, int totalDeaths, int totalAssists, double winRate, double kdRatio, double kdaRatio, int bestKillStreak, long totalPlaytime, int totalDamageDealt, int totalDamageTaken, int totalDrakesKilled, int totalDrakeBuffsObtained, String favoriteChampion, Timestamp firstJoin, Timestamp lastPlayed) {
        this.uuid = uuid;
        this.name = name;
        this.totalGames = totalGames;
        this.totalWins = totalWins;
        this.totalKills = totalKills;
        this.totalDeaths = totalDeaths;
        this.totalAssists = totalAssists;
        this.winRate = winRate;
        this.kdRatio = kdRatio;
        this.kdaRatio = kdaRatio;
        this.bestKillStreak = bestKillStreak;
        this.totalPlaytime = totalPlaytime;
        this.totalDamageDealt = totalDamageDealt;
        this.totalDamageTaken = totalDamageTaken;
        this.totalDrakesKilled = totalDrakesKilled;
        this.totalDrakeBuffsObtained = totalDrakeBuffsObtained;
        this.favoriteChampion = favoriteChampion;
        this.firstJoin = firstJoin;
        this.lastPlayed = lastPlayed;
    }

    public void addGame(GameHistoryData data) {
        this.totalGames++;
        if (data.isWon()) this.totalWins++;
        this.totalKills += data.getKills();
        this.totalDeaths += data.getDeaths();
        this.totalAssists += data.getAssists();
        this.totalDamageDealt += data.getDamageDealt();
        this.totalDamageTaken += data.getDamageTaken();
        this.totalPlaytime += data.getPlaytimeSeconds();
        this.totalDrakesKilled += data.getDrakesKilled();
        this.totalDrakeBuffsObtained += data.getDrakeBuffsObtained();

        if (data.getBestKillStreak() > this.bestKillStreak) {
            this.bestKillStreak = data.getBestKillStreak();
        }

        this.recalculateRatios();
        this.lastPlayed = new Timestamp(System.currentTimeMillis());
    }

    public void recalculateRatios() {
        this.kdRatio = totalDeaths == 0 ? totalKills : (double) totalKills / totalDeaths;
        this.kdaRatio = totalDeaths == 0 ? (totalKills + totalAssists) : (double) (totalKills + totalAssists) / totalDeaths;
        this.winRate = totalGames == 0 ? 0.0 : ((double) totalWins / totalGames) * 100;
    }

    public void resetStats() {
        this.totalGames = 0;
        this.totalWins = 0;
        this.totalKills = 0;
        this.totalDeaths = 0;
        this.totalAssists = 0;
        this.winRate = 0.0;
        this.kdRatio = 0.0;
        this.kdaRatio = 0.0;
        this.bestKillStreak = 0;
        this.totalPlaytime = 0;
        this.totalDamageDealt = 0;
        this.totalDamageTaken = 0;
        this.totalDrakesKilled = 0;
        this.totalDrakeBuffsObtained = 0;
        this.favoriteChampion = null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getTotalAssists() {
        return totalAssists;
    }

    public double getWinRate() {
        return winRate;
    }

    public double getKdRatio() {
        return kdRatio;
    }

    public double getKdaRatio() {
        return kdaRatio;
    }

    public int getBestKillStreak() {
        return bestKillStreak;
    }

    public long getTotalPlaytime() {
        return totalPlaytime;
    }

    public int getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public int getTotalDamageTaken() {
        return totalDamageTaken;
    }

    public int getTotalDrakesKilled() {
        return totalDrakesKilled;
    }

    public int getTotalDrakeBuffsObtained() {
        return totalDrakeBuffsObtained;
    }

    public String getFavoriteChampion() {
        return favoriteChampion;
    }

    public Timestamp getFirstJoin() {
        return firstJoin;
    }

    public Timestamp getLastPlayed() {
        return lastPlayed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public void setTotalAssists(int totalAssists) {
        this.totalAssists = totalAssists;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public void setKdRatio(double kdRatio) {
        this.kdRatio = kdRatio;
    }

    public void setKdaRatio(double kdaRatio) {
        this.kdaRatio = kdaRatio;
    }

    public void setBestKillStreak(int bestKillStreak) {
        this.bestKillStreak = bestKillStreak;
    }

    public void setTotalPlaytime(long totalPlaytime) {
        this.totalPlaytime = totalPlaytime;
    }

    public void setTotalDamageDealt(int totalDamageDealt) {
        this.totalDamageDealt = totalDamageDealt;
    }

    public void setTotalDamageTaken(int totalDamageTaken) {
        this.totalDamageTaken = totalDamageTaken;
    }

    public void setTotalDrakesKilled(int totalDrakesKilled) {
        this.totalDrakesKilled = totalDrakesKilled;
    }

    public void setTotalDrakeBuffsObtained(int totalDrakeBuffsObtained) {
        this.totalDrakeBuffsObtained = totalDrakeBuffsObtained;
    }

    public void setFavoriteChampion(String favoriteChampion) {
        this.favoriteChampion = favoriteChampion;
    }

    public void setFirstJoin(Timestamp firstJoin) {
        this.firstJoin = firstJoin;
    }

    public void setLastPlayed(Timestamp lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    private String getFormattedPlaytime() {
        long hours = totalPlaytime / 3600;
        long minutes = (totalPlaytime % 3600) / 60;
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }
        return minutes + "m";
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", totalGames=" + totalGames +
                ", totalWins=" + totalWins +
                ", totalKills=" + totalKills +
                ", totalDeaths=" + totalDeaths +
                ", totalAssists=" + totalAssists +
                ", winRate=" + String.format("%.1f", winRate) + "%" +
                ", kdRatio=" + String.format("%.2f", kdRatio) +
                ", kdaRatio=" + String.format("%.2f", kdaRatio) +
                ", bestKillStreak=" + bestKillStreak +
                ", playtime=" + this.getFormattedPlaytime() +
                '}';
    }
}
