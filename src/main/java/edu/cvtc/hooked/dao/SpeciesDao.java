package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.model.Species;
import edu.cvtc.hooked.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpeciesDao {

    public void insert(Species s) throws SQLException {
        String sql = "INSERT INTO Species(SpeciesName) VALUES (?)";

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getSpeciesName());

            ps.executeUpdate();
        }
    }

    public Optional<Species> findBySpeciesName(String speciesName) throws SQLException {
        String sql = "SELECT SpeciesID, SpeciesName " +
                "FROM Species WHERE SpeciesName = ?";

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, speciesName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Species s = mapRow(rs);
                    return Optional.of(s);
                }
                return Optional.empty();
            }
        }
    }

    public Optional<Species> findById(int speciesId) throws SQLException {
        String sql = "SELECT SpeciesID, SpeciesName " +
                "FROM Species WHERE SpeciesID = ?";

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, speciesId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Species s = mapRow(rs);
                    return Optional.of(s);
                }
                return Optional.empty();
            }
        }
    }

    public List<Species> findAll() throws SQLException {
        String sql = "SELECT SpeciesID, SpeciesName " +
                "FROM Species ORDER BY SpeciesName";

        List<Species> result = new ArrayList<>();

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }

        return result;
    }

    private Species mapRow(ResultSet rs) throws SQLException {
        return new Species(
                rs.getInt("SpeciesID"),
                rs.getString("SpeciesName")
        );
    }

    public List<Species> searchByTerm(String name) {
        List<Species> list = new ArrayList<>();

        String sql = "SELECT SpeciesID, SpeciesName FROM Species WHERE SpeciesName = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Species s = new Species(
                            rs.getInt("SpeciesID"),
                            rs.getString("SpeciesName")
                    );
                    list.add(s);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean exists(String speciesName) {
        String sql = "SELECT 1 FROM Species WHERE SpeciesName = ? LIMIT 1";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, speciesName);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true if species exists
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
