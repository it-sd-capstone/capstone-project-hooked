package edu.cvtc.hooked;

import edu.cvtc.hooked.dao.UserDao;
import edu.cvtc.hooked.model.User;
import edu.cvtc.hooked.util.DbUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    @BeforeAll
    static void setupDatabase() {
        // make sure all the tables exist before any of these tests run
        DbUtil.ensureSchema();
    }

    @Test
    void insertAndFindByUserName_roundTrip() throws Exception {
        // using a unique username so it never conflicts with anything already in the DB
        String uniqueUserName = "junitUser_" + System.currentTimeMillis();

        UserDao dao = new UserDao();

        // build a user exactly how the app would create one
        User newUser = new User(
                "JUnit",
                "Tester",
                uniqueUserName,
                "fakeHash123"
        );

        // try inserting the user into the database
        dao.insert(newUser);

        // now make sure we can actually pull it back out
        Optional<User> foundOpt = dao.findByUserName(uniqueUserName);
        assertTrue(foundOpt.isPresent(), "User was inserted so it should be found");

        User found = foundOpt.get();

        // DB should have assigned this
        assertNotNull(found.getUserId(), "DB should give the user an ID");

        // confirm the rest of the fields match what I put in
        assertEquals("JUnit", found.getFirstName());
        assertEquals("Tester", found.getLastName());
        assertEquals(uniqueUserName, found.getUserName());
        assertEquals("fakeHash123", found.getPasswordHash());
    }

    @Test
    void findByUserName_returnsEmptyForMissingUser() throws Exception {
        UserDao dao = new UserDao();

        // pick a username that is guaranteed to not exist
        String missingUserName = "no_such_user_" + System.currentTimeMillis();

        // should not find anyone with a username that doesn't exist
        Optional<User> result = dao.findByUserName(missingUserName);

        assertTrue(result.isEmpty(), "Unknown username should come back as empty");
    }
}
