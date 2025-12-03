package edu.cvtc.hooked;

import edu.cvtc.hooked.dao.BaitDao;
import edu.cvtc.hooked.model.Bait;
import edu.cvtc.hooked.util.DbUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaitDaoTest {

    @BeforeAll
    static void setupDatabase() throws Exception {
        // Use the test DB location (same convention as SpeciesDaoTest)
        System.setProperty("hooked.test.db", "true");

        // Clean the Bait table so tests are predictable
        try (Connection c = DbUtil.getConnection();
             Statement stmt = c.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS Bait");
        }

        // Recreate schema including Bait table
        DbUtil.ensureSchema();
    }

    @Test
    void insertAndExists_roundTrip() throws Exception {
        BaitDao dao = new BaitDao();

        String uniqueName = "JUnitBait_" + System.currentTimeMillis();

        Bait b = new Bait();
        b.setName(uniqueName);
        b.setNotes("Test bait notes");

        dao.insert(b);

        assertNotNull(b.getId(), "After insert, BaitID should be set");

        boolean exists = dao.exists(uniqueName);
        assertTrue(exists, "exists(uniqueName) should return true after insert");
    }

    @Test
    void findAll_containsInsertedBaitWithFields() throws Exception {
        BaitDao dao = new BaitDao();

        String uniqueName = "JUnitBaitFindAll_" + System.currentTimeMillis();

        Bait b = new Bait();
        b.setName(uniqueName);
        b.setNotes("Special notes for findAll test");

        dao.insert(b);

        List<Bait> all = dao.findAll();

        Bait found = all.stream()
                .filter(bt -> uniqueName.equals(bt.getName()))
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("findAll should include the bait we just inserted"));

        assertEquals(uniqueName, found.getName());
        assertEquals("Special notes for findAll test", found.getNotes());
    }

    @Test
    void exists_returnsFalseForUnknownBait() throws Exception {
        BaitDao dao = new BaitDao();

        String missingName = "DoesNotExistBait_" + System.currentTimeMillis();

        assertFalse(dao.exists(missingName),
                "exists() should be false for a bait that is not in the DB");
    }
}
