package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.SpeciesDao;
import edu.cvtc.hooked.model.Species;
import edu.cvtc.hooked.util.DbUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Arrays;
import java.util.stream.Collectors;

// Map every route you want to support
@WebServlet(urlPatterns = {
        "/index",
        "/locations", "/logout", "/add", "/search", "/statistics", "/passwordReset", "/species"
})
public class FrontController extends HttpServlet {

    private String viewFor(String path) {
        // Normalize "/index/" to "/index"
        if (path == null || path.equals("/")) return "/WEB-INF/views/index.jsp";

        return switch (path) {
            case "/index"     -> "/WEB-INF/views/index.jsp";
            case "/locations" -> "/WEB-INF/views/location.jsp";
            case "/species"   -> "/WEB-INF/views/species.jsp";
            case "/bait"      -> "/WEB-INF/views/bait.jsp";
            case "/search" -> "/WEB-INF/views/search.jsp";
            case "/add"   -> "/WEB-INF/views/addCatch.jsp";
            case "/statistics"      -> "/WEB-INF/views/statistics.jsp";
            case "/logout"    -> "/WEB-INF/views/logout.jsp"; // or do logout logic first
            case "/passwordReset" -> "/WEB-INF/views/passwordReset.jsp";
            default           -> "/WEB-INF/views/error.jsp";  // optional fallback page
        };
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        // Load statistics data if requesting statistics page
        if ("/statistics".equals(path)) {
            loadStatistics(req);
        }

        if ("/species".equals(path)) {
            handleSpeciesAdminActions(req);
            loadSpecies(req);
        }

        req.getRequestDispatcher(viewFor(path)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        if ("/species".equals(path)) {
            handleSpeciesPost(req, resp);
            return;
        }
        doGet(req, resp);
    }

    private void handleSpeciesPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
        Boolean isAdmin = (session != null) ? (Boolean) session.getAttribute("isAdmin") : null;

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/Login");
            return;
        }

        String idStr      = req.getParameter("speciesId");  // hidden when editing
        boolean isUpdate  = idStr != null && !idStr.isBlank();

        // Only admins can update existing species
        if (isUpdate && (isAdmin == null || !isAdmin)) {
            resp.sendRedirect(req.getContextPath() + "/species?error=" +
                    url("Only admins can edit species.") + "#speciesTable");
            return;
        }

        String species   = req.getParameter("addSpecies");
        String maxLenStr = req.getParameter("maxLength");
        String maxWtStr  = req.getParameter("maxWeight");

        if (species == null || species.isBlank()
                || maxLenStr == null || maxLenStr.isBlank()
                || maxWtStr == null || maxWtStr.isBlank()) {

            resp.sendRedirect(req.getContextPath() + "/species?error=" +
                    url("Species name, max length, and max weight are required.") +
                    "#speciesTable");
            return;
        }

        String normalized = species.trim().replaceAll("\\s+", " ");
        if (!normalized.matches("^[A-Za-z\\-' ]{2,50}$")) {
            resp.sendRedirect(req.getContextPath() + "/species?error=" +
                    url("Species name must be 2–50 letters, spaces, dashes or apostrophes.") +
                    "#speciesTable");
            return;
        }

        String formatted = Arrays.stream(normalized.toLowerCase().split(" "))
                .map(word -> word.substring(0,1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));

        double maxLen;
        double maxWt;
        try {
            maxLen = Double.parseDouble(maxLenStr);
            maxWt  = Double.parseDouble(maxWtStr);

            if (maxLen <= 0 || maxWt <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/species?error=" +
                    url("Max length and weight must be positive numbers.") + "#speciesTable");
            return;
        }

        SpeciesDao dao = new SpeciesDao();

