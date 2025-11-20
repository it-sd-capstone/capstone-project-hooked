package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.CatchDao;
import edu.cvtc.hooked.model.Catch;

import edu.cvtc.hooked.model.SpeciesRestrictions;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static java.lang.System.out;

@WebServlet("/addCatch")
public class AddCatchServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // read form fields
        String userIdStr      = req.getParameter("userId");
        String speciesStr     = req.getParameter("speciesName");
        String locationStr    = req.getParameter("locationName");
        String baitStr        = req.getParameter("baitType");
        String dateCaught     = req.getParameter("dateCaught"); // e.g. "2025-11-17"
        String notes          = req.getParameter("notes");
        String lengthStr      = req.getParameter("length");
        String weightStr      = req.getParameter("weight");

        try {
            Integer userId       = (userIdStr != null && !userIdStr.isBlank()) ? Integer.valueOf(userIdStr) : null;
            String speciesName  = (speciesStr != null && !speciesStr.isBlank()) ? req.getParameter("speciesName") : null;
            String locationName = (locationStr != null && !locationStr.isBlank()) ? req.getParameter("locationName") : null;
            String baitType     = (baitStr != null && !baitStr.isBlank()) ? req.getParameter("baitType") : null;

            double length = (lengthStr != null && !lengthStr.isBlank()) ? Double.parseDouble(lengthStr) : 0.0;
            double weight = (weightStr != null && !weightStr.isBlank()) ? Double.parseDouble(weightStr) : 0.0;

            Catch c = new Catch(userId, speciesName, locationName, baitType, dateCaught, notes, length, weight);

            SpeciesRestrictions restrictions = SpeciesRestrictions.ALL.get(speciesStr);

            if (restrictions == null) {
                req.setAttribute("error", "Unable to save catch: Unrecognized species");
                req.getRequestDispatcher("/WEB-INF/views/addCatch.jsp").forward(req, resp);
            }
            if (restrictions.getMaxLength() < length || restrictions.getMaxWeight() < weight || length <= 0 || weight <= 0) {
                req.setAttribute("error", "Unable to save catch: Invalid size");
                req.getRequestDispatcher("/WEB-INF/views/addCatch.jsp").forward(req, resp);
            }

            CatchDao dao = new CatchDao();
            dao.insert(c);

            resp.sendRedirect(req.getContextPath() + "/WEB-INF/views/successfulAdd.jsp");

        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("error", "Unable to save catch: " + ex.getMessage());
            // go back to the add form
            req.getRequestDispatcher("/WEB-INF/views/addCatch.jsp").forward(req, resp);
        }

    }
}
