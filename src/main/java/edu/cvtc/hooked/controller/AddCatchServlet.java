package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.CatchDao;
import edu.cvtc.hooked.model.Catch;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/addCatch")
public class AddCatchServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // read form fields
        String userIdStr     = req.getParameter("userId");
        String speciesIdStr  = req.getParameter("speciesId");
        String locationIdStr = req.getParameter("locationId");
        String baitIdStr     = req.getParameter("baitId");
        String dateCaught    = req.getParameter("dateCaught"); // e.g. "2025-11-17"
        String notes         = req.getParameter("notes");

        try {
            Integer userId     = (userIdStr != null && !userIdStr.isBlank()) ? Integer.valueOf(userIdStr) : null;
            Integer speciesId  = (speciesIdStr != null && !speciesIdStr.isBlank()) ? Integer.valueOf(speciesIdStr) : null;
            Integer locationId = (locationIdStr != null && !locationIdStr.isBlank()) ? Integer.valueOf(locationIdStr) : null;
            Integer baitId     = (baitIdStr != null && !baitIdStr.isBlank()) ? Integer.valueOf(baitIdStr) : null;

            // You can add validation here if needed (e.g. ensure none are null)

            Catch c = new Catch(userId, speciesId, locationId, baitId, dateCaught, notes);

            CatchDao dao = new CatchDao();
            dao.insert(c);

            // redirect after successful insert
            resp.sendRedirect(req.getContextPath() + "/searchCatches");

        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("error", "Unable to save catch: " + ex.getMessage());
            // go back to the add form
            req.getRequestDispatcher("/WEB-INF/views/addCatch.jsp").forward(req, resp);
        }
    }
}
