package fr.kiza.leagueuhc.core.database.helper;

public enum SQLHelper {

    // -------------------------------
    // PLAYER SIDE
    // -------------------------------

    PLAYER_CREATE_OR_UPDATE("INSERT INTO players (uuid,name,first_join,last_played) " +
            "VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) " +
            "ON DUPLICATE KEY UPDATE " +
            "name = VALUES(name), " +
            "last_played = CURRENT_TIMESTAMP"),
    PLAYER_LOAD("SELECT * FROM players WHERE uuid = ?"),
    PLAYER_UPDATE("UPDATE players SET total_games=?, total_wins=?, total_kills=?, total_deaths=?, win_rate=?, kd_ratio=?, last_played=CURRENT_TIMESTAMP WHERE uuid=?"),
    PLAYER_DELETE("DELETE FROM players WHERE uuid=?");

    private final String request;

    SQLHelper(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }
}
