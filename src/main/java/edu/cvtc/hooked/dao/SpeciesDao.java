package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.model.Species;
import edu.cvtc.hooked.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpeciesDao {

    public void insert(Species s) throws SQLException {
        String sql = "INSERT INTO Species(SpeciesName, Length, Weight) VALUES (?, ?, ?)";

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getSpeciesName());

            setNullableDouble(ps, 2, s.getLength());
            setNullableDouble(ps, 3, s.getWeight());

            ps.executeUpdate();
        }
    }

    public Optional<Species> findBySpeciesName(String speciesName) throws SQLException {
        String sql = "SELECT SpeciesID, SpeciesName, Length, Weight " +
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
        String sql = "SELECT SpeciesID, SpeciesName, Length, Weight " +
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
        String sql = "SELECT SpeciesID, SpeciesName, Length, Weight " +
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
                rs.getString("SpeciesName"),
                (Double) rs.getObject("Length"),
                (Double) rs.getObject("Weight")

        );
    }

    private void setNullableDouble(PreparedStatement ps, int index, Double value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.REAL);
        }  else {
            ps.setDouble(index, value);
        }
    }
}
