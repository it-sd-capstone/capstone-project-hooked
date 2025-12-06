package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.model.Bait;
import edu.cvtc.hooked.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaitDao {

    // ---------- Row mapper (like SpeciesDao.mapRow) ----------
    private Bait mapRow(ResultSet rs) throws SQLException {
        Bait b = new Bait();
        b.setId(rs.getInt("BaitID"));
        b.setName(rs.getString("Name"));
        b.setNotes(rs.getString("Notes"));
        b.setCreatedByUserId((Integer) rs.getObject("CreatedByUserID")); // nullable
        return b;
    }

    // ---------- Basic find-all / sorted ----------

    // Default: alphabetical by bait name ASC
    public List<Bait> findAll() throws SQLException {
        return findAllSorted("name", "asc");
    }

    // Similar to SpeciesDao.findAllSorted
    public List<Bait> findAllSorted(String sortField, String sortDir) throws SQLException {
        String orderCol;

        // For now we really only sort by "name", but this mirrors the structure of SpeciesDao.
        if ("name".equalsIgnoreCase(sortField)) {
            orderCol = "Name";
        } else {
            // default
            orderCol = "Name";
        }

        String direction = "desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";

        String sql = """
            SELECT BaitID, Name, Notes, CreatedByUserID
            FROM Bait
            ORDER BY %s %s
        """.formatted(orderCol, direction);

        List<Bait> list = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // ---------- Lookups / existence checks ----------

    public Optional<Bait> findById(int id) throws SQLException {
        String sql = """
            SELECT BaitID, Name, Notes, CreatedByUserID
            FROM Bait
            WHERE BaitID = ?
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

    public Optional<Bait> findByName(String name) throws SQLException {
        String sql = """
            SELECT BaitID, Name, Notes, CreatedByUserID
            FROM Bait
            WHERE LOWER(Name) = LOWER(?)
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

    // ---------- Insert / update / delete ----------

    public void insert(Bait bait) throws SQLException {
        String sql = """
            INSERT INTO Bait (Name, Notes, CreatedByUserID)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, bait.getName());
            ps.setString(2, bait.getNotes());

            if (bait.getCreatedByUserId() != null) {
                ps.setInt(3, bait.getCreatedByUserId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    bait.setId(rs.getInt(1));
                }
            }
        }
    }

    public void update(Bait bait) throws SQLException {
        String sql = """
            UPDATE Bait
            SET Name = ?, Notes = ?
            WHERE BaitID = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bait.getName());
            ps.setString(2, bait.getNotes());
            ps.setInt(3, bait.getId());

            ps.executeUpdate();
        }
    }

    public void deleteById(int baitId) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Bait WHERE BaitID = ?")) {
            ps.setInt(1, baitId);
            ps.executeUpdate();
        }
    }

    // ---------- Search (for your bait page search box) ----------

    public List<Bait> searchByTerm(String term) throws SQLException {
        String sql = """
            SELECT BaitID, Name, Notes, CreatedByUserID
            FROM Bait
            WHERE LOWER(Name)  LIKE ?
               OR LOWER(Notes) LIKE ?
            ORDER BY Name ASC
        """;

        List<Bait> result = new ArrayList<>();
        String pattern = "%" + (term == null ? "" : term.toLowerCase()) + "%";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        }

        return result;
    }
}
