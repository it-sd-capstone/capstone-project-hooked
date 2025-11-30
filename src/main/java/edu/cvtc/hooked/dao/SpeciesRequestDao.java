package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.model.SpeciesRequest;
import edu.cvtc.hooked.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpeciesRequestDao {

    public void insert(String speciesName, Integer userId) throws SQLException {
        String sql = """
            INSERT INTO SpeciesRequests(SpeciesName, UserID)
            VALUES(?, ?)
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, speciesName);
            if (userId != null) {
                ps.setInt(2, userId);
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.executeUpdate();
        }
    }

    public List<SpeciesRequest> findAll() throws SQLException {
        String sql = """
            SELECT RequestID, SpeciesName, UserID, RequestedAt
            FROM SpeciesRequests
            ORDER BY RequestedAt DESC, RequestID DESC
            """;

        List<SpeciesRequest> results = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SpeciesRequest r = new SpeciesRequest();
                r.setRequestId(rs.getInt("RequestID"));
                r.setSpeciesName(rs.getString("SpeciesName"));
                int uid = rs.getInt("UserID");
                r.setUserId(rs.wasNull() ? null : uid);
                r.setRequestedAt(rs.getString("RequestedAt"));
                results.add(r);
            }
        }

        return results;
    }

    public void deleteById(int requestId) throws SQLException {
        String sql = "DELETE FROM SpeciesRequests WHERE RequestID = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ps.executeUpdate();
        }
    }

}
