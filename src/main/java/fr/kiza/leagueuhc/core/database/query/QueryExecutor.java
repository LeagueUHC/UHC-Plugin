package fr.kiza.leagueuhc.core.database.query;

import fr.kiza.leagueuhc.LeagueUHC;
import fr.kiza.leagueuhc.core.database.DatabaseManager;
import fr.kiza.leagueuhc.core.database.exception.DatabaseException;
import fr.kiza.leagueuhc.core.database.mapper.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class QueryExecutor {

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(4, runnable -> {
       final Thread thread = new Thread(runnable, "LeagueUHC-Database");
       thread.setDaemon(true);
       return thread;
    });

    private QueryExecutor() { }

    public static CompletableFuture<Void> updateAsync(final String sql, final StatementConsumer consumer) {
        return CompletableFuture.runAsync(() -> updateAsync(sql, consumer), EXECUTOR);
    }

    public static <T> CompletableFuture<T> queryAsync(final String sql, final StatementConsumer consumer, final ResultSetMapper<T> mapper) {
        return CompletableFuture.supplyAsync(() -> querySync(sql, consumer, mapper), EXECUTOR);
    }

    public static void updateSync(final String sql, final StatementConsumer consumer) {
        try (final Connection connection = DatabaseManager.getDataSource().getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            consumer.accept(statement);
            statement.executeUpdate();
        } catch (final SQLException e) {
            LeagueUHC.getInstance().getLogger().severe("Database update error: " + e.getMessage());
            throw new DatabaseException("Failed to execute update", e);
        }
    }

    public static <T> T querySync(final String sql, final StatementConsumer consumer, final ResultSetMapper<T> mapper) {
        try (final Connection connection = DatabaseManager.getDataSource().getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            consumer.accept(statement);
            try (final ResultSet rs = statement.executeQuery()) {
                return mapper.map(rs);
            }
        } catch (final SQLException e) {
            LeagueUHC.getInstance().getLogger().severe("Database query error: " + e.getMessage());
            throw new DatabaseException("Failed to execute query", e);
        }
    }

    public static void shutdown() {
        EXECUTOR.shutdown();
    }
}
