package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.model.User;
import edu.cvtc.hooked.util.DbUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {
    public void insert(User u) throws SQLException {
        String sql = "INSERT INTO Users(firstName,lastName,userName,email,resetHash, resetTime, passwordHash) VALUES(?,?,?,?,?,?,?)";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getUserName());
            ps.setString(4, u.getEmail());
            ps.setNull(5, Types.VARCHAR);
            ps.setNull(6, Types.TIMESTAMP);
            ps.setString(7, u.getPasswordHash());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setUserId(rs.getInt(1));
                }
            }
        }
    }

    public Optional<User> findByUserName(String userName) throws SQLException {
        String sql = "SELECT UserID, firstName, lastName, userName, email, resetHash, resetTime, passwordHash FROM Users WHERE userName=?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp timeStamp = rs.getTimestamp("resetTime");
                    LocalDateTime resetTime = timeStamp != null ? timeStamp.toLocalDateTime(): null;

                    User u = new User(
                            rs.getInt("UserID"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("userName"),
                            rs.getString("email"),
                            rs.getString("resetHash"),
                            resetTime,
                            rs.getString("passwordHash")
                    );
                    return Optional.of(u);
                }
                return Optional.empty();
            }
        }
    }

    public List<User> findAll() throws SQLException {
        String sql = """
        SELECT UserID, firstName, lastName, userName, email, resethash, passwordHash, passwordHash
        FROM Users
        ORDER BY UserID
        """;

        List<User> users = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Timestamp timeStamp = rs.getTimestamp("resetTime");
            LocalDateTime resetTime = timeStamp != null ? timeStamp.toLocalDateTime(): null;

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("UserID"));
                u.setFirstName(rs.getString("firstName"));
                u.setLastName(rs.getString("lastName"));
                u.setUserName(rs.getString("userName"));
                u.setEmail(rs.getString("email"));
                u.setResetHash(rs.getString("resetHash"));
                u.setResetTime(resetTime);
                u.setPasswordHash(rs.getString("passwordHash"));
                users.add(u);
            }
        }

        return users;
    }

}

