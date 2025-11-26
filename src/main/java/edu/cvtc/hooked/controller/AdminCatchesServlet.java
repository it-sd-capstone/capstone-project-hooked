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

@WebServlet("/admin/catches")
public class AdminCatchesServlet extends HttpServlet {

    private final CatchDao catchDao = new CatchDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Boolean isAdmin = (Boolean) req.getSession().getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            List<Catch> all = catchDao.findAll();
            req.setAttribute("catches", all);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to load catches: " + e.getMessage());
        }

        req.getRequestDispatcher("/WEB-INF/views/admin-catches.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Boolean isAdmin = (Boolean) req.getSession().getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action   = req.getParameter("action");
        String catchIdStr = req.getParameter("catchId");

        try {
            if ("deleteOne".equals(action) && catchIdStr != null) {
                int catchId = Integer.parseInt(catchIdStr);
                catchDao.deleteById(catchId);
            } else if ("clearAll".equals(action)) {
                catchDao.deleteAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Operation failed: " + e.getMessage());
        }

        // Always redirect back to GET so the table refreshes
        resp.sendRedirect(req.getContextPath() + "/admin/catches");
    }
}
