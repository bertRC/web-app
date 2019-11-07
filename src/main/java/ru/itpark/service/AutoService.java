package ru.itpark.service;

import ru.itpark.domain.Auto;
import ru.itpark.util.JdbcTemplate;
import ru.itpark.util.ResourcesPaths;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AutoService {
    private final DataSource ds;

    public AutoService() throws NamingException, SQLException {
        var context = new InitialContext();
        ds = (DataSource) context.lookup(ResourcesPaths.dbPath);
        JdbcTemplate.execute(ds, "CREATE TABLE IF NOT EXISTS autos (id TEXT PRIMARY KEY, name TEXT NOT NULL, description TEXT NOT NULL, image TEXT);");
    }

    public List<Auto> getAll() throws SQLException {
        return JdbcTemplate.executeQuery(
                ds,
                "SELECT id, name, description, image FROM autos;",
                rs -> new Auto(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image")
                )
        );
    }

    public void create(String name, String description, String image) throws SQLException {
        JdbcTemplate.executeUpdate(
                ds,
                "INSERT INTO autos (id, name, description, image) VALUES (?, ?, ?, ?)",
                stmt -> {
                    stmt.setString(1, UUID.randomUUID().toString());
                    stmt.setString(2, name);
                    stmt.setString(3, description);
                    stmt.setString(4, image);
                }
        );
    }
}
