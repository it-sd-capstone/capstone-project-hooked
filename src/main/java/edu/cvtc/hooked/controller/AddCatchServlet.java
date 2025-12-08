package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.BaitDao;
import edu.cvtc.hooked.dao.CatchDao;
import edu.cvtc.hooked.dao.SpeciesDao;
import edu.cvtc.hooked.dao.LocationDao;   // <-- NEW
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

        Boolean isAdmin = (Boolean) req.getSession().getAttribute("isAdmin");

        // handle deleteId for normal user deletes ---
        String deleteIdStr = req.getParameter("deleteId");
        if (deleteIdStr != null && !deleteIdStr.isBlank()) {
            try {
                int deleteId = Integer.parseInt(deleteIdStr);

                if (isAdmin != null && isAdmin) {
                    catchDao.deleteById(deleteId);
                } else {
                    // Normal user: only delete their own catch
                    catchDao.deleteForUser(deleteId, userId);
                }

                resp.sendRedirect(req.getContextPath() + "/addCatch?deleted=1");
                return;

            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Failed to delete catch.");
            }
        }

        // Success messages
        if ("1".equals(req.getParameter("added"))) {
            req.setAttribute("success", "Your catch has been added!");
        }
        if ("1".equals(req.getParameter("updated"))) {
            req.setAttribute("success", "Your catch has been updated!");
        }
        if ("1".equals(req.getParameter("deleted"))) {
            req.setAttribute("success", "Your catch has been deleted.");
        }

        String editIdStr = req.getParameter("editId");
        if (editIdStr != null && !editIdStr.isBlank()) {
            try {
                int editId = Integer.parseInt(editIdStr);
                catchDao.findById(editId).ifPresent(c -> {
                    // Admin can edit any catch; normal user only their own
                    if ((isAdmin != null && isAdmin) || c.getUserId().equals(userId)) {
                        req.setAttribute("editCatch", c);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            List<Catch> catches = catchDao.findByUserId(userId);
            req.setAttribute("catches", catches);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to load your catches.");
        }

        addSpeciesList(req);
        addBaitList(req);
        addLocationList(req);
        req.getRequestDispatcher("/WEB-INF/views/addCatch.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer sessionUserId = (Integer) req.getSession().getAttribute("userId");
        Boolean isAdmin       = (Boolean) req.getSession().getAttribute("isAdmin");

        if (sessionUserId == null) {
            resp.sendRedirect(req.getContextPath() + "/Login");
            return;
        }

        String ownerUserIdStr = req.getParameter("ownerUserId");
        Integer ownerUserId = sessionUserId;

        if (isAdmin != null && isAdmin && ownerUserIdStr != null && !ownerUserIdStr.isBlank()) {
            try {
                ownerUserId = Integer.valueOf(ownerUserIdStr);
            } catch (NumberFormatException ignored) {
                // fall back to session userId
            }
        }

        String catchIdStr     = req.getParameter("catchId");  // hidden field
        boolean isUpdate      = catchIdStr != null && !catchIdStr.isBlank();

        String speciesName      = req.getParameter("speciesName"); // from dropdown
        String locationName     = req.getParameter("locationName"); // from dropdown
        String baitType         = req.getParameter("baitType");  // from dropdown
        String dateCaught       = req.getParameter("dateCaught");
        String notes            = req.getParameter("notes");
        String lengthStr        = req.getParameter("length");
        String weightStr        = req.getParameter("weight");

        if (speciesName == null || speciesName.isBlank()) {
            req.setAttribute("error", "Please select a species.");
            forwardWithCatches(req, resp, ownerUserId);
            return;
        }

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
            forwardWithCatches(req, resp, ownerUserId);
            return;
        }

        // Lookup species in DB and enforce size limits
        SpeciesDao speciesDao = new SpeciesDao();
        try {
            var optSpecies = speciesDao.findByName(speciesName);
            if (optSpecies.isEmpty()) {
                req.setAttribute("error", "Invalid species. Please select a valid species.");
                forwardWithCatches(req, resp, ownerUserId);
                return;
            }

            var sp = optSpecies.get();
            if (length <= 0 || weight <= 0 ||
                    length > sp.getMaxLength() ||
                    weight > sp.getMaxWeight()) {

                req.setAttribute("error", "Length/weight exceed allowed max for " + sp.getSpeciesName() +
                        " (Max length: " + sp.getMaxLength() +
                        ", Max weight: " + sp.getMaxWeight() + ").");
                forwardWithCatches(req, resp, ownerUserId);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Could not validate species limits. Please try again.");
            forwardWithCatches(req, resp, ownerUserId);
            return;
        }

        Catch c = new Catch(
                ownerUserId,
                speciesName,
                locationName,
                baitType,
                dateCaught,
                notes,
                length,
                weight
        );

        if (isUpdate) {
            c.setCatchId(Integer.parseInt(catchIdStr));
        }

        try {
            if (isUpdate) {
                if (isAdmin != null && isAdmin) {
                    catchDao.updateAsAdmin(c);
                    resp.sendRedirect(req.getContextPath() + "/addCatch?updated=1");
                } else {
                    catchDao.update(c);
                    resp.sendRedirect(req.getContextPath() + "/addCatch?updated=1");
                }
            } else {
                catchDao.insert(c);
                resp.sendRedirect(req.getContextPath() + "/addCatch?added=1");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("error", "Failed to save catch: " + ex.getMessage());
            forwardWithCatches(req, resp, sessionUserId);
        }
    }

    private void forwardWithCatches(HttpServletRequest req, HttpServletResponse resp, Integer userId)
            throws ServletException, IOException {
        if (userId != null) {
            try {
                List<Catch> catches = catchDao.findByUserId(userId);
                req.setAttribute("catches", catches);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        addSpeciesList(req);
        addBaitList(req);
        addLocationList(req);
        req.getRequestDispatcher("/WEB-INF/views/addCatch.jsp").forward(req, resp);
    }

    private void addSpeciesList(HttpServletRequest req) {
        try {
            SpeciesDao dao = new SpeciesDao();
            req.setAttribute("speciesList", dao.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("speciesList", java.util.Collections.emptyList());
        }
    }

    private void addBaitList(HttpServletRequest req) {
        try {
            BaitDao dao = new BaitDao();
            req.setAttribute("baitList", dao.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("baitList", java.util.Collections.emptyList());
        }
    }

    // --- NEW: location list for dropdown ---
    private void addLocationList(HttpServletRequest req) {
        try {
            LocationDao dao = new LocationDao();
            // sorted by name ascending;
            req.setAttribute("locationList", dao.findAllSorted("locationName", "asc"));
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("locationList", java.util.Collections.emptyList());
        }
    }
}
