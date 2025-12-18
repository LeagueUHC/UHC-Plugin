package fr.kiza.leagueuhc.core.database.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData {

    private final UUID uuid;
    private String name;

    private int totalGames;
    private int totalWins;
    private int totalKills;
    private int totalDeaths;

    private double winRate;
    private double kdRatio;

    private Timestamp firstJoin;
    private Timestamp lastPlayed;

    public static final Map<UUID, PlayerData> PLAYER = new ConcurrentHashMap<>();

    public PlayerData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.totalGames = 0;
        this.totalWins = 0;
        this.totalKills = 0;
        this.totalDeaths = 0;
        this.winRate = 0.0;
        this.kdRatio = 0.0;
        this.firstJoin = new Timestamp(System.currentTimeMillis());
        this.lastPlayed = new Timestamp(System.currentTimeMillis());
    }

    public PlayerData(UUID uuid, String name, int totalGames, int totalWins, int totalKills, int totalDeaths, double winRate, double kdRatio, Timestamp firstJoin, Timestamp lastPlayed) {
        this.uuid = uuid;
        this.name = name;
        this.totalGames = totalGames;
        this.totalWins = totalWins;
        this.totalKills = totalKills;
        this.totalDeaths = totalDeaths;
        this.winRate = winRate;
        this.kdRatio = kdRatio;
        this.firstJoin = firstJoin;
        this.lastPlayed = lastPlayed;
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

    public double getWinRate() {
        return winRate;
    }

    public double getKdRatio() {
        return kdRatio;
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

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public void setKdRatio(double kdRatio) {
        this.kdRatio = kdRatio;
    }

    public void setFirstJoin(Timestamp firstJoin) {
        this.firstJoin = firstJoin;
    }

    public void setLastPlayed(Timestamp lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public void addGame(boolean won, int kills, int deaths) {
        this.totalGames++;
        if (won) this.totalWins++;
        this.totalKills += kills;
        this.totalDeaths += deaths;

        this.kdRatio = totalDeaths == 0 ? totalKills : (double) totalKills / totalDeaths;
        this.winRate = totalGames == 0 ? 0.0 : ((double) totalWins / totalGames) * 100;
        this.lastPlayed = new Timestamp(System.currentTimeMillis());
    }

    public void resetStats() {
        this.totalGames = 0;
        this.totalWins = 0;
        this.totalKills = 0;
        this.totalDeaths = 0;
        this.winRate = 0.0;
        this.kdRatio = 0.0;
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
                ", winRate=" + winRate +
                ", kdRatio=" + kdRatio +
                ", firstJoin=" + firstJoin +
                ", lastPlayed=" + lastPlayed +
                '}';
    }
}
