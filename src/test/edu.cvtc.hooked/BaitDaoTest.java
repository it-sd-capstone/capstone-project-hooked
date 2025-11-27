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

public class BaitDaoTest {

    @BeforeAll
    static void setup() throws Exception {
        System.setProperty("hooked.test.db", "true");
        DbUtil.ensureSchema();

        try (Connection conn = DbUtil.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM Bait");
        }
    }

    @Test
    void testInsertAndFindById() {
        BaitDao dao = new BaitDao();

        Bait b = new Bait("Nightcrawler", "Common worm bait");
        dao.insert(b);  // ID assigned by DB

        // Find the inserted record by name since ID is not assigned back onto the object
        List<Bait> list = dao.searchByTerm("Nightcrawler");

        assertTrue(list.size() >= 1);
        Bait fromDb = list.get(0);

        assertEquals("Nightcrawler", fromDb.getName());
        assertEquals("Common worm bait", fromDb.getNotes());
    }

    @Test
    void testFindAllReturnsInsertedRows() {
        BaitDao dao = new BaitDao();

        dao.insert(new Bait("Minnow", "Live bait"));
        dao.insert(new Bait("Crankbait", "Hard lure"));

        List<Bait> list = dao.findAll();

        assertTrue(list.size() >= 2);
        assertTrue(list.stream().anyMatch(b -> b.getName().equals("Minnow")));
        assertTrue(list.stream().anyMatch(b -> b.getName().equals("Crankbait")));
    }

    @Test
    void testSearchByTerm() {
        BaitDao dao = new BaitDao();

        dao.insert(new Bait("Spinnerbait", "Flash lure"));
        dao.insert(new Bait("Jig", "Good for bass"));

        List<Bait> results = dao.searchByTerm("bass");

        assertTrue(results.size() >= 1);
        assertTrue(results.stream().anyMatch(b -> b.getName().equals("Jig")));
    }
}