        try {
            if (isUpdate) {
                // EDIT existing
                int id = Integer.parseInt(idStr);
                Species s = new Species(id, formatted, maxLen, maxWt, null);
                dao.update(s);
                resp.sendRedirect(req.getContextPath() + "/species?success=" +
                        url("Species updated successfully.") + "#speciesTable");

            } else {
                // ADD new
                if (dao.exists(formatted)) {
                    resp.sendRedirect(req.getContextPath() + "/species?error=" +
                            url("That species already exists.") + "#speciesTable");
                    return;
                }

                Species s = new Species(formatted, maxLen, maxWt, userId);
                dao.insert(s);
                resp.sendRedirect(req.getContextPath() + "/species?success=" +
                        url("Species added successfully.") + "#speciesTable");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/species?error=" +
                    url("Failed to save species.") + "#speciesTable");
        }
    }


    private void loadStatistics(HttpServletRequest req) {
        try (Connection conn = DbUtil.getConnection()) {

            // Heaviest fish
            String heaviestQuery = """
                SELECT SpeciesName, Weight, LocationName, DateCaught
                FROM Catches
                WHERE Weight IS NOT NULL
                ORDER BY Weight DESC
                LIMIT 1
            """;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(heaviestQuery)) {
                if (rs.next()) {
                    req.setAttribute("heaviestFish", rs.getString("SpeciesName"));
                    req.setAttribute("heaviestWeight", rs.getDouble("Weight"));
                    req.setAttribute("heaviestLocation", rs.getString("LocationName"));
                    req.setAttribute("heaviestDate", rs.getString("DateCaught"));
                }
            }

            // Longest fish
            String longestQuery = """
                SELECT SpeciesName, Length, LocationName, DateCaught
                FROM Catches
                WHERE Length IS NOT NULL
                ORDER BY Length DESC
                LIMIT 1
            """;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(longestQuery)) {
                if (rs.next()) {
                    req.setAttribute("longestFish", rs.getString("SpeciesName"));
                    req.setAttribute("longestLength", rs.getDouble("Length"));
                    req.setAttribute("longestLocation", rs.getString("LocationName"));
                    req.setAttribute("longestDate", rs.getString("DateCaught"));
                }
            }

            // Most productive location
            String locationQuery = """
                SELECT LocationName, COUNT(*) as CatchCount
                FROM Catches
                GROUP BY LocationName
                ORDER BY CatchCount DESC
                LIMIT 1
            """;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(locationQuery)) {
                if (rs.next()) {
                    req.setAttribute("topLocation", rs.getString("LocationName"));
                    req.setAttribute("locationCount", rs.getInt("CatchCount"));
                }
            }

            // Most productive bait
            String baitQuery = """
                SELECT BaitType, COUNT(*) as CatchCount
                FROM Catches
                GROUP BY BaitType
                ORDER BY CatchCount DESC
                LIMIT 1
            """;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(baitQuery)) {
                if (rs.next()) {
                    req.setAttribute("topBait", rs.getString("BaitType"));
                    req.setAttribute("baitCount", rs.getInt("CatchCount"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to load statistics: " + e.getMessage());
        }
    }

    private void loadSpecies(HttpServletRequest req) {
        try {
            String sort = req.getParameter("sort");
            String dir  = req.getParameter("dir");

            if (sort == null || sort.isBlank()) {
                sort = "species";   // default: alphabetical
            }
            if (dir == null || dir.isBlank()) {
                dir = "asc";
            }

            SpeciesDao dao = new SpeciesDao();
            req.setAttribute("speciesList", dao.findAllSorted(sort, dir));

            // so JSP can know current sort if needed
            req.setAttribute("currentSort", sort);
            req.setAttribute("currentDir", dir);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("speciesError", "Unable to load species.");
        }
    }


    private String capitalizeWords(String input) {
        String[] parts = input.split(" ");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String w = parts[i];
            if (!w.isEmpty()) {
                sb.append(Character.toUpperCase(w.charAt(0)))
                        .append(w.substring(1).toLowerCase());
            }
            if (i < parts.length - 1) sb.append(" ");
        }
        return sb.toString();
    }

    private String url(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private void handleSpeciesAdminActions(HttpServletRequest req) {
        Boolean isAdmin = (Boolean) req.getSession().getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            return; // non-admins can't edit/delete
        }

        String deleteId = req.getParameter("deleteId");
        String editId   = req.getParameter("editId");

        SpeciesDao dao = new SpeciesDao();

        // DELETE
        if (deleteId != null && !deleteId.isBlank()) {
            try {
                int id = Integer.parseInt(deleteId);
                dao.deleteById(id);
                req.setAttribute("success", "Species deleted.");
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Unable to delete species.");
            }
        }

        // EDIT (load species into request so JSP can prefill form)
        if (editId != null && !editId.isBlank()) {
            try {
                int id = Integer.parseInt(editId);
                dao.findById(id).ifPresent(s -> req.setAttribute("editSpecies", s));
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Unable to load species for editing.");
            }
        }
    }


}