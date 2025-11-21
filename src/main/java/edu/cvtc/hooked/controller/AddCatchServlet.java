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
import java.sql.SQLException;
import java.util.List;

@WebServlet("/addCatch")
public class AddCatchServlet extends HttpServlet {

    private final CatchDao catchDao = new CatchDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            req.setAttribute("error", "You must be logged in to add a catch.");
            req.getRequestDispatcher("/WEB-INF/views/addCatch.jsp").forward(req, resp);
            return;
        }

        // If redirected here with ?added=1, show success message
        String addedFlag = req.getParameter("added");
        if ("1".equals(addedFlag)) {
            req.setAttribute("success", "Your catch has been added!");
        }

        try {
            List<Catch> catches = catchDao.findByUserId(userId);
            req.setAttribute("catches", catches);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to load your catches.");
        }

        req.setAttribute("speciesList", SpeciesRestrictions.ALL.keySet());
        req.getRequestDispatcher("/WEB-INF/views/addCatch.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");

        if (userId == null) {
            req.setAttribute("error", "You must be logged in to add a catch.");
            forwardWithCatches(req, resp, null);
            return;
        }

        String speciesNameInput = req.getParameter("speciesName");
        String locationName     = req.getParameter("locationName");
        String baitType         = req.getParameter("baitType");
        String dateCaught       = req.getParameter("dateCaught");
        String notes            = req.getParameter("notes");
        String lengthStr        = req.getParameter("length");
        String weightStr        = req.getParameter("weight");

        double length = 0;
        double weight = 0;

        try {
            if (lengthStr != null && !lengthStr.isBlank()) {
                length = Double.parseDouble(lengthStr);
            }
            if (weightStr != null && !weightStr.isBlank()) {
                weight = Double.parseDouble(weightStr);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Length and weight must be numeric.");
            forwardWithCatches(req, resp, userId);
            return;
        }

        if (speciesNameInput != null) {
            speciesNameInput = speciesNameInput.trim();
        }
        String speciesKey = (speciesNameInput == null) ? null : speciesNameInput.toLowerCase();

        SpeciesRestrictions restrictions = (speciesKey == null)
                ? null
                : SpeciesRestrictions.ALL.get(speciesKey);

        if (restrictions == null) {
            req.setAttribute("error", "Unrecognized species.");
            forwardWithCatches(req, resp, userId);
            return;
        }

        if (length <= 0 || weight <= 0 ||
                length > restrictions.getMaxLength() ||
                weight > restrictions.getMaxWeight()) {

            req.setAttribute("error", "Invalid length/weight for species.");
            forwardWithCatches(req, resp, userId);
            return;
        }

        Catch c = new Catch(
                userId,
                speciesNameInput,   // store what user typed, e.g. "Bluegill"
                locationName,
                baitType,
                dateCaught,
                notes,
                length,
                weight
        );

        try {
            CatchDao dao = new CatchDao();
            dao.insert(c);
        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("error", "Failed to save catch: " + ex.getMessage());
            forwardWithCatches(req, resp, userId);
            return;
        }

        // ✅ SUCCESS PATH: redirect and STOP.
        resp.sendRedirect(req.getContextPath() + "/addCatch?added=1");
        return;  // <-- IMPORTANT
    }


//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//
//        // ALWAYS get userId from session
//        Integer userId = (Integer) req.getSession().getAttribute("userId");
//
//        if (userId == null) {
//            req.setAttribute("error", "You must be logged in to add a catch.");
//            req.getRequestDispatcher("/WEB-INF/views/addCatch.jsp").forward(req, resp);
//            return;
//        }
//
//        // read form fields
//        String speciesName     = req.getParameter("speciesName");
//        String locationName    = req.getParameter("locationName");
//        String baitType        = req.getParameter("baitType");
//        String dateCaught     = req.getParameter("dateCaught"); // e.g. "2025-11-17"
//        String notes          = req.getParameter("notes");
//        String lengthStr      = req.getParameter("length");
//        String weightStr      = req.getParameter("weight");
//
//        double length = 0;
//        double weight = 0;
//
//        try {
//            if (lengthStr != null && !lengthStr.isBlank()) {
//                length = Double.parseDouble(lengthStr);
//            }
//            if (weightStr != null && !weightStr.isBlank()) {
//                weight = Double.parseDouble(weightStr);
//            }
//        } catch (NumberFormatException e) {
//            req.setAttribute("error", "Length and weight must be numeric.");
//            req.getRequestDispatcher("/WEB-INF/views/addCatch.jsp").forward(req, resp);
//            return;
//        }
//
//        if (speciesName != null) {
//            speciesName = speciesName.trim().toLowerCase();
//        }
//
//        if (speciesNameInput != null) {
//            speciesNameInput = speciesNameInput.trim();
//        }
//        String speciesKey = (speciesNameInput == null) ? null : speciesNameInput.toLowerCase();
//
//        SpeciesRestrictions restrictions = (speciesKey == null)
//                ? null
//                : SpeciesRestrictions.ALL.get(speciesKey);
//
//        SpeciesRestrictions restrictions = SpeciesRestrictions.ALL.get(speciesName);
//
//        if (restrictions == null) {
//            req.setAttribute("error", "Unable to save catch: Unrecognized species");
//            req.getRequestDispatcher("/WEB-INF/views/addCatch.jsp").forward(req, resp);
//            return;
//        }
//
//        if (length <= 0 || weight <= 0 ||
//                length > restrictions.getMaxLength() ||
//                weight > restrictions.getMaxWeight()) {
//
//            req.setAttribute("error", "Invalid length/weight for species.");
//            req.getRequestDispatcher("/WEB-INF/views/addCatch.jsp").forward(req, resp);
//            return;
//        }
//
//        // Build catch object
//        Catch c = new Catch(
//                userId,
//                speciesName,
//                locationName,
//                baitType,
//                dateCaught,
//                notes,
//                length,
//                weight
//        );
//
//        try {
//            catchDao.insert(c);
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            req.setAttribute("error", "Failed to save catch: " + ex.getMessage());
//            forwardWithCatches(req, resp, userId);
//            return;
//        }
//
//        // SUCCESS: show message + refreshed table on same page.
//        resp.sendRedirect(req.getContextPath() + "/addCatch?added=1");
//        forwardWithCatches(req, resp, userId);
//    }

    private void forwardWithCatches(HttpServletRequest req, HttpServletResponse resp, Integer userId)
            throws ServletException, IOException {
        try {
            List<Catch> catches = catchDao.findByUserId(userId);
            req.setAttribute("catches", catches);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        req.setAttribute("speciesList", SpeciesRestrictions.ALL.keySet());
        req.getRequestDispatcher("/WEB-INF/views/addCatch.jsp").forward(req, resp);
    }
}
