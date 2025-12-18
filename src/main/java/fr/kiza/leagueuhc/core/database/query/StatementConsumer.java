package fr.kiza.leagueuhc.core.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementConsumer {
    void accept(final PreparedStatement statement) throws SQLException;
}
