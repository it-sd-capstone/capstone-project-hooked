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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet("/searchCatches")
public class SearchCatchesServlet extends HttpServlet {

    private final CatchDao catchDao = new CatchDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String speciesRaw  = req.getParameter("searchSpecies");
        String location    = req.getParameter("searchLocation");
        String bait        = req.getParameter("searchBait");

        // sort parameters from the links in search.jsp
        String sort = req.getParameter("sort");
        String dir  = req.getParameter("dir");

        // Turn "walleye, bluegill" into ["walleye", "bluegill"]
        List<String> speciesList = null;
        if (speciesRaw != null && !speciesRaw.isBlank()) {
            speciesList = new ArrayList<>();
            for (String part : speciesRaw.split(",")) {
                String s = part.trim();
                if (!s.isEmpty()) {
                    speciesList.add(s);
                }
            }
        }

        // Show catches across all users
        Integer userIdFilter = null;

        List<Catch> matches = Collections.emptyList();
        try {
            matches = catchDao.searchOutput(userIdFilter, speciesList, location, bait, sort, dir);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Search failed: " + e.getMessage());
        }

        req.setAttribute("catches", matches);
        // keep typed values in the form
        req.setAttribute("searchSpecies", speciesRaw);
        req.setAttribute("searchLocation", location);
        req.setAttribute("searchBait", bait);

        req.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(req, resp);
    }

    private String trim(String s) {
        return (s == null) ? null : s.trim();
    }
}


