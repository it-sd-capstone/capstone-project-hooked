package edu.cvtc.hooked;

import model.HookedApplication;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class MainTest {
    @Test
    void createConnection() throws SQLException {
        Connection db = HookedApplication.createConnection();
        assertNotNull(db);
        assertFalse(db.isClosed());
        db.close();
        assertTrue(db.isClosed());
    }


    @Test
    void queryRaw() throws SQLException {
        try (Connection db = HookedApplication.createConnection()) {
            ResultSet rows = HookedApplication.queryRaw(db, "SELECT 5 AS result");
            assertNotNull(rows);
            assertTrue(rows.next());
            int result = rows.getInt("result");
            assertEquals(5, result);
            rows.close();
        }
    }
}
