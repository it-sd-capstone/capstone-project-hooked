package edu.cvtc.hooked.util;

import java.nio.file.*;
import java.sql.*;
import java.net.URL;

public final class DbUtil {

    private static final int TIMEOUT_STATEMENT_S = 5;

    public static String databasePath() {
        URL classUrl = DbUtil.class.getProtectionDomain().getCodeSource().getLocation();
        Path classesPath = Paths.get(classUrl.getPath().substring(1)); // Remove leading /

        Path projectRoot = classesPath.getParent()
                .getParent()
                .getParent()
                .getParent();

        Path dataDir = projectRoot.resolve("data");
        try { Files.createDirectories(dataDir); } catch (Exception ignored) {}
        return dataDir.resolve("hooked.db").toString();
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load SQLite JDBC driver", e);
        }

        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + databasePath());
        try (Statement s = conn.createStatement()) {
            s.setQueryTimeout(TIMEOUT_STATEMENT_S);
            s.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    public static void ensureSchema() {
        try (Connection c = getConnection();
             Statement command = c.createStatement()) {

            command.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Users (
                  UserID       INTEGER PRIMARY KEY AUTOINCREMENT,
                  firstName    TEXT NOT NULL,
                  lastName     TEXT NOT NULL,
                  userName     TEXT NOT NULL UNIQUE,
                  passwordHash TEXT NOT NULL
                )
            """);

            command.executeUpdate("""
            CREATE TABLE IF NOT EXISTS Species (
              SpeciesID    INTEGER PRIMARY KEY AUTOINCREMENT,
              SpeciesName  VARCHAR(50) NOT NULL,
              Length       REAL,
              Weight       REAL
            );
            """);

            command.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Catches (
                    CatchID      INTEGER PRIMARY KEY AUTOINCREMENT,
                    UserID       INTEGER NOT NULL,
                    SpeciesName  VARCHAR(50) NOT NULL,
                    LocationName TEXT NOT NULL,
                    BaitType     TEXT NOT NULL,
                    Length       DECIMAL(5,2),
                    Weight       DECIMAL(5,2),
                    DateCaught   DATE,
                    Notes        TEXT,
                    FOREIGN KEY (UserID) REFERENCES Users(UserID)
                );
            """);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet queryRaw(Connection db, String sql) throws SQLException {
        Statement st = db.createStatement();
        st.setQueryTimeout(TIMEOUT_STATEMENT_S);
        return st.executeQuery(sql);
    }

    private DbUtil() {}
}