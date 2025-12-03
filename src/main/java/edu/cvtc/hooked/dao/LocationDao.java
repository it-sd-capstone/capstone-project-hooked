package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.model.Location;
import edu.cvtc.hooked.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationDao {

    // ---------- Row mapper ----------
    private Location mapRow(ResultSet rs) throws SQLException {
        Location l = new Location();
        l.setLocationId(rs.getInt("LocationID"));
        l.setLocationName(rs.getString("LocationName"));
        l.setState(rs.getString("State"));
        l.setCreatedByUserId((Integer) rs.getObject("CreatedByUserID"));
        return l;
    }

    // ---------- Basic find-all / sorted ----------

    public List<Location> findAll() throws SQLException {
        return findAllSorted("locationName", "asc");
    }

    public List<Location> findAllSorted(String sortField, String sortDir) throws SQLException {
        String orderCol;

        if ("locationName".equalsIgnoreCase(sortField)) {
            orderCol = "LocationName";
        } else {
            orderCol = "LocationName";
        }

        String direction = "desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";

        String sql = """
            SELECT LocationID, LocationName, State, CreatedByUserID
            FROM Location
            ORDER BY %s %s
        """.formatted(orderCol, direction);

        List<Location> list = new ArrayList<>();

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

    public Optional<Location> findById(int id) throws SQLException {
        String sql = """
            SELECT LocationID, LocationName, State, CreatedByUserID
            FROM Location
            WHERE LocationID = ?
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

    public Optional<Location> findByNameAndState(String locationName, String state) throws SQLException {
        String sql = """
            SELECT LocationID, LocationName, State, CreatedByUserID
            FROM Location
            WHERE LOWER(LocationName) = LOWER(?)
              AND UPPER(State) = UPPER(?)
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, locationName);
            ps.setString(2, state);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public boolean exists(String locationName, String state) throws SQLException {
        return findByNameAndState(locationName, state).isPresent();
    }

    // ---------- Insert / update / delete ----------

    public void insert(Location location) throws SQLException {
        String sql = """
            INSERT INTO Location (LocationName, State, CreatedByUserID)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, location.getLocationName());
            ps.setString(2, location.getState());

            if (location.getCreatedByUserId() != null) {
                ps.setInt(3, location.getCreatedByUserId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    location.setLocationId(rs.getInt(1));
                }
            }
        }
    }

    public void update(Location location) throws SQLException {
        String sql = """
            UPDATE Location
            SET LocationName = ?, State = ?
            WHERE LocationID = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, location.getLocationName());
            ps.setString(2, location.getState());
            ps.setInt(3, location.getLocationId());

            ps.executeUpdate();
        }
    }

    public void deleteById(int locationId) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Location WHERE LocationID = ?")) {

            ps.setInt(1, locationId);
            ps.executeUpdate();
        }
    }
}
