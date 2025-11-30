package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.model.SpeciesRestrictions;
import edu.cvtc.hooked.dao.SpeciesRequestDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/species")
public class SpeciesServlet extends HttpServlet {

    private final SpeciesRequestDao requestDao = new SpeciesRequestDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Read search + clear params
        String clearParam = req.getParameter("clear");
        String speciesSearch = req.getParameter("speciesSearch");

        // If the user clicked "Clear Search", ignore the speciesSearch
        if ("1".equals(clearParam)) {
            speciesSearch = null;
        }

        // Build full sorted name list for the dropdown
        List<String> allNames = new ArrayList<>(SpeciesRestrictions.ALL.keySet());
        Collections.sort(allNames);
        req.setAttribute("speciesNames", allNames);

        // Build full sorted entry list for the table
        List<Map.Entry<String, SpeciesRestrictions>> allEntries =
                new ArrayList<>(SpeciesRestrictions.ALL.entrySet());
        allEntries.sort(Map.Entry.comparingByKey());

        // Decide what to show in the table
        List<Map.Entry<String, SpeciesRestrictions>> restrictionList;

        if (speciesSearch != null && !speciesSearch.isBlank()) {
            List<Map.Entry<String, SpeciesRestrictions>> filtered = new ArrayList<>();
            for (Map.Entry<String, SpeciesRestrictions> e : allEntries) {
                if (e.getKey().equalsIgnoreCase(speciesSearch)) {
                    filtered.add(e);
                }
            }
            // If nothing matches for some reason, fall back to full list
            restrictionList = filtered.isEmpty() ? allEntries : filtered;
        } else {
            restrictionList = allEntries;
        }

        // For JSP: tells it whether we’re currently filtered
        req.setAttribute("currentSpeciesSearch", speciesSearch);
        req.setAttribute("restrictionList", restrictionList);

        req.getRequestDispatcher("/WEB-INF/views/species.jsp").forward(req, resp);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String rawName = req.getParameter("addSpecies");
        if (rawName == null || rawName.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/species?error=Species+name+is+required");
            return;
        }

        String formatted = toTitleCase(rawName.trim());

        // logged-in userId if available
        Integer userId = (Integer) req.getSession().getAttribute("userId");

        try {
            requestDao.insert(formatted, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/species?error=Unable+to+save+request");
            return;
        }

        // redirect back with success + the formatted name (your JSP already uses param.success/param.formatted)
        String redirectUrl = String.format("%s/species?success=1&formatted=%s",
                req.getContextPath(),
                URLEncoder.encode(formatted, StandardCharsets.UTF_8));
        resp.sendRedirect(redirectUrl);
    }

    private String toTitleCase(String input) {
        String[] parts = input.toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isEmpty()) continue;
            sb.append(Character.toUpperCase(parts[i].charAt(0)));
            if (parts[i].length() > 1) {
                sb.append(parts[i].substring(1));
            }
            if (i < parts.length - 1) sb.append(" ");
        }
        return sb.toString();
    }
}
