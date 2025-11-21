package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.model.User;
import edu.cvtc.hooked.util.DbUtil;
import java.sql.*;
import java.util.Optional;

public class UserDao {
    public void insert(User u) throws SQLException {
        String sql = "INSERT INTO Users(firstName,lastName,userName,passwordHash) VALUES(?,?,?,?)";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getUserName());
            ps.setString(4, u.getPasswordHash());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setUserId(rs.getInt(1));
                }
            }
        }
    }

    public Optional<User> findByUserName(String userName) throws SQLException {
        String sql = "SELECT UserID, firstName, lastName, userName, passwordHash FROM Users WHERE userName=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User(
                            rs.getInt("UserID"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("userName"),
                            rs.getString("passwordHash")
                    );
                    return Optional.of(u);
                }
                return Optional.empty();
            }
        }
    }
}

