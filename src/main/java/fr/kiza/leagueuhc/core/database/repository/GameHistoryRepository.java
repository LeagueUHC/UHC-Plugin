package fr.kiza.leagueuhc.core.database.repository;

import fr.kiza.leagueuhc.LeagueUHC;
import fr.kiza.leagueuhc.core.database.data.GameHistoryData;
import fr.kiza.leagueuhc.core.database.query.QueryExecutor;
import fr.kiza.leagueuhc.core.database.helper.SQLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GameHistoryRepository {

    protected final LeagueUHC instance;

    public GameHistoryRepository(LeagueUHC instance) {
        this.instance = instance;
    }

    /**
     * Sauvegarde un historique de partie
     */
    public CompletableFuture<Void> save(final GameHistoryData history) {
        return QueryExecutor.updateAsync(SQLHelper.GAME_HISTORY_INSERT.getRequest(), ps -> {
            ps.setInt(1, history.getGameId());
            ps.setString(2, history.getPlayerUuid().toString());
            ps.setString(3, history.getPlayerName());
            ps.setString(4, history.getChampionPlayed());
            ps.setInt(5, history.getKills());
            ps.setInt(6, history.getDeaths());
            ps.setInt(7, history.getAssists());
            ps.setInt(8, history.getBestKillStreak());
            ps.setInt(9, history.getDamageDealt());
            ps.setInt(10, history.getDamageTaken());
            ps.setInt(11, history.getHealingDone());
            ps.setDouble(12, history.getGoldEarned());
            ps.setDouble(13, history.getGoldSpent());
            ps.setInt(14, history.getDrakesKilled());
            ps.setInt(15, history.getDrakeBuffsObtained());
            ps.setInt(16, history.getAbilitiesUsed());
            ps.setLong(17, history.getPlaytimeSeconds());
            ps.setBoolean(18, history.isWon());
        });
    }

    /**
     * Charge les X dernières parties d'un joueur
     */
    public CompletableFuture<List<GameHistoryData>> loadPlayerHistory(final UUID uuid, final int limit) {
        return QueryExecutor.queryAsync(SQLHelper.GAME_HISTORY_LOAD_PLAYER.getRequest(), ps -> {
            ps.setString(1, uuid.toString());
            ps.setInt(2, limit);
        }, rs -> {
            List<GameHistoryData> history = new ArrayList<>();
            while (rs.next()) {
                history.add(new GameHistoryData(
                        rs.getInt("id"),
                        rs.getInt("game_id"),
                        UUID.fromString(rs.getString("player_uuid")),
                        rs.getString("player_name"),
                        rs.getString("champion_played"),
                        rs.getInt("kills"),
                        rs.getInt("deaths"),
                        rs.getInt("assists"),
                        rs.getInt("best_kill_streak"),
                        rs.getInt("damage_dealt"),
                        rs.getInt("damage_taken"),
                        rs.getInt("healing_done"),
                        rs.getDouble("gold_earned"),
                        rs.getDouble("gold_spent"),
                        rs.getInt("drakes_killed"),
                        rs.getInt("drake_buffs_obtained"),
                        rs.getInt("abilities_used"),
                        rs.getLong("playtime_seconds"),
                        rs.getBoolean("won"),
                        rs.getTimestamp("played_at")
                ));
            }
            return history;
        });
    }

    /**
     * Charge tous les résultats d'une partie spécifique
     */
    public CompletableFuture<List<GameHistoryData>> loadGameResults(final int gameId) {
        return QueryExecutor.queryAsync(SQLHelper.GAME_HISTORY_LOAD_GAME.getRequest(), ps -> {
            ps.setInt(1, gameId);
        }, rs -> {
            List<GameHistoryData> history = new ArrayList<>();
            while (rs.next()) {
                history.add(new GameHistoryData(
                        rs.getInt("id"),
                        rs.getInt("game_id"),
                        UUID.fromString(rs.getString("player_uuid")),
                        rs.getString("player_name"),
                        rs.getString("champion_played"),
                        rs.getInt("kills"),
                        rs.getInt("deaths"),
                        rs.getInt("assists"),
                        rs.getInt("best_kill_streak"),
                        rs.getInt("damage_dealt"),
                        rs.getInt("damage_taken"),
                        rs.getInt("healing_done"),
                        rs.getDouble("gold_earned"),
                        rs.getDouble("gold_spent"),
                        rs.getInt("drakes_killed"),
                        rs.getInt("drake_buffs_obtained"),
                        rs.getInt("abilities_used"),
                        rs.getLong("playtime_seconds"),
                        rs.getBoolean("won"),
                        rs.getTimestamp("played_at")
                ));
            }
            return history;
        });
    }

    /**
     * Supprime l'historique d'un joueur
     */
    public CompletableFuture<Void> deletePlayerHistory(final UUID uuid) {
        return QueryExecutor.updateAsync(SQLHelper.GAME_HISTORY_DELETE_PLAYER.getRequest(), ps -> {
            ps.setString(1, uuid.toString());
        });
    }
}