package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.BaitDao;
import edu.cvtc.hooked.model.Bait;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "BaitServlet", urlPatterns = "/bait")
public class BaitServlet extends HttpServlet {

    private final BaitDao baitDao = new BaitDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String searchTerm = req.getParameter("searchTerm");
        List<Bait> baits;

        if (searchTerm != null && !searchTerm.isBlank()) {
            baits = baitDao.searchByTerm(searchTerm.trim());
            req.setAttribute("searchActive", true);
        } else {
            baits = baitDao.findAll();
            req.setAttribute("searchActive", false);
        }

        req.setAttribute("baits", baits);

        req.getRequestDispatcher("/WEB-INF/views/bait.jsp")
                .forward(req, resp);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Handle "Add Bait" form submission
        String name = req.getParameter("addBait");
        String notes = req.getParameter("notes");

        if (name != null && !name.isBlank()) {
            Bait bait = new Bait(name.trim(), notes);
            baitDao.insert(bait);
        }

        // Post/Redirect/Get pattern to avoid resubmission
        resp.sendRedirect(req.getContextPath() + "/bait");
    }
}
