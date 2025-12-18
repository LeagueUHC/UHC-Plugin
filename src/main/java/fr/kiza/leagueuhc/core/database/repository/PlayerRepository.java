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
                    rs.getDouble("win_rate"),
                    rs.getDouble("kd_ratio"),
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
            ps.setDouble(5, data.getWinRate());
            ps.setDouble(6, data.getKdRatio());
            ps.setString(7, data.getUuid().toString());
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
            ps.setDouble(5, data.getWinRate());
            ps.setDouble(6, data.getKdRatio());
            ps.setString(7, data.getUuid().toString());
        });
    }
}