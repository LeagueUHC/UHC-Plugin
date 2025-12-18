package fr.kiza.leagueuhc.core.database.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetMapper<T> {
    T map(final ResultSet result) throws SQLException;
}
