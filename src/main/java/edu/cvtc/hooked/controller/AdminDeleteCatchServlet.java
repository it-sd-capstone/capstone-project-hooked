package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.CatchDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/deleteCatch")
public class AdminDeleteCatchServlet extends HttpServlet {

    private final CatchDao catchDao = new CatchDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Only admins can delete from this page
        Boolean isAdmin = (Boolean) req.getSession().getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String catchIdStr = req.getParameter("catchId");

        if (catchIdStr != null && !catchIdStr.isBlank()) {
            try {
                int catchId = Integer.parseInt(catchIdStr);
                catchDao.deleteById(catchId);
            } catch (Exception e) {
                e.printStackTrace();
                // optional: you could pass an error message back via query param
                // resp.sendRedirect(req.getContextPath() + "/admin/catches?view=catches&error=Delete+failed");
                // return;
            }
        }

        // After delete, go back to admin catches table
        resp.sendRedirect(req.getContextPath() + "/admin/catches?view=catches");
    }
}
