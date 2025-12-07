package edu.cvtc.hooked;

import edu.cvtc.hooked.dao.CatchDao;
import edu.cvtc.hooked.dao.UserDao;
import edu.cvtc.hooked.model.Catch;
import edu.cvtc.hooked.model.User;
import edu.cvtc.hooked.util.DbUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CatchDaoTest {

    @BeforeAll
    static void setupDatabase() {
        // Make sure all tables exist so we can insert users and catches.
        System.setProperty("hooked.test.db", "true");
        DbUtil.ensureSchema();
    }

    // Helper: create a valid user to satisfy foreign key constraints.
    private int createTestUser() throws Exception {
        UserDao userDao = new UserDao();
        String uniqueName = "user_" + System.currentTimeMillis();
        String email = "test" + System.currentTimeMillis() + "@example.com";
        User u = new User("Test", "User", uniqueName, email, null, null, "hash123");
        userDao.insert(u);
        return u.getUserId();
    }

    @Test
    public void insertAndFindById_roundTrip() throws Exception {
        // Must create a real user because Catches has a foreign key on UserID.
        int userId = createTestUser();

        CatchDao dao = new CatchDao();

        Catch toInsert = new Catch(
                userId,
                "Walleye",
                "Lake Superior",
                "Minnow",
                "2024-10-01",
                "Testing notes",
                22.5,
                4.3
        );

        dao.insert(toInsert);
        assertNotNull(toInsert.getCatchId(), "Catch should be assigned an ID on insert.");

        Optional<Catch> foundOpt = dao.findById(toInsert.getCatchId());
        assertTrue(foundOpt.isPresent(), "Catch should be found by ID.");

        Catch found = foundOpt.get();

        assertEquals(userId, found.getUserId());
        assertEquals("Walleye", found.getSpeciesName());
        assertEquals("Lake Superior", found.getLocationName());
        assertEquals("Minnow", found.getBaitType());
        assertEquals("2024-10-01", found.getDateCaught());
        assertEquals("Testing notes", found.getNotes());
        assertEquals(22.5, found.getLength(), 0.0001);
        assertEquals(4.3, found.getWeight(), 0.0001);
    }

    @Test
    public void findById_returnsEmptyWhenMissing() throws Exception {
        CatchDao dao = new CatchDao();

        Optional<Catch> result = dao.findById(-99999);

        assertTrue(result.isEmpty(), "Missing ID should produce Optional.empty");
    }

    @Test
    public void findByUserId_returnsInsertedCatches() throws Exception {
        int userId = createTestUser();

        CatchDao dao = new CatchDao();

        Catch c1 = new Catch(userId, "Bass", "River", "Worm", "2024-11-02", "note1", 14.0, 2.1);
        Catch c2 = new Catch(userId, "Pike", "River", "Spinner", "2024-11-03", "note2", 30.0, 7.8);

        dao.insert(c1);
        dao.insert(c2);

        List<Catch> list = dao.findByUserId(userId);

        assertFalse(list.isEmpty(), "User should have catches.");

        boolean foundBass = list.stream().anyMatch(c -> c.getSpeciesName().equals("Bass"));
        boolean foundPike = list.stream().anyMatch(c -> c.getSpeciesName().equals("Pike"));

        assertTrue(foundBass, "Bass should be in the results.");
        assertTrue(foundPike, "Pike should be in the results.");
    }

    @Test
    public void findByUserId_returnsEmptyWhenNoCatches() throws Exception {
        int userId = createTestUser();
        CatchDao dao = new CatchDao();

        List<Catch> list = dao.findByUserId(userId);

        assertTrue(list.isEmpty(), "Brand new user should have no catches.");
    }

    @Test
    public void searchOutput_filtersByUserId() throws Exception {
        int userId = createTestUser();
        CatchDao dao = new CatchDao();

        Catch c = new Catch(userId, "Perch", "Dock", "Jig", "2024-09-09", "test", 11.0, 0.9);
        dao.insert(c);

        List<Catch> results = dao.searchOutput(userId, (String) null, null, null);

        assertFalse(results.isEmpty(), "Search by userId should find catch.");
        assertEquals("Perch", results.get(0).getSpeciesName());
    }

    @Test
    public void searchOutput_filtersBySpecies() throws Exception {
        int userId = createTestUser();
        CatchDao dao = new CatchDao();

        Catch c = new Catch(userId, "Trout", "Creek", "Fly", "2024-08-08", "rain", 12.3, 1.0);
        dao.insert(c);

        List<Catch> results = dao.searchOutput(null, "Trout", null, null);

        assertFalse(results.isEmpty(), "Search by species should find Trout.");
        assertEquals("Trout", results.get(0).getSpeciesName());
    }

    @Test
    public void searchOutput_returnsEmptyForNoMatches() throws Exception {
        CatchDao dao = new CatchDao();

        List<Catch> results = dao.searchOutput(null, "FakeFish", null, null);

        assertTrue(results.isEmpty(), "Fake species should not return results.");
    }

    @Test
    public void update_changesFields() throws Exception {
        int userId = createTestUser();
        CatchDao dao = new CatchDao();

        Catch c = new Catch(userId, "Bass", "Shore", "Worm", "2024-01-01", "test", 10.0, 1.2);
        dao.insert(c);

        c.setSpeciesName("UpdatedBass");
        c.setLength(55.5);
        dao.update(c);

        Catch updated = dao.findById(c.getCatchId()).get();
        assertEquals("UpdatedBass", updated.getSpeciesName());
        assertEquals(55.5, updated.getLength());
    }

    @Test
    public void deleteForUser_removesCatch() throws Exception {
        int userId = createTestUser();
        CatchDao dao = new CatchDao();

        Catch c = new Catch(userId, "Walleye", "Bay", "Minnow",
                "2024-05-05", "note", 19, 3);
        dao.insert(c);

        dao.deleteForUser(c.getCatchId(), userId);

        assertTrue(dao.findById(c.getCatchId()).isEmpty());
    }

    @Test
    public void deleteById_adminDeletesCatch() throws Exception {
        int userId = createTestUser();
        CatchDao dao = new CatchDao();

        Catch c = new Catch(userId, "Carp", "Slough", "Corn",
                "2024-06-10", "muddy", 25, 8);
        dao.insert(c);

        dao.deleteById(c.getCatchId());
        assertTrue(dao.findById(c.getCatchId()).isEmpty());
    }

    @Test
    public void deleteAll_removesEverything() throws Exception {
        int userId = createTestUser();
        CatchDao dao = new CatchDao();

        dao.insert(new Catch(userId, "A", "L1", "B1", "2024", "", 1, 1));
        dao.insert(new Catch(userId, "B", "L2", "B2", "2024", "", 2, 2));

        dao.deleteAll();

        assertTrue(dao.findAll().isEmpty());
    }

    @Test
    public void updateAsAdmin_canChangeOwner() throws Exception {
        int user1 = createTestUser();
        int user2 = createTestUser();

        CatchDao dao = new CatchDao();

        Catch c = new Catch(user1, "Crappie", "Pond", "Jig",
                "2024-02-02", "", 9, 0.6);

        dao.insert(c);

        c.setUserId(user2); // Admin takes ownership
        c.setSpeciesName("AdminUpdated");
        dao.updateAsAdmin(c);

        Catch found = dao.findById(c.getCatchId()).get();
        assertEquals("AdminUpdated", found.getSpeciesName());
        assertEquals(user2, found.getUserId());
    }

    @Test
    public void findAll_returnsMultipleCatches() throws Exception {
        int userId = createTestUser();
        CatchDao dao = new CatchDao();

        dao.insert(new Catch(userId, "Bluegill", "Dock1", "Bobber", "2024", "", 5, 0.2));
        dao.insert(new Catch(userId, "Perch", "Dock2", "Jig", "2024", "", 8, 0.4));

        List<Catch> list = dao.findAll();
        assertTrue(list.size() >= 2);
    }

    @Test
    public void searchOutput_multiSpeciesFuzzy() throws Exception {
        int userId = createTestUser();
        CatchDao dao = new CatchDao();

        dao.insert(new Catch(userId, "Lake Trout", "Deep", "Spoon", "2024", "", 30, 10));
        dao.insert(new Catch(userId, "Brown Trout", "Shallow", "Fly", "2024", "", 18, 3));

        List<String> speciesTerms = List.of("trout");

        List<Catch> results = dao.searchOutput(userId, speciesTerms, null, null);

        assertTrue(results.size() >= 2);
    }

}


