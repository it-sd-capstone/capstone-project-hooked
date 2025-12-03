package edu.cvtc.hooked;

import edu.cvtc.hooked.dao.SpeciesDao;
import edu.cvtc.hooked.model.Species;
import edu.cvtc.hooked.util.DbUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpeciesDaoTest {

    @BeforeAll
    static void setupDatabase() throws Exception {
        // Use the test DB location if you have that convention
        System.setProperty("hooked.test.db", "true");

        // Clean the Species table so tests are predictable
        try (Connection c = DbUtil.getConnection();
             Statement stmt = c.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS Species");
        }

        DbUtil.ensureSchema();
    }

    @Test
    void insertAndExists_roundTrip() throws Exception {
        SpeciesDao dao = new SpeciesDao();

        String uniqueName = "JUnitSpecies_" + System.currentTimeMillis();

        Species s = new Species();
        s.setSpeciesName(uniqueName);
        s.setMaxLength(25.0);
        s.setMaxWeight(8.0);

        dao.insert(s);

        assertNotNull(s.getSpeciesId(), "After insert, SpeciesID should be set");

        boolean exists = dao.exists(uniqueName);
        assertTrue(exists, "exists(uniqueName) should return true after insert");
    }

    @Test
    void findAll_containsInsertedSpeciesWithFields() throws Exception {
        SpeciesDao dao = new SpeciesDao();

        String uniqueName = "JUnitSpeciesFindAll_" + System.currentTimeMillis();

        Species s = new Species();
        s.setSpeciesName(uniqueName);
        s.setMaxLength(30.5);
        s.setMaxWeight(12.3);

        dao.insert(s);

        List<Species> all = dao.findAll();

        Species found = all.stream()
                .filter(sp -> uniqueName.equals(sp.getSpeciesName()))
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("findAll should include the species we just inserted"));

        assertEquals(uniqueName, found.getSpeciesName());
        assertEquals(30.5, found.getMaxLength());
        assertEquals(12.3, found.getMaxWeight());
    }

    @Test
    void exists_returnsFalseForUnknownSpecies() throws Exception {
        SpeciesDao dao = new SpeciesDao();

        String missingName = "DoesNotExist_" + System.currentTimeMillis();

        assertFalse(dao.exists(missingName),
                "exists() should be false for a species that is not in the DB");
    }
}
