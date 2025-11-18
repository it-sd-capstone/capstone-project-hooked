package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.model.Catch;
import edu.cvtc.hooked.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CatchDao {

    // INSERT a new catch
    public void insert(Catch c) throws SQLException {
        String sql = """
            INSERT INTO Catches(UserID, SpeciesName, LocationName, BaitType, DateCaught, Notes, Length, Weight)
            VALUES(?,?,?,?,?,?,?,?)
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, c.getUserId());
            ps.setString(2, c.getSpeciesName());
            ps.setString(3, c.getLocationName());
            ps.setString(4, c.getBaitType());

            if (c.getDateCaught() != null && !c.getDateCaught().isBlank()) {
                ps.setString(5, c.getDateCaught());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }

            ps.setString(6, c.getNotes());
            ps.setDouble(7, c.getLength());
            ps.setDouble(8, c.getWeight());

            ps.executeUpdate();

            // Grab generated ID
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setCatchId(rs.getInt(1));
                }
            }
        }
    }

    // Find one catch by ID
    public Optional<Catch> findById(Integer catchId) throws SQLException {
        String sql = """
            SELECT CatchID, UserID, SpeciesName, LocationName, BaitType, DateCaught, Notes, Length, Weight
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
                            rs.getString("SpeciesName"),
                            rs.getString("LocationName"),
                            rs.getString("BaitType"),
                            rs.getString("DateCaught"),
                            rs.getString("Notes"),
                            rs.getDouble("Length"),
                            rs.getDouble("Weight")
                    );

                    c.setLength(rs.getDouble("Length"));
                    c.setWeight(rs.getDouble("Weight"));
                    return Optional.of(c);
                }
                return Optional.empty();
            }
        }
    }

    // Find all catches for a user
    public List<Catch> findByUserId(Integer userId) throws SQLException {
        String sql = """
            SELECT CatchID, UserID, SpeciesName, LocationName, BaitType, DateCaught, Notes, Length, Weight
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
                            rs.getString("SpeciesName"),
                            rs.getString("LocationName"),
                            rs.getString("BaitType"),
                            rs.getString("DateCaught"),
                            rs.getString("Notes"),
                            rs.getDouble("Length"),
                            rs.getDouble("Weight")
                    );
                    c.setLength(rs.getDouble("Length"));
                    c.setWeight(rs.getDouble("Weight"));
                    results.add(c);
                }
            }
        }

        return results;
    }
}
