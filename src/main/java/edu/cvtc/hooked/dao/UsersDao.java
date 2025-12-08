package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.util.DbUtil;

import java.sql.*;
import java.util.*;

public class UsersDao {

    public Map<Integer, String> findUsernamesByIds(Set<Integer> ids) throws SQLException {
        Map<Integer, String> result = new HashMap<>();

        if (ids == null || ids.isEmpty()) {
            return result;
        }

        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = "SELECT UserID, userName FROM Users WHERE UserID IN (" + placeholders + ")";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int i = 1;
            for (Integer id : ids) {
                ps.setInt(i++, id);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getInt("UserID"), rs.getString("userName"));
                }
            }
        }

        return result;
    }
}
