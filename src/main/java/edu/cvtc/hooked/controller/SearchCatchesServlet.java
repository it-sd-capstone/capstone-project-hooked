package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.CatchDao;
import edu.cvtc.hooked.model.Catch;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/searchCatches")
public class SearchCatchesServlet extends HttpServlet {

    private final CatchDao catchDao = new CatchDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String species  = trim(req.getParameter("searchSpecies"));
        String location = trim(req.getParameter("searchLocation"));
        String bait     = trim(req.getParameter("searchBait"));

        String sort = req.getParameter("sort"); // "species", "length", etc.
        String dir  = req.getParameter("dir");  // "asc" or "desc"

        try {
            List<Catch> catches = catchDao.searchAll(species, location, bait, sort, dir);
            req.setAttribute("catches", catches);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to load catches: " + e.getMessage());
        }

        // Always forward to the same JSP
        req.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(req, resp);
    }

    private String trim(String s) {
        return (s == null) ? null : s.trim();
    }
}


