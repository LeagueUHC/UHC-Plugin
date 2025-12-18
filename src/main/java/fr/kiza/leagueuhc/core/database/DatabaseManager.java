package fr.kiza.leagueuhc.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.kiza.leagueuhc.LeagueUHC;
import fr.kiza.leagueuhc.core.database.query.QueryExecutor;
import fr.kiza.leagueuhc.core.database.repository.PlayerRepository;
import fr.kiza.leagueuhc.core.database.service.PlayerService;

import javax.sql.DataSource;

public class DatabaseManager {

    private final LeagueUHC instance;

    private static HikariDataSource dataSource;

    private final PlayerRepository playerRepository;
    private final PlayerService playerService;

    public DatabaseManager(final LeagueUHC instance) {
        this.instance = instance;

        this.playerRepository = new PlayerRepository(instance);
        this.playerService = new PlayerService(instance, this.playerRepository);
    }

    public static void init(final DatabaseConfig config) {
        final HikariConfig hikari = new HikariConfig();
        hikari.setJdbcUrl(config.getJdbcUrl());
        hikari.setUsername(config.getUsername());
        hikari.setPassword(config.getPassword());

        hikari.setMaximumPoolSize(10);
        hikari.setMinimumIdle(2);
        hikari.setConnectionTimeout(3000);
        hikari.setIdleTimeout(600000);
        hikari.setMaxLifetime(1800000);
        hikari.setPoolName("LeagueUHC-Pool");

        hikari.addDataSourceProperty("cachePrepStmts", "true");
        hikari.addDataSourceProperty("prepStmtCacheSize", "250");
        hikari.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikari.addDataSourceProperty("useServerPrepStmts", "true");

        dataSource = new HikariDataSource(hikari);
    }

    public static void shutdown() {
        QueryExecutor.shutdown();
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public static DataSource getDataSource() {
        if (dataSource == null || dataSource.isClosed()) {
            throw new IllegalStateException("Database not initialized or already closed");
        }
        return dataSource;
    }

    public PlayerRepository getPlayerRepository() {
        return this.playerRepository;
    }

    public PlayerService getPlayerService() {
        return this.playerService;
    }
}