package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.SpeciesDao;
import edu.cvtc.hooked.model.Species;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

// @WebServlet("/species")
public class SpeciesServlet extends HttpServlet {

    private final SpeciesDao speciesDao = new SpeciesDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            List<Species> all = speciesDao.findAll();
            req.setAttribute("speciesList", all);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to load species list.");
        }

        req.getRequestDispatcher("/WEB-INF/views/species.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/Login");
            return;
        }

        String name = req.getParameter("addSpecies");
        String maxLenStr = req.getParameter("maxLength");
        String maxWtStr  = req.getParameter("maxWeight");

        if (name == null || name.isBlank() ||
                maxLenStr == null || maxLenStr.isBlank() ||
                maxWtStr == null || maxWtStr.isBlank()) {

            req.setAttribute("error", "Species name, max length, and max weight are required.");
            doGet(req, resp);
            return;
        }

        double maxLen;
        double maxWt;
        try {
            maxLen = Double.parseDouble(maxLenStr);
            maxWt  = Double.parseDouble(maxWtStr);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Max length and max weight must be numeric.");
            doGet(req, resp);
            return;
        }

        Species s = new Species(name.trim().toLowerCase(), maxLen, maxWt, userId);

        try {
            if (speciesDao.exists(s.getSpeciesName())) {
                req.setAttribute("error", "That species already exists.");
                doGet(req, resp);
                return;
            }

            speciesDao.insert(s);
            req.setAttribute("success", "Species added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to save species: " + e.getMessage());
        }

        doGet(req, resp);
    }
}
