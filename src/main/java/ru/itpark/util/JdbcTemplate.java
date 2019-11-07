package ru.itpark.util;

import javax.sql.DataSource;
import java.sql.SQLException;

public class JdbcTemplate {
    private JdbcTemplate() {
    }

    public static void execute(DataSource ds, String sql) throws SQLException {
        try (
                var conn = ds.getConnection();
//                var stmt = conn.createStatement();
                var stmt = conn.prepareStatement(sql);
        ) {
            stmt.execute();
        }
    }
}
