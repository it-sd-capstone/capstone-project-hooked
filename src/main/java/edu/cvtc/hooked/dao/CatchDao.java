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

    public List<Catch> searchOutput(
            Integer userId,
            List<String> speciesList,
            String location,
            String bait) throws SQLException {

        // default: newest first
        return searchOutput(userId, speciesList, location, bait, null, null);
    }

    public List<Catch> searchOutput(
            Integer userId,
            List<String> speciesList,
            String location,
            String bait,
            String sortField,
            String sortDir) throws SQLException {

        StringBuilder sb = new StringBuilder(
                "SELECT CatchID, UserID, SpeciesName, LocationName, BaitType, DateCaught, Notes, Length, Weight " +
                        "FROM Catches WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if (userId != null) {
            sb.append(" AND UserID = ?");
            params.add(userId);
        }

        // 🔍 fuzzy + multi-species ("trout", "bluegills", "walleye, bluegill")
        if (speciesList != null && !speciesList.isEmpty()) {
            sb.append(" AND (");
            for (int i = 0; i < speciesList.size(); i++) {
                if (i > 0) sb.append(" OR ");

                String term = speciesList.get(i).toLowerCase();

                sb.append("(");
                sb.append("LOWER(SpeciesName) LIKE ?");                  // e.g. 'lake trout' LIKE '%trout%'
                sb.append(" OR ? LIKE '%' || LOWER(SpeciesName) || '%'"); // e.g. 'bluegills' LIKE '%bluegill%'
                sb.append(")");

                params.add("%" + term + "%");
                params.add(term);
            }
            sb.append(")");
        }

        if (location != null && !location.isBlank()) {
            sb.append(" AND LOWER(LocationName) LIKE ?");
            params.add("%" + location.toLowerCase() + "%");
        }

        if (bait != null && !bait.isBlank()) {
            sb.append(" AND LOWER(BaitType) LIKE ?");
            params.add("%" + bait.toLowerCase() + "%");
        }

        // 🧮 Choose ORDER BY column (same logic you had in searchAll)
        String orderCol;
        if ("species".equalsIgnoreCase(sortField)) {
            orderCol = "SpeciesName";
        } else if ("length".equalsIgnoreCase(sortField)) {
            orderCol = "Length";
        } else if ("weight".equalsIgnoreCase(sortField)) {
            orderCol = "Weight";
        } else if ("location".equalsIgnoreCase(sortField)) {
            orderCol = "LocationName";
        } else if ("date".equalsIgnoreCase(sortField)) {
            orderCol = "DateCaught";
        } else if ("bait".equalsIgnoreCase(sortField)) {
            orderCol = "BaitType";
        } else {
            // default sort: newest first
            orderCol = "DateCaught";
            sortDir = "desc";
        }

        String direction = "desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";

        sb.append(" ORDER BY ").append(orderCol).append(" ").append(direction);

        List<Catch> results = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

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
                    results.add(c);
                }
            }
        }

        return results;
    }

    // Convenience overload for a single species
    public List<Catch> searchOutput(
            Integer userId,
            String species,
            String location,
            String bait
    ) throws SQLException {

        List<String> speciesList = null;
        if (species != null && !species.isBlank()) {
            speciesList = java.util.Collections.singletonList(species);
        }

        return searchOutput(userId, speciesList, location, bait);
    }


    // UPDATE an existing catch
    public void update(Catch c) throws SQLException {
        if (c.getCatchId() == null) {
            throw new IllegalArgumentException("Catch ID is required for update");
        }

        String sql = """
        UPDATE Catches
        SET SpeciesName = ?,
            LocationName = ?,
            BaitType     = ?,
            DateCaught   = ?,
            Notes        = ?,
            Length       = ?,
            Weight       = ?
        WHERE CatchID = ? AND UserID = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getSpeciesName());
            ps.setString(2, c.getLocationName());
            ps.setString(3, c.getBaitType());
            ps.setString(4, c.getDateCaught());
            ps.setString(5, c.getNotes());
            ps.setDouble(6, c.getLength());
            ps.setDouble(7, c.getWeight());
            ps.setInt(8, c.getCatchId());
            ps.setInt(9, c.getUserId());

            ps.executeUpdate();
        }
    }

    // DELETE a catch, ensuring it belongs to the user
    public void deleteForUser(int catchId, int userId) throws SQLException {
        String sql = "DELETE FROM Catches WHERE CatchID = ? AND UserID = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, catchId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    // All catches in the system (for admin view)
    public List<Catch> findAll() throws SQLException {
        String sql = """
        SELECT CatchID, UserID, SpeciesName, LocationName, BaitType,
               DateCaught, Notes, Length, Weight
        FROM Catches
        ORDER BY DateCaught DESC, CatchID DESC
        """;

        List<Catch> results = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
                results.add(c);
            }
        }

        return results;
    }

    // Admin: delete a single catch by ID (no user restriction)
    public void deleteById(int catchId) throws SQLException {
        String sql = "DELETE FROM Catches WHERE CatchID = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, catchId);
            ps.executeUpdate();
        }
    }

    // Admin: clear the whole Catches table
    public void deleteAll() throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Catches")) {
            ps.executeUpdate();
        }
    }

    // Admin-only: update by CatchID only
    public void updateAsAdmin(Catch c) throws SQLException {
        if (c.getCatchId() == null) {
            throw new IllegalArgumentException("Catch ID is required for update");
        }

        String sql = """
        UPDATE Catches
        SET SpeciesName = ?,
            LocationName = ?,
            BaitType     = ?,
            DateCaught   = ?,
            Notes        = ?,
            Length       = ?,
            Weight       = ?,
            UserID       = ?
        WHERE CatchID = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getSpeciesName());
            ps.setString(2, c.getLocationName());
            ps.setString(3, c.getBaitType());
            ps.setString(4, c.getDateCaught());
            ps.setString(5, c.getNotes());
            ps.setDouble(6, c.getLength());
            ps.setDouble(7, c.getWeight());
            ps.setInt(8, c.getUserId());      // keep/restore owner
            ps.setInt(9, c.getCatchId());

            ps.executeUpdate();
        }
    }

}
