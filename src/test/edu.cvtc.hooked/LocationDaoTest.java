package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.model.Location;
import edu.cvtc.hooked.util.DbUtil;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LocationDaoTest {

    private static LocationDao locationDao;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Initialize schema and get DAO
        DbUtil.ensureSchema();
        locationDao = new LocationDao();

        // Optional: clear table for a clean test run
        try (Connection conn = DbUtil.getConnection();
             var stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM Location");
        }
    }

    @Test
    @Order(1)
    void testInsertAndFindById() throws SQLException {
        Location l = new Location();
        l.setLocationName("Test Lake");
        l.setState("WI");
        l.setCreatedByUserId(null);

        locationDao.insert(l);
        assertNotNull(l.getLocationId(), "Location ID should be set after insert");

        Optional<Location> fetched = locationDao.findById(l.getLocationId());
        assertTrue(fetched.isPresent(), "Inserted location should be found");
        assertEquals("Test Lake", fetched.get().getLocationName());
        assertEquals("WI", fetched.get().getState());
    }

    @Test
    @Order(2)
    void testFindByNameAndState() throws SQLException {
        Optional<Location> fetched = locationDao.findByNameAndState("Test Lake", "WI");
        assertTrue(fetched.isPresent(), "Location should be found by name and state");
        assertEquals("WI", fetched.get().getState());
    }

    @Test
    @Order(3)
    void testUpdate() throws SQLException {
        Optional<Location> opt = locationDao.findByNameAndState("Test Lake", "WI");
        assertTrue(opt.isPresent());

        Location l = opt.get();
        l.setLocationName("Updated Lake");
        l.setState("MN");
        locationDao.update(l);

        Optional<Location> updated = locationDao.findById(l.getLocationId());
        assertTrue(updated.isPresent());
        assertEquals("Updated Lake", updated.get().getLocationName());
        assertEquals("MN", updated.get().getState());
    }

    @Test
    @Order(4)
    void testFindAllSorted() throws SQLException {
        // Insert a few more
        locationDao.insert(new Location("Alpha Lake", "WI"));
        locationDao.insert(new Location("Beta River", "WI"));
        locationDao.insert(new Location("Zeta Pond", "WI"));

        List<Location> asc = locationDao.findAllSorted("locationName", "asc");
        assertFalse(asc.isEmpty());
        assertEquals("Alpha Lake", asc.get(0).getLocationName());

        List<Location> desc = locationDao.findAllSorted("locationName", "desc");
        assertFalse(desc.isEmpty());
        assertEquals("Zeta Pond", desc.get(0).getLocationName());
    }

    @Test
    @Order(5)
    void testDelete() throws SQLException {
        Optional<Location> opt = locationDao.findByNameAndState("Updated Lake", "MN");
        assertTrue(opt.isPresent());

        int id = opt.get().getLocationId();
        locationDao.deleteById(id);

        Optional<Location> deleted = locationDao.findById(id);
        assertFalse(deleted.isPresent(), "Location should be deleted");
    }
}
