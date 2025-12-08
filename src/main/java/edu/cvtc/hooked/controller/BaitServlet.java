package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.BaitDao;
import edu.cvtc.hooked.model.Bait;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "BaitServlet", urlPatterns = "/bait")
public class BaitServlet extends HttpServlet {

    private final BaitDao baitDao = new BaitDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Boolean isAdmin = (session != null) ? (Boolean) session.getAttribute("isAdmin") : null;
        if (isAdmin == null) {
            isAdmin = Boolean.FALSE;
        }

        String deleteIdParam = req.getParameter("deleteId");
        String editIdParam   = req.getParameter("editId");

        // --- DELETE (admin only) ---
        if (deleteIdParam != null && !deleteIdParam.isBlank()) {
            if (!isAdmin) {
                req.setAttribute("error", "You are not authorized to delete baits.");
            } else {
                try {
                    int deleteId = Integer.parseInt(deleteIdParam);
                    baitDao.deleteById(deleteId);
                    req.setAttribute("success", "Bait deleted.");
                } catch (Exception e) {
                    e.printStackTrace();
                    req.setAttribute("error", "Unable to delete bait.");
                }
            }
        }

        // --- EDIT (load bait to prefill form) ---
        if (editIdParam != null && !editIdParam.isBlank()) {
            if (!isAdmin) {
                req.setAttribute("error", "You are not authorized to edit baits.");
            } else {
                try {
                    int editId = Integer.parseInt(editIdParam);
                    baitDao.findById(editId)
                            .ifPresent(b -> req.setAttribute("baitToEdit", b));
                } catch (Exception e) {
                    e.printStackTrace();
                    req.setAttribute("error", "Unable to load bait for editing.");
                }
            }
        }

        // --- Sorting (same as before) ---
        String sortParam = req.getParameter("sort");      // "asc" or "desc"
        String sortDir   = "desc".equalsIgnoreCase(sortParam) ? "desc" : "asc";  // default asc

        try {
            // Use your existing sorted method
            List<Bait> all = baitDao.findAllSorted("name", sortDir);
            req.setAttribute("baitList", all);
            req.setAttribute("sortOrder", sortDir);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to load bait list.");
        }

        req.getRequestDispatcher("/WEB-INF/views/bait.jsp").forward(req, resp);
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
                        req.setAttribute("error", "You are not authorized to delete baits.");
                    } else {
                        int baitId = Integer.parseInt(req.getParameter("baitId"));
                        baitDao.deleteById(baitId);
                        req.setAttribute("success", "Bait deleted successfully.");
                    }
                }
                case "update" -> {
                    if (!isAdmin) {
                        req.setAttribute("error", "You are not authorized to edit baits.");
                    } else {
                        int baitId   = Integer.parseInt(req.getParameter("baitId"));
                        String name  = req.getParameter("name");
                        String notes = req.getParameter("notes");

                        if (name == null || name.isBlank()) {
                            req.setAttribute("error", "Bait name cannot be empty.");
                        } else if (!name.trim().matches("^[A-Za-z0-9\\-' ]{2,50}$")) {
                             req.setAttribute("error", "Bait name must be 2–50 characters: letters, numbers, spaces, dashes, or apostrophes.");
                        } else {
                            String normalizedName = name.trim().toLowerCase();

                            boolean nameTakenByAnother = baitDao.findByName(normalizedName)
                                    .filter(b -> b.getId() != baitId)
                                    .isPresent();

                            if (nameTakenByAnother) {
                                req.setAttribute("error", "That bait name is already used by another record.");
                            } else {
                                Bait updated = new Bait();
                                updated.setId(baitId);
                                updated.setName(normalizedName);
                                updated.setNotes(notes);
                                updated.setCreatedByUserId(userId);

                                baitDao.update(updated);
                                req.setAttribute("success", "Bait updated successfully.");
                            }
                        }
                    }
                }
                case "create" -> {
                    String name  = req.getParameter("name");
                    String notes = req.getParameter("notes");

                    if (name == null || name.isBlank()) {
                        req.setAttribute("error", "Bait name is required.");
                    } else if (!name.trim().matches("^[A-Za-z0-9\\-' ]{2,50}$")) {
                        req.setAttribute("error", "Bait name must be 2–50 characters: letters, numbers, spaces, dashes, or apostrophes.");
                    } else {
                        String normalizedName = name.trim().toLowerCase();

                        if (baitDao.exists(normalizedName)) {
                            req.setAttribute("error", "The bait '" + normalizedName + "' already exists.");
                        } else {
                            Bait bait = new Bait();
                            bait.setName(normalizedName);
                            bait.setNotes(notes);
                            bait.setCreatedByUserId(userId);

                            baitDao.insert(bait);
                            req.setAttribute("success", "Bait '" + normalizedName + "' added successfully.");
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

        // Reload page like SpeciesServlet
        doGet(req, resp);
    }
}
