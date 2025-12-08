package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.model.Species;
import edu.cvtc.hooked.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpeciesDao {

    public List<Species> findAll() throws SQLException {
        // Default: alphabetical by species name ASC
        return findAllSorted("species", "asc");
    }

    public List<Species> findAllSorted(String sortField, String sortDir) throws SQLException {
        String orderCol;

        if ("length".equalsIgnoreCase(sortField)) {
            orderCol = "MaxLength";
        } else if ("weight".equalsIgnoreCase(sortField)) {
            orderCol = "MaxWeight";
        } else {
            // default / "species"
            orderCol = "SpeciesName COLLATE NOCASE";
        }

        String direction = "desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";

        String sql = """
            SELECT SpeciesID, SpeciesName, MaxLength, MaxWeight, CreatedByUserID
            FROM Species
            ORDER BY %s %s
        """.formatted(orderCol, direction);

        List<Species> list = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Optional<Species> findByName(String name) throws SQLException {
        String sql = """
            SELECT SpeciesID, SpeciesName, MaxLength, MaxWeight, CreatedByUserID
            FROM Species
            WHERE LOWER(SpeciesName) = LOWER(?)
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public boolean exists(String name) throws SQLException {
        return findByName(name).isPresent();
    }

    public void insert(Species s) throws SQLException {

        if (exists(s.getSpeciesName())) {
            throw new IllegalArgumentException("Species already exists with that name.");
        }

        String sql = """
            INSERT INTO Species(SpeciesName, MaxLength, MaxWeight, CreatedByUserID)
            VALUES(?,?,?,?)
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getSpeciesName());
            ps.setDouble(2, s.getMaxLength());
            ps.setDouble(3, s.getMaxWeight());

            if (s.getCreatedByUserId() != null) {
                ps.setInt(4, s.getCreatedByUserId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    s.setSpeciesId(rs.getInt(1));
                }
            }
        }
    }

    public void update(Species s) throws SQLException {
        String sql = """
            UPDATE Species
            SET SpeciesName = ?, MaxLength = ?, MaxWeight = ?
            WHERE SpeciesID = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getSpeciesName());
            ps.setDouble(2, s.getMaxLength());
            ps.setDouble(3, s.getMaxWeight());
            ps.setInt(4, s.getSpeciesId());

            ps.executeUpdate();
        }
    }

    public void deleteById(int speciesId) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Species WHERE SpeciesID = ?")) {
            ps.setInt(1, speciesId);
            ps.executeUpdate();
        }
    }

    private Species mapRow(ResultSet rs) throws SQLException {
        return new Species(
                rs.getInt("SpeciesID"),
                rs.getString("SpeciesName"),
                rs.getDouble("MaxLength"),
                rs.getDouble("MaxWeight"),
                (Integer) rs.getObject("CreatedByUserID")
        );
    }

    public Optional<Species> findById(int id) throws SQLException {
        String sql = """
        SELECT SpeciesID, SpeciesName, MaxLength, MaxWeight, CreatedByUserID
        FROM Species
        WHERE SpeciesID = ?
    """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

}
