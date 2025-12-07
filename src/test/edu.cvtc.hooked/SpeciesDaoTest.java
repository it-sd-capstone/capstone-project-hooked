package edu.cvtc.hooked;

import edu.cvtc.hooked.dao.SpeciesDao;
import edu.cvtc.hooked.model.Species;
import edu.cvtc.hooked.util.DbUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpeciesDaoTest {

    @BeforeAll
    static void setupDatabase() throws Exception {
        try (Connection c = DbUtil.getConnection();
             Statement stmt = c.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS Species");
        }
        DbUtil.ensureSchema();
    }

    private Species insertTestSpecies(SpeciesDao dao, String name,
                                      double len, double wt, Integer userId) throws Exception {

        Species s = new Species(name, len, wt, userId);
        dao.insert(s);
        assertNotNull(s.getSpeciesId());
        return s;
    }

    @Test
    void insert_exists_findById_roundTrip() throws Exception {
        SpeciesDao dao = new SpeciesDao();
        String rawName = "SpeciesA_" + System.currentTimeMillis();
        String expectedName = formatted(rawName);

        Species s = new Species(rawName, 10.0, 5.0, 1);
        dao.insert(s);

        assertTrue(dao.exists(expectedName), "exists() should check the formatted name");

        Optional<Species> found = dao.findById(s.getSpeciesId());
        assertTrue(found.isPresent());

        Species sp = found.get();
        assertEquals(expectedName, sp.getSpeciesName());
        assertEquals(10.0, sp.getMaxLength());
        assertEquals(5.0, sp.getMaxWeight());
        assertEquals(1, sp.getCreatedByUserId());
    }

    @Test
    void findByName_shouldLocateSpecies() throws Exception {
        SpeciesDao dao = new SpeciesDao();

        String rawName = "SpeciesByName_" + System.currentTimeMillis();
        String expectedName = formatted(rawName);

        Species s = new Species(rawName, 7.0, 3.0, 2);
        dao.insert(s);

        Optional<Species> found = dao.findByName(expectedName);
        assertTrue(found.isPresent());
        assertEquals(expectedName, found.get().getSpeciesName());
    }

    @Test
    void findByName_returnsEmptyForUnknown() throws Exception {
        SpeciesDao dao = new SpeciesDao();
        assertTrue(dao.findByName("Unknown_" + System.currentTimeMillis()).isEmpty());
    }

    @Test
    void findAllSorted_byLengthAscDesc() throws Exception {
        SpeciesDao dao = new SpeciesDao();

        insertTestSpecies(dao, "LenSmall_" + System.currentTimeMillis(), 5.0, 1.0, null);
        insertTestSpecies(dao, "LenBig_" + System.currentTimeMillis(), 50.0, 2.0, null);

        List<Species> asc = dao.findAllSorted("length", "asc");
        assertTrue(asc.size() >= 2);
        assertTrue(asc.get(0).getMaxLength() <= asc.get(1).getMaxLength());

        List<Species> desc = dao.findAllSorted("length", "desc");
        assertTrue(desc.size() >= 2);
        assertTrue(desc.get(0).getMaxLength() >= desc.get(1).getMaxLength());
    }

    @Test
    void findAllSorted_byWeight() throws Exception {
        SpeciesDao dao = new SpeciesDao();

        insertTestSpecies(dao, "Wt1_" + System.currentTimeMillis(), 10.0, 2.0, null);
        insertTestSpecies(dao, "Wt2_" + System.currentTimeMillis(), 10.0, 10.0, null);

        List<Species> sorted = dao.findAllSorted("weight", "asc");
        assertTrue(sorted.get(0).getMaxWeight() <= sorted.get(1).getMaxWeight());
    }

    @Test
    void findAllSorted_defaultSpeciesNameOrder() throws Exception {
        SpeciesDao dao = new SpeciesDao();

        String aName = "A_" + System.currentTimeMillis();
        String zName = "Z_" + System.currentTimeMillis();

        insertTestSpecies(dao, zName, 5.0, 1.0, null);
        insertTestSpecies(dao, aName, 5.0, 1.0, null);

        List<Species> sorted = dao.findAllSorted("species", "asc");
        assertTrue(sorted.stream().anyMatch(s -> s.getSpeciesName().equals(aName)));
        assertTrue(sorted.stream().anyMatch(s -> s.getSpeciesName().equals(zName)));
    }

    @Test
    void update_shouldModifyFields() throws Exception {
        SpeciesDao dao = new SpeciesDao();

        String name = "UpdateTest_" + System.currentTimeMillis();
        Species s = insertTestSpecies(dao, name, 10.0, 10.0, 1);

        // Change the data
        s.setSpeciesName(name);
        s.setMaxLength(99.0);
        s.setMaxWeight(88.0);

        dao.update(s);

        Species updated = dao.findById(s.getSpeciesId()).orElseThrow();
        assertEquals(99.0, updated.getMaxLength());
        assertEquals(88.0, updated.getMaxWeight());
    }

    @Test
    void deleteById_removesSpecies() throws Exception {
        SpeciesDao dao = new SpeciesDao();

        String name = "DeleteMe_" + System.currentTimeMillis();
        Species s = insertTestSpecies(dao, name, 5.0, 5.0, null);

        dao.deleteById(s.getSpeciesId());

        assertFalse(dao.exists(name));
        assertTrue(dao.findById(s.getSpeciesId()).isEmpty());
    }

    private String formatted(String raw) {
        return new Species(raw, 0, 0, null).getSpeciesName();
    }
}