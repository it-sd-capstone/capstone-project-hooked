package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.SpeciesDao;
import edu.cvtc.hooked.model.Species;
import edu.cvtc.hooked.dao.CatchDao;
import edu.cvtc.hooked.model.Catch;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/SearchData")
public class SearchData extends HttpServlet {

    private final SpeciesDao speciesDao = new SpeciesDao();

    private final CatchDao catchDao = new CatchDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String speciesName = req.getParameter("speciesSearch");

        if (speciesName == null || speciesName.isBlank()) {
            req.setAttribute("error", "No species selected.");
            req.getRequestDispatcher("/WEB-INF/views/species.jsp").forward(req, resp);
            return;
        }

        // Look up that one species
        List<Species> result = speciesDao.searchByTerm(speciesName);

        req.setAttribute("speciesList", result);

        try{
        List<Catch> catches = catchDao.searchOutput(null, speciesName, null, null);
            if (catches.isEmpty()) {
                req.setAttribute("catchesForSpecies", null);
            } else {
                req.setAttribute("catchesForSpecies", catches);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error loading catches for this species.");
        }

        // Send user back to the species page to see result in the table
        req.getRequestDispatcher("/WEB-INF/views/species.jsp").forward(req, resp);
    }
}
