package fr.kiza.leagueuhc.core.database.data;

import java.sql.Timestamp;
import java.util.UUID;

public class GameHistoryData {

    private int id;
    private final int gameId;
    private final UUID playerUuid;
    private final String playerName;

    private final String championPlayed;

    private final int kills;
    private final int deaths;
    private final int assists;
    private final int bestKillStreak;
    private final int damageDealt;
    private final int damageTaken;
    private final int healingDone;

    private final double goldEarned;
    private final double goldSpent;

    private final int drakesKilled;
    private final int drakeBuffsObtained;
    private final int abilitiesUsed;

    private final long playtimeSeconds;
    private final boolean won;
    private Timestamp playedAt;

    public GameHistoryData(int gameId, UUID playerUuid, String playerName, String championPlayed, int kills, int deaths, int assists, int bestKillStreak, int damageDealt, int damageTaken, int healingDone, double goldEarned, double goldSpent, int drakesKilled, int drakeBuffsObtained, int abilitiesUsed, long playtimeSeconds, boolean won) {
        this.gameId = gameId;
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.championPlayed = championPlayed;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.bestKillStreak = bestKillStreak;
        this.damageDealt = damageDealt;
        this.damageTaken = damageTaken;
        this.healingDone = healingDone;
        this.goldEarned = goldEarned;
        this.goldSpent = goldSpent;
        this.drakesKilled = drakesKilled;
        this.drakeBuffsObtained = drakeBuffsObtained;
        this.abilitiesUsed = abilitiesUsed;
        this.playtimeSeconds = playtimeSeconds;
        this.won = won;
        this.playedAt = new Timestamp(System.currentTimeMillis());
    }

    public GameHistoryData(int id, int gameId, UUID playerUuid, String playerName, String championPlayed, int kills, int deaths, int assists, int bestKillStreak, int damageDealt, int damageTaken, int healingDone, double goldEarned, double goldSpent, int drakesKilled, int drakeBuffsObtained, int abilitiesUsed, long playtimeSeconds, boolean won, Timestamp playedAt) {
        this.id = id;
        this.gameId = gameId;
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.championPlayed = championPlayed;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.bestKillStreak = bestKillStreak;
        this.damageDealt = damageDealt;
        this.damageTaken = damageTaken;
        this.healingDone = healingDone;
        this.goldEarned = goldEarned;
        this.goldSpent = goldSpent;
        this.drakesKilled = drakesKilled;
        this.drakeBuffsObtained = drakeBuffsObtained;
        this.abilitiesUsed = abilitiesUsed;
        this.playtimeSeconds = playtimeSeconds;
        this.won = won;
        this.playedAt = playedAt;
    }

    public double getKDA() {
        return deaths == 0 ? (kills + assists) : (double) ((kills + assists) / deaths);
    }

    public String getFormattedPlaytime() {
        long minutes = playtimeSeconds / 60;
        long seconds = playtimeSeconds % 60;
        return String.format("%dm %ds", minutes, seconds);
    }

    public double getNetGold() {
        return goldEarned - goldSpent;
    }

    public String getSummary() {
        return String.format("%s | %s | %d/%d/%d | %.0f gold | %s", won ? "§aVictoire" : "§cDéfaite", championPlayed, kills, deaths, assists, goldEarned, getFormattedPlaytime());
    }

    public int getId() {
        return id;
    }

    public int getGameId() {
        return gameId;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getChampionPlayed() {
        return championPlayed;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getAssists() {
        return assists;
    }

    public int getBestKillStreak() {
        return bestKillStreak;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public int getHealingDone() {
        return healingDone;
    }

    public double getGoldEarned() {
        return goldEarned;
    }

    public double getGoldSpent() {
        return goldSpent;
    }

    public int getDrakesKilled() {
        return drakesKilled;
    }

    public int getDrakeBuffsObtained() {
        return drakeBuffsObtained;
    }

    public int getAbilitiesUsed() {
        return abilitiesUsed;
    }

    public long getPlaytimeSeconds() {
        return playtimeSeconds;
    }

    public boolean isWon() {
        return won;
    }

    public Timestamp getPlayedAt() {
        return playedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayedAt(Timestamp playedAt) {
        this.playedAt = playedAt;
    }

    @Override
    public String toString() {
        return "GameHistory{" +
                "gameId=" + gameId +
                ", player='" + playerName + '\'' +
                ", champion='" + championPlayed + '\'' +
                ", kda=" + kills + "/" + deaths + "/" + assists +
                ", gold=" + goldEarned + "/" + goldSpent +
                ", won=" + won +
                ", playedAt=" + playedAt +
                '}';
    }
}
