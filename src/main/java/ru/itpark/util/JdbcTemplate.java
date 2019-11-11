package ru.itpark.util;

import ru.itpark.exception.DataStoreException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private JdbcTemplate() {
    }

    private static <T> T executeInternal(DataSource ds, String sql, PreparedStatementExecutor<T> executor) {
        try (
                var conn = ds.getConnection();
                var stmt = conn.prepareStatement(sql);
        ) {
            return executor.execute(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataStoreException(e);
        }
    }

    public static <T> List<T> executeQuery(DataSource ds, String sql, PreparedStatementSetter setter, RowMapper<T> mapper) {
        return executeInternal(ds, sql, stmt -> {
            try (var rs = setter.setValues(stmt).executeQuery();) {
                List<T> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapper.map(rs));
                }
                return result;
            }
        });
    }

    public static <T> List<T> executeQuery(DataSource ds, String sql, RowMapper<T> mapper) {
        return executeQuery(ds, sql, stmt -> stmt, mapper);
    }

    public static int executeUpdate(DataSource ds, String sql, PreparedStatementSetter setter) {
        return executeInternal(ds, sql, stmt ->
            setter.setValues(stmt).executeUpdate());
    }

    public static int executeUpdate(DataSource ds, String sql) {
        return executeUpdate(ds, sql, stmt -> stmt);
    }
}
