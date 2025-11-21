package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.model.Bait;
import edu.cvtc.hooked.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaitDao {

    private static final String SELECT_ALL =
            "SELECT BaitID, Name, Notes FROM bait ORDER BY Name";

    private static final String INSERT_SQL =
            "INSERT INTO bait (Name, Notes) VALUES (?, ?)";

    public List<Bait> findAll() {
        List<Bait> result = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bait bait = new Bait();
                bait.setId(rs.getInt("BaitID"));
                bait.setName(rs.getString("Name"));
                bait.setNotes(rs.getString("Notes"));
                result.add(bait);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading bait list", e);
        }

        return result;
    }

    public void insert(Bait bait) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setString(1, bait.getName());
            ps.setString(2, bait.getNotes());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting bait", e);
        }
    }

    private static final String SELECT_BY_ID =
            "SELECT BaitID, Name, Notes FROM Bait WHERE BaitID = ?";

    public Bait findById(int id) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Bait bait = new Bait();
                    bait.setId(rs.getInt("BaitID"));
                    bait.setName(rs.getString("Name"));
                    bait.setNotes(rs.getString("Notes"));
                    return bait;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading bait by id", e);
        }
    }

    private static final String SEARCH_BY_TERM = """
    SELECT BaitID, Name, Notes
    FROM Bait
    WHERE Name  LIKE ?
       OR Notes LIKE ?
    ORDER BY Name
    """;

    public List<Bait> searchByTerm(String term) {
        List<Bait> result = new ArrayList<>();

        String pattern = "%" + term + "%";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_BY_TERM)) {

            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bait bait = new Bait();
                    bait.setId(rs.getInt("BaitID"));
                    bait.setName(rs.getString("Name"));
                    bait.setNotes(rs.getString("Notes"));
                    result.add(bait);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error searching bait", e);
        }

        return result;
    }


}
