package edu.cvtc.hooked;

import edu.cvtc.hooked.util.DbUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
 
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
}
