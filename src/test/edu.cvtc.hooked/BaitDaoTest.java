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

    private String formatted(String input) {
        if (input == null || input.isEmpty()) return input;

        String[] words = input.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String w : words) {
            sb.append(Character.toUpperCase(w.charAt(0)))
                    .append(w.substring(1))
                    .append(" ");
        }

        return sb.toString().trim();
    }

    @Test
    void insertAndExists_roundTrip() throws Exception {
        BaitDao dao = new BaitDao();

        String rawName = "JUnitBait_" + System.currentTimeMillis();
        String expectedName = formatted(rawName);

        Bait b = new Bait();
        b.setName(rawName);
        b.setNotes("Test bait notes");

        dao.insert(b);

        assertNotNull(b.getId(), "After insert, BaitID should be set");

        assertTrue(dao.exists(expectedName));
    }

    @Test
    void findAll_containsInsertedBaitWithFields() throws Exception {
        BaitDao dao = new BaitDao();

        String rawName = "JUnitBaitFindAll_" + System.currentTimeMillis();
        String expectedName = formatted(rawName);

        Bait b = new Bait();
        b.setName(rawName);
        b.setNotes("Special notes for findAll test");

        dao.insert(b);

        List<Bait> all = dao.findAll();

        Bait found = all.stream()
                .filter(bt -> expectedName.equals(bt.getName()))
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("findAll should include the bait we just inserted"));

        assertEquals(expectedName, found.getName());
        assertEquals(formatted("Special notes for findAll test"), found.getNotes());
    }

    @Test
    void exists_returnsFalseForUnknownBait() throws Exception {
        BaitDao dao = new BaitDao();

        String rawMissing = "DoesNotExistBait_" + System.currentTimeMillis();
        String expectedMissing = formatted(rawMissing);

        assertFalse(dao.exists(expectedMissing));
    }
}
