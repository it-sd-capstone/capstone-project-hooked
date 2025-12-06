package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.LocationDao;
import edu.cvtc.hooked.model.Location;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "LocationServlet", urlPatterns = "/location")
public class LocationServlet extends HttpServlet {

    private final LocationDao locationDao = new LocationDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Optional: load one location to edit
        String editIdParam = req.getParameter("editId");
        if (editIdParam != null && !editIdParam.isBlank()) {
            try {
                int editId = Integer.parseInt(editIdParam);
                Optional<Location> locOpt = locationDao.findById(editId);
                locOpt.ifPresent(l -> req.setAttribute("locationToEdit", l));
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Unable to load location for editing.");
            }
        }

        // Sort parameter: asc/desc (default asc)
        String sortParam = req.getParameter("sort");
        String sortDir   = "desc".equalsIgnoreCase(sortParam) ? "desc" : "asc";

        try {
            List<Location> all = locationDao.findAllSorted("locationName", sortDir);
            req.setAttribute("locationList", all);
            req.setAttribute("sortOrder", sortDir);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to load location list.");
        }

        req.getRequestDispatcher("/WEB-INF/views/location.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Integer userId  = (session != null) ? (Integer) session.getAttribute("userId") : null;
        Boolean isAdmin = (session != null) ? (Boolean) session.getAttribute("isAdmin") : null;

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/Login");
            return;
        }
        if (isAdmin == null) {
            isAdmin = Boolean.FALSE;
        }

        String action = req.getParameter("action"); // "create", "update", "delete"
        if (action == null || action.isBlank()) {
            action = "create";
        }

        try {
            switch (action) {
                case "delete" -> {
                    if (!isAdmin) {
                        req.setAttribute("error", "You are not authorized to delete locations.");
                    } else {
                        int locationId = Integer.parseInt(req.getParameter("locationId"));
                        locationDao.deleteById(locationId);
                        req.setAttribute("success", "Location deleted successfully.");
                    }
                }
                case "update" -> {
                    if (!isAdmin) {
                        req.setAttribute("error", "You are not authorized to edit locations.");
                    } else {
                        int locationId = Integer.parseInt(req.getParameter("locationId"));
                        String locationName = req.getParameter("locationName");
                        String state        = req.getParameter("state");

                        if (locationName == null || locationName.isBlank()) {
                            req.setAttribute("error", "Location name cannot be empty.");
                        } else if (!locationName.trim().matches("^[A-Za-z\\-' ]{2,50}$")) {
                            req.setAttribute("error", "Location name must be 2–50 letters, spaces, dashes, or apostrophes.");
                        } else if (state == null || state.isBlank()) {
                            req.setAttribute("error", "State is required.");
                        } else if (!state.trim().matches("^[a-zA-Z]{2}$")) {
                            req.setAttribute("error", "State must be exactly 2 letters (e.g., WI).");
                        } else {
                            String trimmedName  = locationName.trim();
                            String trimmedState = state.trim().toUpperCase();

                            boolean takenByAnother = locationDao
                                    .findByNameAndState(trimmedName, trimmedState)
                                    .filter(l -> !l.getLocationId().equals(locationId))
                                    .isPresent();

                            if (takenByAnother) {
                                req.setAttribute("error",
                                        "That location (" + trimmedName + ", " + trimmedState + ") already exists.");
                            } else {
                                Location updated = new Location();
                                updated.setLocationId(locationId);
                                updated.setLocationName(trimmedName);
                                updated.setState(trimmedState);
                                updated.setCreatedByUserId(userId);

                                locationDao.update(updated);
                                req.setAttribute("success", "Location updated successfully.");
                            }
                        }
                    }
                }
                case "create" -> {
                    String locationName = req.getParameter("locationName");
                    String state        = req.getParameter("state");

                    if (locationName == null || locationName.isBlank()) {
                        req.setAttribute("error", "Location name is required.");
                    } else if (!locationName.trim().matches("^[A-Za-z\\-' ]{2,50}$")) {
                        req.setAttribute("error", "Location name must be 2–50 letters, spaces, dashes, or apostrophes.");
                    } else if (state == null || state.isBlank()) {
                        req.setAttribute("error", "State is required.");
                    } else if (!state.trim().matches("^[a-zA-Z]{2}$")) {
                        req.setAttribute("error", "State must be exactly 2 letters (e.g., WI).");
                    } else {
                        String trimmedName  = locationName.trim();
                        String trimmedState = state.trim().toUpperCase();

                        if (locationDao.exists(trimmedName, trimmedState)) {
                            req.setAttribute("error",
                                    "The location (" + trimmedName + ", " + trimmedState + ") already exists.");
                        } else {
                            Location location = new Location();
                            location.setLocationName(trimmedName);
                            location.setState(trimmedState);
                            location.setCreatedByUserId(userId);

                            locationDao.insert(location);
                            req.setAttribute("success",
                                    "Location (" + trimmedName + ", " + trimmedState + ") added successfully.");
                        }
                    }
                }
                default -> {
                    // no-op
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Database error: " + e.getMessage());
        }

        // Reload page
        doGet(req, resp);
    }
}
