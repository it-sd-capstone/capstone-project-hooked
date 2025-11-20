package edu.cvtc.hooked;

import edu.cvtc.hooked.dao.CatchDao;
import edu.cvtc.hooked.model.Catch;
import edu.cvtc.hooked.model.SpeciesRestrictions;
import edu.cvtc.hooked.util.DbUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.List;

public class MainTest {
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
            String speciesStr = "bluegill";
            String locationStr = "mississippi river";
            String baitStr = "gulp! minnow";
            String dateCaught = "2025-9-15";
            String notes = "slip bobber";
            String lengthStr = "11.0";
            String weightStr = "1.0";

            Integer userID = 1;
            String firstName = "Test";
            String lastName = "User";
            String username = "TestUser";
            String password = "Test";

            String user = "INSERT INTO Users (firstName, lastName, userName, passwordHash) VALUES (?, ?, ?, ?)";

            int userId;
            try (PreparedStatement ps1 = db.prepareStatement(user, Statement.RETURN_GENERATED_KEYS)) {
                ps1.setString(1, firstName);
                ps1.setString(2, lastName);
                ps1.setString(3, username);
                ps1.setString(4, password);

                int res = ps1.executeUpdate();
                System.out.println("User rows inserted: " + res);

                ResultSet rs = ps1.getGeneratedKeys();
                rs.next();
                userId = rs.getInt(1);
            }

            String sql = "INSERT INTO Catches (UserID, SpeciesName, LocationName, BaitType, DateCaught, Notes, Length, Weight) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = db.prepareStatement(sql)) {
                ps.setInt(1, (userId));
                ps.setString(2, speciesStr);
                ps.setString(3, locationStr);
                ps.setString(4, baitStr);
                ps.setString(5, dateCaught);
                ps.setString(6, notes);
                ps.setDouble(7, Double.parseDouble(lengthStr));
                ps.setDouble(8, Double.parseDouble(weightStr));
                int rows = ps.executeUpdate();
                System.out.println("Rows inserted: " + rows);
            }

            try (PreparedStatement verifyUser = db.prepareStatement("SELECT * FROM Users WHERE UserID = ?")) {
                verifyUser.setInt(1, userId);
                ResultSet rs = verifyUser.executeQuery();

                assertTrue(rs.next());
                assertEquals(userID, rs.getInt("UserID"));
                assertEquals(firstName, rs.getString("FirstName"));
                assertEquals(lastName, rs.getString("LastName"));
                assertEquals(username, rs.getString("Username"));
                assertEquals(password, rs.getString("PasswordHash"));
            }

            try (PreparedStatement verifyCatch = db.prepareStatement("SELECT * FROM Catches WHERE UserID = ?")) {
                verifyCatch.setInt(1, userId);
                ResultSet rs = verifyCatch.executeQuery();

                assertTrue(rs.next());
                assertEquals(userID, rs.getInt("UserID"));
                assertEquals("bluegill", rs.getString("SpeciesName"));
                assertEquals(locationStr, rs.getString("LocationName"));
                assertEquals(baitStr, rs.getString("BaitType"));
                assertEquals(dateCaught, rs.getString("DateCaught"));
                assertEquals(notes, rs.getString("Notes"));
                assertEquals(Double.parseDouble(lengthStr), rs.getDouble("Length"));
                assertEquals(Double.parseDouble(weightStr), rs.getDouble("Weight"));
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

            Integer userID = 1;
            String firstName = "Test";
            String lastName = "User";
            String username = "TestUser";
            String password = "Test";

            String user = "INSERT INTO Users (firstName, lastName, userName, passwordHash) VALUES (?, ?, ?, ?)";

            int userId;
            try (PreparedStatement ps1 = db.prepareStatement(user, Statement.RETURN_GENERATED_KEYS)) {
                ps1.setString(1, firstName);
                ps1.setString(2, lastName);
                ps1.setString(3, username);
                ps1.setString(4, password);

                int res = ps1.executeUpdate();
                System.out.println("User rows inserted: " + res);

                ResultSet rs = ps1.getGeneratedKeys();
                rs.next();
                userId = rs.getInt(1);
            }

            String sql = "INSERT INTO Catches (UserID, SpeciesName, LocationName, BaitType, DateCaught, Notes, Length, Weight) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = db.prepareStatement(sql)) {
                ps.setInt(1, (userId));
                ps.setString(2, speciesStr);
                ps.setString(3, locationStr);
                ps.setString(4, baitStr);
                ps.setString(5, dateCaught);
                ps.setString(6, notes);
                ps.setDouble(7, Double.parseDouble(lengthStr));
                ps.setDouble(8, Double.parseDouble(weightStr));
                int rows = ps.executeUpdate();
                System.out.println("Rows inserted: " + rows);
            }

            try (PreparedStatement ps = db.prepareStatement(sql)) {
                ps.setInt(1, (userId));
                ps.setString(2, speciesStr2);
                ps.setString(3, locationStr2);
                ps.setString(4, baitStr2);
                ps.setString(5, dateCaught2);
                ps.setString(6, notes2);
                ps.setDouble(7, Double.parseDouble(lengthStr2));
                ps.setDouble(8, Double.parseDouble(weightStr2));
                int rows = ps.executeUpdate();
                System.out.println("Rows inserted: " + rows);
            }

            try (PreparedStatement verifyUser = db.prepareStatement("SELECT * FROM Users WHERE UserID = ?")) {
                verifyUser.setInt(1, userId);
                ResultSet rs = verifyUser.executeQuery();

                assertTrue(rs.next());
                assertEquals(userID, rs.getInt("UserID"));
                assertEquals(firstName, rs.getString("FirstName"));
                assertEquals(lastName, rs.getString("LastName"));
                assertEquals(username, rs.getString("Username"));
                assertEquals(password, rs.getString("PasswordHash"));
            }

            try (PreparedStatement verifyCatch = db.prepareStatement("SELECT * FROM Catches WHERE UserID = ?")) {
                verifyCatch.setInt(1, userId);
                ResultSet rs = verifyCatch.executeQuery();

                assertTrue(rs.next());
                assertEquals(userID, rs.getInt("UserID"));
                assertEquals("bluegill", rs.getString("SpeciesName"));
                assertEquals(locationStr, rs.getString("LocationName"));
                assertEquals(baitStr, rs.getString("BaitType"));
                assertEquals(dateCaught, rs.getString("DateCaught"));
                assertEquals(notes, rs.getString("Notes"));
                assertEquals(Double.parseDouble(lengthStr), rs.getDouble("Length"));
                assertEquals(Double.parseDouble(weightStr), rs.getDouble("Weight"));
            }

            CatchDao dao = new CatchDao();

            int userIDSearch = 1;
            String species = "bluegill";
            String location = "mississippi river";
            String bait = "gulp! minnow";

            List<Catch> results = dao.searchOutput(userIDSearch, species, location, bait);
            System.out.println(results);

            assertEquals(results.size(), 1);
        }
    }
}
