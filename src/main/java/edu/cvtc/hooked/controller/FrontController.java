package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.SpeciesDao;
import edu.cvtc.hooked.model.Species;
import edu.cvtc.hooked.util.DbUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

// Map every route you want to support
@WebServlet(urlPatterns = {
        "/index",
        "/about", "/locations", "/species", "/logout", "/add", "/search", "/statistics"
})
public class FrontController extends HttpServlet {

    private String viewFor(String path) {
        // Normalize "/index/" to "/index"
        if (path == null || path.equals("/")) return "/WEB-INF/views/index.jsp";

        return switch (path) {
            case "/index"     -> "/WEB-INF/views/index.jsp";
            case "/about"     -> "/WEB-INF/views/about.jsp";
            case "/locations" -> "/WEB-INF/views/locations.jsp";
            case "/species"   -> "/WEB-INF/views/species.jsp";
            case "/bait"      -> "/WEB-INF/views/bait.jsp";
            case "/search" -> "/WEB-INF/views/search.jsp";
            case "/add"   -> "/WEB-INF/views/addCatch.jsp";
            case "/statistics"      -> "/WEB-INF/views/statistics.jsp";
            case "/logout"    -> "/WEB-INF/views/logout.jsp"; // or do logout logic first
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

        if (req.getSession(false) == null || req.getSession(false).getAttribute("userId") == null) {
            resp.sendRedirect("species?error=You must be logged in to add species.");
            return;
        }

        String species = req.getParameter("addSpecies");

        if (species == null || species.trim().isEmpty()) {
            resp.sendRedirect("species?error=Empty species name");
            return;
        }

        species = species.trim().replaceAll("\\s+", " ");

        String formatted = capitalizeWords(species);

        if (!formatted.equals(species)) {
            resp.sendRedirect("species?formatted="
                    + java.net.URLEncoder.encode(formatted, "UTF-8"));
            return;
        }


        if (!species.matches("^[A-Za-z][A-Za-z\\s-]*[A-Za-z]$")) {
            resp.sendRedirect("species?error=Invalid species name");
            return;
        }

        SpeciesDao dao = new SpeciesDao();
        if (dao.exists(species)) {
            resp.sendRedirect("species?error=Species already exists");
            return;
        }

        try {
            Species s = new Species(
                    species.trim()
            );

            dao.insert(s);

            resp.sendRedirect("species?success=1");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("species?error=1");
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
            SpeciesDao dao = new SpeciesDao();
            req.setAttribute("speciesList", dao.findAll());
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

}