package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.CatchDao;
import edu.cvtc.hooked.model.Catch;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/searchCatches")
public class SearchCatchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        handleSearch(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        handleSearch(req, resp);
    }

    private void handleSearch(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userIdStr = req.getParameter("userId");

        // If no userId provided, just show the search form
        if (userIdStr == null || userIdStr.isBlank()) {
            req.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(req, resp);
            return;
        }

        try {
            Integer userId = Integer.valueOf(userIdStr);

            CatchDao dao = new CatchDao();
            List<Catch> results = dao.findByUserId(userId);

            req.setAttribute("results", results);
            req.setAttribute("userId", userIdStr);

            req.getRequestDispatcher("/WEB-INF/views/searchResults.jsp").forward(req, resp);

        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("error", "Search failed: " + ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(req, resp);
        }
    }
}
