package edu.ucdenver.rosita.utils;

import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GridStatementCreator implements PreparedStatementCreator {

    private final String sql;
    private final int threshold;
    private final Object[] params;

    public GridStatementCreator(String sql, Object[] params, int threshold) {

        this.sql = sql;
        this.threshold = threshold;
        this.params = params;

    }

    @Override
    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

        final PreparedStatement stmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(threshold);
        connection.setAutoCommit(false);
        for (int i = 0; params != null && i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        return stmt;

    }

}
