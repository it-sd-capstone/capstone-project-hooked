package edu.cvtc.hooked;

import edu.cvtc.hooked.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testDefaultConstructorStartsEmpty() {
        // Just checking that the empty constructor does not prefill anything.
        User user = new User();

        assertNull(user.getUserId());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getUserName());
        assertNull(user.getPasswordHash());
    }

    @Test
    public void testFullConstructorSetsAllFields() {
        // This uses the constructor that takes the id and all fields.
        // Making sure every value lands in the right spot.
        User user = new User(
                1,
                "John",
                "Smith",
                "jsmith",
                "hashed123"
        );

        assertEquals(1, user.getUserId());
        assertEquals("John", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("jsmith", user.getUserName());
        assertEquals("hashed123", user.getPasswordHash());
    }

    @Test
    public void testNewUserConstructorHasNullId() {
        // The new-user constructor should not set an ID.
        User user = new User(
                "Jane",
                "Doe",
                "jdoe",
                "hash456"
        );

        assertNull(user.getUserId());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("jdoe", user.getUserName());
        assertEquals("hash456", user.getPasswordHash());
    }

    @Test
    public void testSettersAndGetters() {
        // Making sure all setters and getters behave correctly.
        User user = new User();

        user.setUserId(25);
        user.setFirstName("Alpha");
        user.setLastName("Tester");
        user.setUserName("alphaTester");
        user.setPasswordHash("hash789");

        assertEquals(25, user.getUserId());
        assertEquals("Alpha", user.getFirstName());
        assertEquals("Tester", user.getLastName());
        assertEquals("alphaTester", user.getUserName());
        assertEquals("hash789", user.getPasswordHash());
    }

    @Test
    public void testGetDisplayName() {
        // Should return "firstName lastName"
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");

        assertEquals("Test User", user.getDisplayName());
    }

    @Test
    public void testToStringIncludesIdAndUserNameNotPassword() {
        // toString should help with debugging but never show the hash.
        User user = new User(
                7,
                "FName",
                "LName",
                "coolUser",
                "superSecretHash"
        );

        String text = user.toString();

        assertTrue(text.contains("7"));
        assertTrue(text.contains("coolUser"));
        assertFalse(text.contains("superSecretHash"));
    }

    @Test
    public void testEqualsUsesUserName() {
        // Two users with the same userName should be equal.
        User a = new User(
                1,
                "A",
                "B",
                "sameUser",
                "h1"
        );
        User b = new User(
                2,
                "X",
                "Y",
                "sameUser",
                "h2"
        );

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testNotEqualWhenUserNameDiffers() {
        // Different userNames should not be equal.
        User a = new User(
                1,
                "A",
                "B",
                "userOne",
                "h1"
        );
        User b = new User(
                2,
                "X",
                "Y",
                "userTwo",
                "h2"
        );

        assertNotEquals(a, b);
    }
}

