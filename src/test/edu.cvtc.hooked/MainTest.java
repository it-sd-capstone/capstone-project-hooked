package edu.cvtc.hooked;

import edu.cvtc.hooked.dao.CatchDao;
import edu.cvtc.hooked.model.Catch;
import edu.cvtc.hooked.model.SpeciesRestrictions;
import edu.cvtc.hooked.util.DbUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.List;

public class MainTest {

    static {
        System.setProperty("hooked.test.db", "true");
    }

    @BeforeAll
    static void setupDatabase() {
        // Make sure all tables exist so we can insert users and catches.
        System.setProperty("hooked.test.db", "true");
        DbUtil.ensureSchema();
    }

    @Test
    void getConnection_opensAndCloses() throws Exception {
        try (Connection db = DbUtil.getConnection()) {
            assertNotNull(db);
            assertFalse(db.isClosed());
        } // auto-closes here
    }


    @Test
    void queryRaw() throws SQLException {
        try (Connection db = DbUtil.getConnection()) {
            ResultSet rows = DbUtil.queryRaw(db, "SELECT 5 AS result");
            assertNotNull(rows);
            assertTrue(rows.next());
            int result = rows.getInt("result");
            assertEquals(5, result);
            rows.close();
        }
    }

    @Test
    void restrictions() throws SQLException, ClassNotFoundException {
        try (Connection db = DbUtil.getConnection()) {
            String speciesStr = "bluegill";
            double length = 20.00;
            double weight = 10.00;

            SpeciesRestrictions restrictions = SpeciesRestrictions.ALL.get(speciesStr);
            assertNotNull(restrictions);

            boolean invalid = (length > restrictions.getMaxLength()) || (weight > restrictions.getMaxWeight());
            assertTrue(invalid);
        }
    }

