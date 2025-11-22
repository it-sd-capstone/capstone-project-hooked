package edu.cvtc.hooked;

import edu.cvtc.hooked.dao.LocationDao;
import edu.cvtc.hooked.model.Location;
import edu.cvtc.hooked.util.DbUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LocationDaoTest {

    private static LocationDao dao;

    @BeforeAll
    static void setup() {
        DbUtil.ensureSchema();
        dao = new LocationDao();
    }

    @Test
    void testInsertAndFindById() throws SQLException {
        Location l = new Location("Big Test Lake", "WI");
        dao.insert(l);

        assertNotNull(l.getLocationId());
        Optional<Location> found = dao.findById(l.getLocationId());

        assertTrue(found.isPresent());
        assertEquals("Big Test Lake", found.get().getLocationName());
        assertEquals("WI", found.get().getState());
    }

    @Test
    void testInsertAndFindByName() throws SQLException {
        Location l = new Location("Hidden Creek", "MN");
        dao.insert(l);

        Optional<Location> found = dao.findByName("Hidden Creek");

        assertTrue(found.isPresent());
        assertEquals("Hidden Creek", found.get().getLocationName());
        assertEquals("MN", found.get().getState());
    }

    @Test
    void testFindAllReturnsList() throws SQLException {
        List<Location> list = dao.findAll();
        assertNotNull(list);
    }
}

