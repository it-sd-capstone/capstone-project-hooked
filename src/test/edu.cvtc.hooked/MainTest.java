package edu.cvtc.hooked;

import edu.cvtc.hooked.model.SpeciesRestrictions;
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

    @Test
    void restrictions() throws SQLException, ClassNotFoundException {
        try (Connection db = DbUtil.getConnection()) {
            String speciesStr     = "bluegill";
            double length = 20.00;
            double weight = 10.00;

            SpeciesRestrictions restrictions = SpeciesRestrictions.ALL.get(speciesStr);
            assertNotNull(restrictions);

            boolean invalid = (length > restrictions.getMaxLength()) || (weight > restrictions.getMaxWeight());
            assertTrue(invalid);
        }
    }
}
