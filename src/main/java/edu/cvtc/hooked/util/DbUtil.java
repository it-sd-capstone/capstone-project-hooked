package edu.cvtc.hooked.util;

import java.nio.file.*;
import java.sql.*;
import java.net.URL;

public final class DbUtil {

    private static final int TIMEOUT_STATEMENT_S = 5;

    public static String databasePath() {
        try {
            // Use the user's home directory, which is writable on both
            // local dev and AWS Elastic Beanstalk.
            String userHome = System.getProperty("user.home");

            // Check if we are in test mode
            if ("true".equals(System.getProperty("hooked.test.db"))) {
                Path testDir = Paths.get(userHome, "hooked-data");
                Files.createDirectories(testDir);

                String testPath = testDir.resolve("hooked-test.db").toString();
                System.out.println(">>> TEST DB PATH = " + testPath);
                return testPath;
            }

            // Normal DB
            Path dataDir = Paths.get(userHome, "hooked-data");
            Files.createDirectories(dataDir);

            String path = dataDir.resolve("hooked.db").toString();
            System.out.println(">>> DB PATH = " + path);
            return path;

        } catch (Exception e) {
            throw new RuntimeException("Failed to build DB path", e);
        }
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
                  email    TEXT NOT NULL UNIQUE,
                  resetHash TEXT,
                  resetTime TIMESTAMP,
                  passwordHash TEXT NOT NULL
                )
            """);

            command.executeUpdate("""
            CREATE TABLE IF NOT EXISTS Species (
              SpeciesID    INTEGER PRIMARY KEY AUTOINCREMENT,
              SpeciesName  VARCHAR(50) NOT NULL UNIQUE
            );
            """);

            command.executeUpdate("""
            CREATE TABLE IF NOT EXISTS Location (
              LocationID   INTEGER PRIMARY KEY AUTOINCREMENT,
              LocationName VARCHAR(100) NOT NULL,
              State        VARCHAR(2) NOT NULL
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

            command.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Bait (
                    BaitID   INTEGER PRIMARY KEY AUTOINCREMENT,
                    Name     TEXT NOT NULL,
                    Notes    TEXT
                );
            """);

            command.executeUpdate("""
                INSERT INTO Users(firstName, lastName, userName, passwordHash)
                SELECT 'Admin', 'User', 'admin', 'hookedAdmin1@gmail.com', 'admin'
                WHERE NOT EXISTS (
                    SELECT 1 FROM Users WHERE userName = 'admin'
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

