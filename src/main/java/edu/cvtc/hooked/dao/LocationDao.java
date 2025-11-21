package edu.cvtc.hooked.dao;

import edu.cvtc.hooked.model.Location;
import edu.cvtc.hooked.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationDao {

    // INSERT a new location
    public void insert(Location l) throws SQLException {
        String sql = "INSERT INTO Location(LocationName, State) VALUES(?, ?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, l.getLocationName());
            ps.setString(2, l.getState());

            ps.executeUpdate();

            // assign generated ID back into the model
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    l.setLocationId(rs.getInt(1));
                }
            }
        }
    }

    // Find by ID
    public Optional<Location> findById(int id) throws SQLException {
        String sql = "SELECT LocationID, LocationName, State FROM Location WHERE LocationID = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Location loc = new Location(
                            rs.getInt("LocationID"),
                            rs.getString("LocationName"),
                            rs.getString("State")
                    );
                    return Optional.of(loc);
                }
            }
        }

        return Optional.empty();
    }

    // Find by name
    public Optional<Location> findByName(String name) throws SQLException {
        String sql = "SELECT LocationID, LocationName, State FROM Location WHERE LocationName = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Location loc = new Location(
                            rs.getInt("LocationID"),
                            rs.getString("LocationName"),
                            rs.getString("State")
                    );
                    return Optional.of(loc);
                }
            }
        }

        return Optional.empty();
    }

    // Return all locations (for dropdowns, admin pages, etc.)
    public List<Location> findAll() throws SQLException {
        String sql = "SELECT LocationID, LocationName, State FROM Location ORDER BY LocationName";

        List<Location> list = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Location(
                        rs.getInt("LocationID"),
                        rs.getString("LocationName"),
                        rs.getString("State")
                ));
            }
        }

        return list;
    }
}
