/*
package controllers;

import java.io.*;
import java.util.stream.Collectors;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.ServletException;

@WebServlet(name = "SearchData", value = "/SearchData")
public class SearchData extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            // THIS IS REUSED CODE FROM A PREVIOUS PROJECT, I HAVE UPDATED IT FOR THIS PROJECT
            // AS IT DOES NTO HAVE A FEW METHODS AND RESOURCES THERE ARE A FEW ERRORS
            // ALL MOVIE THINGS WILL NEED TO BE REPLACE WITH APPROPRIATE METHODS WHEN THEY ARE CREATED


             final speciesDao speciesDao = new SpeciesDaoImpl();
            final locationsDao locationsDao = new LocationsDaoImpl();
             final baitsDao baitDao = new BaitsDaoImpl();
            species final List<Species> specie = speciesDao.retrieveSpecies();
             location final List<Locations> location = locationDao.retrieveLocations();
             bait final List<Baits> bait = baitDao.retrieveBaits();


            String speciesFilter = request.getParameter("species");
            String locationFilter = request.getParameter("location");
            String baitFilter = request.getParameter("bait");

            if(request.getParameter("species") != null && !request.getParameter("species").isEmpty()) {
                final List<Species> filteredBySpecies = specie.stream()
                        .filter( (Species s) -> s.getSpecies().equalsIgnoreCase(speciesFilter))
                        .collect(Collectors.toList());

                request.setAttribute("", filteredBySpecies);
            }

            if(request.getParameter("location") != null && !request.getParameter("location").isEmpty()) {
                final List<Locations> filteredByLocation = location.stream()
                        .filter( (Locations l) -> l.getLocation().equalsIgnoreCase(locationFilter))
                        .collect(Collectors.toList());

                request.setAttribute("", filteredByLocation);
            }

            if(request.getParameter("bait") != null && !request.getParameter("bait").isEmpty()) {
                final List<Baits> filteredByBait = bait.stream()
                        .filter( (Baits b) -> b.getBait().equalsIgnoreCase(baitFilter))
                        .collect(Collectors.toList());

                request.setAttribute("movie", filteredByBait);
            }

        } catch (Exception e) {
            throw new  RuntimeException(e);
        }

        getServletContext().getRequestDispatcher("index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
*/