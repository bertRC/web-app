package ru.itpark.util;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private JdbcTemplate() {
    }

    public static void execute(DataSource ds, String sql) throws SQLException {
        try (
                var conn = ds.getConnection();
                var stmt = conn.prepareStatement(sql);
        ) {
            stmt.execute();
        }
    }

    public static <T> List<T> executeQuery(DataSource ds, String sql, PreparedStatementSetter setter, RowMapper<T> mapper) throws SQLException {
        try (
                var conn = ds.getConnection();
                var stmt = conn.prepareStatement(sql);
        ) {
            setter.setValues(stmt);
            try (var rs = stmt.executeQuery();) {
                List<T> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapper.map(rs));
                }
                return result;
            }
        }
    }

    public static <T> List<T> executeQuery(DataSource ds, String sql, RowMapper<T> mapper) throws SQLException {
        return executeQuery(ds, sql, stmt -> {}, mapper);
    }

    public static void executeUpdate(DataSource ds, String sql, PreparedStatementSetter setter) throws SQLException {
        try (
                var conn = ds.getConnection();
                var stmt = conn.prepareStatement(sql);
        ) {
            setter.setValues(stmt);
            stmt.executeUpdate();
        }
    }
}
