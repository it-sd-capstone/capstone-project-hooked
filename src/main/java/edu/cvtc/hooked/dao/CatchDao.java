package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.model.Catch;
import edu.cvtc.hooked.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CatchDao {

    // INSERT a new catch (like UserDao.insert)
    public void insert(Catch c) throws SQLException {
        String sql = """
            INSERT INTO Catches(UserID, SpeciesID, LocationID, BaitID, DateCaught, Notes)
            VALUES(?,?,?,?,?,?)
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, c.getUserId());
            ps.setInt(2, c.getSpeciesId());
            ps.setInt(3, c.getLocationId());
            ps.setInt(4, c.getBaitId());

            if (c.getDateCaught() != null && !c.getDateCaught().isBlank()) {
                ps.setString(5, c.getDateCaught());   // e.g. "2025-11-17"
            } else {
                ps.setNull(5, Types.VARCHAR);
            }

            ps.setString(6, c.getNotes());

            ps.executeUpdate();

            // optional: grab generated id
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setCatchId(rs.getInt(1));
                }
            }
        }
    }

    // Simple "search" – find one catch by its ID (like findByUserName)
    public Optional<Catch> findById(Integer catchId) throws SQLException {
        String sql = """
            SELECT CatchID, UserID, SpeciesID, LocationID, BaitID, DateCaught, Notes
            FROM Catches
            WHERE CatchID = ?
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, catchId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Catch c = new Catch(
                            rs.getInt("CatchID"),
                            rs.getInt("UserID"),
                            rs.getInt("SpeciesID"),
                            rs.getInt("LocationID"),
                            rs.getInt("BaitID"),
                            rs.getString("DateCaught"),
                            rs.getString("Notes")
                    );
                    return Optional.of(c);
                }
                return Optional.empty();
            }
        }
    }

    // A slightly more "searchy" method: find all catches for a user
    public List<Catch> findByUserId(Integer userId) throws SQLException {
        String sql = """
            SELECT CatchID, UserID, SpeciesID, LocationID, BaitID, DateCaught, Notes
            FROM Catches
            WHERE UserID = ?
            ORDER BY DateCaught DESC, CatchID DESC
            """;

        List<Catch> results = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Catch c = new Catch(
                            rs.getInt("CatchID"),
                            rs.getInt("UserID"),
                            rs.getInt("SpeciesID"),
                            rs.getInt("LocationID"),
                            rs.getInt("BaitID"),
                            rs.getString("DateCaught"),
                            rs.getString("Notes")
                    );
                    results.add(c);
                }
            }
        }

        return results;
    }
}