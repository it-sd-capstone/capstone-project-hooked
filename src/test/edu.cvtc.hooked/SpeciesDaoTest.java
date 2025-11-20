package edu.cvtc.hooked;

import edu.cvtc.hooked.dao.SpeciesDao;
import edu.cvtc.hooked.model.Species;
import edu.cvtc.hooked.util.DbUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SpeciesDaoTest {

    @BeforeAll
    static void setupDatabase() {
        // Make sure all the tables exist before any of these tests run.
        DbUtil.ensureSchema();
    }

    @Test
    void insertAndFindBySpeciesName_roundTrip() throws Exception {
        // Using a unique species name so it never conflicts with anything already in the DB.
        String uniqueName = "JUnitSpecies_" + System.currentTimeMillis();

        SpeciesDao dao = new SpeciesDao();

        // Build a species the same way the app would.
        Species toInsert = new Species(
                uniqueName,
                20.5,
                3.4
        );

        // Try inserting the species into the database.
        dao.insert(toInsert);

        // Now make sure we can actually pull it back out.
        Optional<Species> foundOpt = dao.findBySpeciesName(uniqueName);
        assertTrue(foundOpt.isPresent(), "Species was inserted so it should be found");

        Species found = foundOpt.get();

        // DB should have assigned this.
        assertNotNull(found.getSpeciesId(), "DB should give the species an ID");

        // Confirm the rest of the fields match what I put in.
        assertEquals(uniqueName, found.getSpeciesName());
        assertEquals(20.5, found.getLength(), 0.0001);
        assertEquals(3.4, found.getWeight(), 0.0001);
    }

    @Test
    void insertAndFindById_roundTrip() throws Exception {
        // Insert a species, look it up by name to get its ID, then look it up by ID.
        String uniqueName = "JUnitSpeciesById_" + System.currentTimeMillis();

        SpeciesDao dao = new SpeciesDao();

        Species toInsert = new Species(
                uniqueName,
                15.0,
                2.0
        );

        dao.insert(toInsert);

        Optional<Species> byNameOpt = dao.findBySpeciesName(uniqueName);
        assertTrue(byNameOpt.isPresent(), "Expected to find species by name first");

        int id = byNameOpt.get().getSpeciesId();

        Optional<Species> byIdOpt = dao.findById(id);
        assertTrue(byIdOpt.isPresent(), "Expected to find species by ID");

        Species found = byIdOpt.get();

        assertEquals(id, found.getSpeciesId());
        assertEquals(uniqueName, found.getSpeciesName());
        assertEquals(15.0, found.getLength(), 0.0001);
        assertEquals(2.0, found.getWeight(), 0.0001);
    }

    @Test
    void findBySpeciesName_returnsEmptyWhenNotFound() throws Exception {
        SpeciesDao dao = new SpeciesDao();

        // Pick a species name that is guaranteed to not exist.
        String missingName = "MissingSpecies_" + System.currentTimeMillis();

        Optional<Species> result = dao.findBySpeciesName(missingName);

        // Should not find anything for a name that doesn't exist.
        assertTrue(result.isEmpty(), "Unknown species name should come back as empty");
    }

    @Test
    void findById_returnsEmptyWhenNotFound() throws Exception {
        SpeciesDao dao = new SpeciesDao();

        // Use a clearly invalid ID that should not exist.
        Optional<Species> result = dao.findById(-12345);

        assertTrue(result.isEmpty(), "Unknown species ID should come back as empty");
    }

    @Test
    void findAll_containsInsertedSpecies() throws Exception {
        SpeciesDao dao = new SpeciesDao();

        // Insert a species with a unique name.
        String uniqueName = "JUnitSpeciesFindAll_" + System.currentTimeMillis();

        Species toInsert = new Species(
                uniqueName,
                12.3,
                1.4
        );

        dao.insert(toInsert);

        // Now load everything and make sure our species is in the list.
        List<Species> all = dao.findAll();

        boolean found = all.stream()
                .anyMatch(s -> uniqueName.equals(s.getSpeciesName()));

        assertTrue(found, "findAll should include the species we just inserted");
    }
}


