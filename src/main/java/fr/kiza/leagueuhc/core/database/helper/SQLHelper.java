package fr.kiza.leagueuhc.core.database.helper;

public enum SQLHelper {

    // =====================================================
    // TABLE CREATION
    // =====================================================

    CREATE_PLAYERS_TABLE(
            "CREATE TABLE IF NOT EXISTS players (" +
                    "uuid VARCHAR(36) PRIMARY KEY, " +
                    "name VARCHAR(16) NOT NULL, " +
                    "total_games INT DEFAULT 0, " +
                    "total_wins INT DEFAULT 0, " +
                    "total_kills INT DEFAULT 0, " +
                    "total_deaths INT DEFAULT 0, " +
                    "total_assists INT DEFAULT 0, " +
                    "win_rate DOUBLE DEFAULT 0.0, " +
                    "kd_ratio DOUBLE DEFAULT 0.0, " +
                    "kda_ratio DOUBLE DEFAULT 0.0, " +
                    "best_kill_streak INT DEFAULT 0, " +
                    "total_playtime BIGINT DEFAULT 0, " +
                    "total_damage_dealt INT DEFAULT 0, " +
                    "total_damage_taken INT DEFAULT 0, " +
                    "total_drakes_killed INT DEFAULT 0, " +
                    "total_drake_buffs_obtained INT DEFAULT 0, " +
                    "favorite_champion VARCHAR(32) DEFAULT NULL, " +
                    "first_join TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "last_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)"
    ),

    CREATE_GAME_HISTORY_TABLE(
            "CREATE TABLE IF NOT EXISTS game_history (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "game_id INT NOT NULL, " +
                    "player_uuid VARCHAR(36) NOT NULL, " +
                    "player_name VARCHAR(16) NOT NULL, " +
                    "champion_played VARCHAR(32) NOT NULL, " +
                    "kills INT DEFAULT 0, " +
                    "deaths INT DEFAULT 0, " +
                    "assists INT DEFAULT 0, " +
                    "best_kill_streak INT DEFAULT 0, " +
                    "damage_dealt INT DEFAULT 0, " +
                    "damage_taken INT DEFAULT 0, " +
                    "healing_done INT DEFAULT 0, " +
                    "gold_earned DOUBLE DEFAULT 0.0, " +
                    "gold_spent DOUBLE DEFAULT 0.0, " +
                    "drakes_killed INT DEFAULT 0, " +
                    "drake_buffs_obtained INT DEFAULT 0, " +
                    "abilities_used INT DEFAULT 0, " +
                    "playtime_seconds BIGINT DEFAULT 0, " +
                    "won BOOLEAN DEFAULT FALSE, " +
                    "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (player_uuid) REFERENCES players(uuid) ON DELETE CASCADE, " +
                    "INDEX idx_game_id (game_id), " +
                    "INDEX idx_player_uuid (player_uuid), " +
                    "INDEX idx_played_at (played_at))"
    ),

    // =====================================================
    // PLAYER QUERIES
    // =====================================================

    PLAYER_CREATE_OR_UPDATE(
            "INSERT INTO players (uuid, name) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE name = VALUES(name), last_played = CURRENT_TIMESTAMP"
    ),

    PLAYER_LOAD("SELECT * FROM players WHERE uuid = ?"),

    PLAYER_UPDATE(
            "UPDATE players SET " +
                    "total_games = ?, " +
                    "total_wins = ?, " +
                    "total_kills = ?, " +
                    "total_deaths = ?, " +
                    "total_assists = ?, " +
                    "win_rate = ?, " +
                    "kd_ratio = ?, " +
                    "kda_ratio = ?, " +
                    "best_kill_streak = ?, " +
                    "total_playtime = ?, " +
                    "total_damage_dealt = ?, " +
                    "total_damage_taken = ?, " +
                    "total_drakes_killed = ?, " +
                    "total_drake_buffs_obtained = ?, " +
                    "favorite_champion = ? " +
                    "WHERE uuid = ?"
    ),

    PLAYER_DELETE("DELETE FROM players WHERE uuid = ?"),

    // =====================================================
    // GAME HISTORY QUERIES
    // =====================================================

    GAME_HISTORY_INSERT(
            "INSERT INTO game_history (" +
                    "game_id, player_uuid, player_name, champion_played, " +
                    "kills, deaths, assists, best_kill_streak, " +
                    "damage_dealt, damage_taken, healing_done, " +
                    "gold_earned, gold_spent, " +
                    "drakes_killed, drake_buffs_obtained, abilities_used, " +
                    "playtime_seconds, won" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
    ),

    GAME_HISTORY_LOAD_PLAYER(
            "SELECT * FROM game_history WHERE player_uuid = ? ORDER BY played_at DESC LIMIT ?"
    ),

    GAME_HISTORY_LOAD_GAME(
            "SELECT * FROM game_history WHERE game_id = ? ORDER BY kills DESC"
    ),

    GAME_HISTORY_DELETE_PLAYER("DELETE FROM game_history WHERE player_uuid = ?");

    private final String request;

    SQLHelper(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }
}