    @Test
    void dbinput() throws SQLException, ClassNotFoundException {
        try (Connection c = DbUtil.getConnection();
             Statement stmt = c.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS Catches");
            stmt.executeUpdate("DROP TABLE IF EXISTS Users");
        }
        DbUtil.ensureSchema();
        try (Connection db = DbUtil.getConnection()) {
            String firstName = "Test";
            String lastName = "User";
            String username = "TestUser_" + System.currentTimeMillis();
            String password = "Test";

            String insertUser = """
                INSERT INTO Users (firstName, lastName, userName, email, passwordHash)
                VALUES (?, ?, ?, ?, ?)
            """;

            int userId;
            try (PreparedStatement ps1 = db.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                ps1.setString(1, firstName);
                ps1.setString(2, lastName);
                ps1.setString(3, username);
                ps1.setString(4, username + "@example.com");
                ps1.setString(5, password);

                ps1.executeUpdate();

                ResultSet rs = ps1.getGeneratedKeys();
                rs.next();
                userId = rs.getInt(1);
            }

            String sql = "INSERT INTO Catches (UserID, SpeciesName, LocationName, BaitType, DateCaught, Notes, Length, Weight) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = db.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setString(2, "bluegill");
                ps.setString(3, "mississippi river");
                ps.setString(4, "gulp! minnow");
                ps.setString(5, "2025-9-15");
                ps.setString(6, "slip bobber");
                ps.setDouble(7, 11.0);
                ps.setDouble(8, 1.0);
                ps.executeUpdate();
            }

            try (PreparedStatement verifyUser = db.prepareStatement("SELECT * FROM Users WHERE UserID = ?")) {
                verifyUser.setInt(1, userId);

                ResultSet rs = verifyUser.executeQuery();

                assertEquals(userId, rs.getInt("UserID"));
                assertEquals(firstName, rs.getString("firstName"));
                assertEquals(lastName, rs.getString("lastName"));
                assertEquals(username, rs.getString("userName"));
            }

            try (PreparedStatement verifyCatch = db.prepareStatement("SELECT * FROM Catches WHERE UserID = ?")) {
                verifyCatch.setInt(1, userId);

                ResultSet rs = verifyCatch.executeQuery();

                assertEquals(userId, rs.getInt("UserID"));
                assertEquals("bluegill", rs.getString("SpeciesName"));
                assertEquals("mississippi river", rs.getString("LocationName"));
                assertEquals("gulp! minnow", rs.getString("BaitType"));
                assertEquals("2025-9-15", rs.getString("DateCaught"));
                assertEquals("slip bobber", rs.getString("Notes"));
            }
        }
    }

    @Test
    void search() throws SQLException, ClassNotFoundException {
        try (Connection c = DbUtil.getConnection();
             Statement stmt = c.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS Catches");
            stmt.executeUpdate("DROP TABLE IF EXISTS Users");
        }
        DbUtil.ensureSchema();
        try (Connection db = DbUtil.getConnection()) {
            String speciesStr = "bluegill";
            String locationStr = "mississippi river";
            String baitStr = "gulp! minnow";
            String dateCaught = "2025-9-15";
            String notes = "slip bobber";
            String lengthStr = "11.0";
            String weightStr = "1.0";

            String speciesStr2 = "shovelnose sturgeon";
            String locationStr2 = "chippewa river";
            String baitStr2 = "nightcrawler";
            String dateCaught2 = "2025-8-25";
            String notes2 = "fishing an eddy";
            String lengthStr2 = "27.0";
            String weightStr2 = "5.0";

            String firstName = "Test";
            String lastName = "User";
            String userName = "TestUser";
            String password = "Test";

            String user = "INSERT INTO Users (firstName, lastName, userName, email, passwordHash) VALUES (?, ?, ?, ?, ?)";

            int userId;
            try (PreparedStatement ps1 = db.prepareStatement(user, Statement.RETURN_GENERATED_KEYS)) {
                ps1.setString(1, firstName);
                ps1.setString(2, lastName);
                ps1.setString(3, userName);
                ps1.setString(4, userName + "@example.com");
                ps1.setString(5, password);

                ps1.executeUpdate();

                ResultSet rs = ps1.getGeneratedKeys();
                rs.next();
                userId = rs.getInt(1);
            }

            String sql = "INSERT INTO Catches (UserID, SpeciesName, LocationName, BaitType, DateCaught, Notes, Length, Weight) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = db.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setString(2, speciesStr);
                ps.setString(3, locationStr);
                ps.setString(4, baitStr);
                ps.setString(5, dateCaught);
                ps.setString(6, notes);
                ps.setDouble(7, Double.parseDouble(lengthStr));
                ps.setDouble(8, Double.parseDouble(weightStr));
                ps.executeUpdate();
            }

            try (PreparedStatement ps = db.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setString(2, speciesStr2);
                ps.setString(3, locationStr2);
                ps.setString(4, baitStr2);
                ps.setString(5, dateCaught2);
                ps.setString(6, notes2);
                ps.setDouble(7, Double.parseDouble(lengthStr2));
                ps.setDouble(8, Double.parseDouble(weightStr2));
                ps.executeUpdate();
            }

            try (PreparedStatement verifyUser = db.prepareStatement("SELECT * FROM Users WHERE UserID = ?")) {
                verifyUser.setInt(1, userId);

                ResultSet rs = verifyUser.executeQuery();

                assertTrue(rs.next());
                assertEquals(userId, rs.getInt("UserID"));
                assertEquals(firstName, rs.getString("firstName"));
                assertEquals(lastName, rs.getString("lastName"));
                assertEquals(userName, rs.getString("userName"));
                assertEquals(password, rs.getString("passwordHash"));
            }

            try (PreparedStatement verifyCatch = db.prepareStatement("SELECT * FROM Catches WHERE UserID = ?")) {
                verifyCatch.setInt(1, userId);
                ResultSet rs = verifyCatch.executeQuery();

                assertTrue(rs.next());
                assertEquals(userId, rs.getInt("UserID"));
                assertEquals("bluegill", rs.getString("SpeciesName"));
                assertEquals(locationStr, rs.getString("LocationName"));
                assertEquals(baitStr, rs.getString("BaitType"));
                assertEquals(dateCaught, rs.getString("DateCaught"));
                assertEquals(notes, rs.getString("Notes"));
                assertEquals(Double.parseDouble(lengthStr), rs.getDouble("Length"));
                assertEquals(Double.parseDouble(weightStr), rs.getDouble("Weight"));
            }

            CatchDao dao = new CatchDao();

            List<Catch> results = dao.searchOutput(userId, speciesStr, locationStr, baitStr);

            assertEquals(1, results.size());
        }
    }
}
