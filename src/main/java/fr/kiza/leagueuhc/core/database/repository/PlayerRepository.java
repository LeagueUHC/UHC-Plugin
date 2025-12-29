package fr.kiza.leagueuhc.core.database.repository;

import fr.kiza.leagueuhc.LeagueUHC;
import fr.kiza.leagueuhc.core.database.query.QueryExecutor;
import fr.kiza.leagueuhc.core.database.data.PlayerData;
import fr.kiza.leagueuhc.core.database.helper.SQLHelper;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerRepository {

    protected final LeagueUHC instance;

    public PlayerRepository(LeagueUHC instance) {
        this.instance = instance;
    }

    public CompletableFuture<Void> createOrUpdate(final UUID uuid) {
        final String playerName = Bukkit.getPlayer(uuid).getName();

        return QueryExecutor.updateAsync(SQLHelper.PLAYER_CREATE_OR_UPDATE.getRequest(), ps -> {
            ps.setString(1, uuid.toString());
            ps.setString(2, playerName);
        }).thenRun(() ->
                this.instance.getLogger().info(playerName + " (" + uuid + ") has been created or updated.")
        );
    }

    public CompletableFuture<PlayerData> load(final UUID uuid) {
        return QueryExecutor.queryAsync(SQLHelper.PLAYER_LOAD.getRequest(), ps -> {
            ps.setString(1, uuid.toString());
        }, rs -> {
            if (!rs.next()) return null;

            return new PlayerData(
                    uuid,
                    rs.getString("name"),
                    rs.getInt("total_games"),
                    rs.getInt("total_wins"),
                    rs.getInt("total_kills"),
                    rs.getInt("total_deaths"),
                    rs.getInt("total_assists"),
                    rs.getDouble("win_rate"),
                    rs.getDouble("kd_ratio"),
                    rs.getDouble("kda_ratio"),
                    rs.getInt("best_kill_streak"),
                    rs.getLong("total_playtime"),
                    rs.getInt("total_damage_dealt"),
                    rs.getInt("total_damage_taken"),
                    rs.getInt("total_drakes_killed"),
                    rs.getInt("total_drake_buffs_obtained"),
                    rs.getString("favorite_champion"),
                    rs.getTimestamp("first_join"),
                    rs.getTimestamp("last_played")
            );
        });
    }

    public CompletableFuture<Void> save(final PlayerData data) {
        return QueryExecutor.updateAsync(SQLHelper.PLAYER_UPDATE.getRequest(), ps -> {
            ps.setInt(1, data.getTotalGames());
            ps.setInt(2, data.getTotalWins());
            ps.setInt(3, data.getTotalKills());
            ps.setInt(4, data.getTotalDeaths());
            ps.setInt(5, data.getTotalAssists());
            ps.setDouble(6, data.getWinRate());
            ps.setDouble(7, data.getKdRatio());
            ps.setDouble(8, data.getKdaRatio());
            ps.setInt(9, data.getBestKillStreak());
            ps.setLong(10, data.getTotalPlaytime());
            ps.setInt(11, data.getTotalDamageDealt());
            ps.setInt(12, data.getTotalDamageTaken());
            ps.setInt(13, data.getTotalDrakesKilled());
            ps.setInt(14, data.getTotalDrakeBuffsObtained());
            ps.setString(15, data.getFavoriteChampion());
            ps.setString(16, data.getUuid().toString());
        });
    }

    public CompletableFuture<Void> delete(final UUID uuid) {
        return QueryExecutor.updateAsync(SQLHelper.PLAYER_DELETE.getRequest(), ps -> {
            ps.setString(1, uuid.toString());
        });
    }

    public void saveSync(final PlayerData data) {
        QueryExecutor.updateSync(SQLHelper.PLAYER_UPDATE.getRequest(), ps -> {
            ps.setInt(1, data.getTotalGames());
            ps.setInt(2, data.getTotalWins());
            ps.setInt(3, data.getTotalKills());
            ps.setInt(4, data.getTotalDeaths());
            ps.setInt(5, data.getTotalAssists());
            ps.setDouble(6, data.getWinRate());
            ps.setDouble(7, data.getKdRatio());
            ps.setDouble(8, data.getKdaRatio());
            ps.setInt(9, data.getBestKillStreak());
            ps.setLong(10, data.getTotalPlaytime());
            ps.setInt(11, data.getTotalDamageDealt());
            ps.setInt(12, data.getTotalDamageTaken());
            ps.setInt(13, data.getTotalDrakesKilled());
            ps.setInt(14, data.getTotalDrakeBuffsObtained());
            ps.setString(15, data.getFavoriteChampion());
            ps.setString(16, data.getUuid().toString());
        });
    }
}