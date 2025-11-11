package edu.cvtc.hooked.model;

import java.util.Objects;

public class User {
    private Integer userId;          // null until saved
    private String firstName;
    private String lastName;
    private String userName;
    private String passwordHash;     // store a hash, not the raw password

    public User() {}

    public User(Integer userId, String firstName, String lastName,
                String userName, String passwordHash) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.passwordHash = passwordHash;
    }

    // convenience ctor for new users (id assigned by DB)
    public User(String firstName, String lastName, String userName, String passwordHash) {
        this(null, firstName, lastName, userName, passwordHash);
    }

    // getters/setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getDisplayName() { return firstName + " " + lastName; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User u = (User) o;
        // prefer stable key; username is unique
        return Objects.equals(userName, u.userName);
    }
    @Override public int hashCode() { return Objects.hash(userName); }

    @Override public String toString() {
        return "User{userId=" + userId + ", userName='" + userName + "'}";
        // intentionally omit passwordHash
    }
}
