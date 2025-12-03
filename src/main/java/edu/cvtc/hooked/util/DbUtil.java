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
                INSERT INTO Users(firstName, lastName, userName, email, passwordHash)
                SELECT 'Admin', 'User', 'admin', 'hookedAdmin1@gmail.com', 'admin'
                WHERE NOT EXISTS (
                    SELECT 1 FROM Users WHERE userName = 'admin'
                );
            """);

            command.executeUpdate("""
                CREATE TABLE IF NOT EXISTS SpeciesRequests (
                  RequestID   INTEGER PRIMARY KEY AUTOINCREMENT,
                  SpeciesName TEXT NOT NULL,
                  UserID      INTEGER,
                  RequestedAt TEXT DEFAULT (datetime('now')),
                  FOREIGN KEY (UserID) REFERENCES Users(UserID)
                )
            """);

            command.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Species (
                    SpeciesID       INTEGER PRIMARY KEY AUTOINCREMENT,
                    SpeciesName     TEXT NOT NULL UNIQUE,
                    MaxLength       DECIMAL(5,2) NOT NULL,
                    MaxWeight       DECIMAL(5,2) NOT NULL,
                    CreatedByUserID INTEGER,
                    FOREIGN KEY (CreatedByUserID) REFERENCES Users(UserID)
                )
            """);

            seedSpeciesIfEmpty(c);

        } catch (Exception e) {
            throw new RuntimeException("Failed to ensure schema", e);
        }
    }

    private static void seedSpeciesIfEmpty(Connection conn) throws SQLException {
        // Check if Species already has data
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Species")) {
            if (rs.next() && rs.getInt(1) > 0) {
                // Already seeded – do nothing
                return;
            }
        }

        String sql = "INSERT INTO Species (SpeciesName, MaxLength, MaxWeight) VALUES (?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            insertSpecies(ps, "bighead carp",        50.0, 68.0);
            insertSpecies(ps, "bigmouth buffalo",    50.0, 77.0);
            insertSpecies(ps, "black bullhead",      22.0, 6.0);
            insertSpecies(ps, "black crappie",       20.0, 5.0);
            insertSpecies(ps, "bluegill",            12.0, 3.0);
            insertSpecies(ps, "bowfin",              32.0, 14.0);
            insertSpecies(ps, "brooke trout",        25.0, 11.0);
            insertSpecies(ps, "brown bullhead",      18.0, 5.0);
            insertSpecies(ps, "brown trout",         41.0, 42.0);
            insertSpecies(ps, "burbot",              38.0, 19.0);
            insertSpecies(ps, "channel catfish",     50.0, 44.0);
            insertSpecies(ps, "common carp",         50.0, 58.0);
            insertSpecies(ps, "flathead catfish",    53.0, 75.0);
            insertSpecies(ps, "freshwater drum",     38.0, 36.0);
            insertSpecies(ps, "golden redhorse",     24.0, 6.0);
            insertSpecies(ps, "greater redhorse",    29.0, 11.0);
            insertSpecies(ps, "lake sturgeon",       80.0, 171.0);
            insertSpecies(ps, "lake trout",          45.0, 50.0);
            insertSpecies(ps, "largemouth bass",     24.0, 12.0);
            insertSpecies(ps, "longnose gar",        53.0, 22.0);
            insertSpecies(ps, "muskellunge",         64.0, 70.0);
            insertSpecies(ps, "northern pike",       46.0, 38.0);
            insertSpecies(ps, "pumpkinseed",         12.0, 2.0);
            insertSpecies(ps, "rock bass",           14.0, 3.0);
            insertSpecies(ps, "sauger",              24.0, 7.0);
            insertSpecies(ps, "shorthead redhorse",  24.0, 5.0);
            insertSpecies(ps, "shortnose gar",       32.0, 5.0);
            insertSpecies(ps, "shovelnose sturgeon", 40.0, 8.0);
            insertSpecies(ps, "silver redhorse",     30.0, 12.0);
            insertSpecies(ps, "walleye",             30.0, 19.0);
            insertSpecies(ps, "longnose sucker",     22.0, 5.0);
            insertSpecies(ps, "northern hog sucker", 17.0, 2.0);
            insertSpecies(ps, "sucker spotted",      22.0, 5.0);

            ps.executeBatch();
        }
    }

    private static void insertSpecies(PreparedStatement ps,
                                      String name,
                                      double maxLength,
                                      double maxWeight) throws SQLException {
        ps.setString(1, name);
        ps.setDouble(2, maxLength);
        ps.setDouble(3, maxWeight);
        ps.addBatch();
    }



    public static ResultSet queryRaw(Connection db, String sql) throws SQLException {
        Statement st = db.createStatement();
        st.setQueryTimeout(TIMEOUT_STATEMENT_S);
        return st.executeQuery(sql);
    }

    private DbUtil() {}

}

