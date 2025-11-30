package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.CatchDao;
import edu.cvtc.hooked.dao.UserDao;
import edu.cvtc.hooked.dao.SpeciesRequestDao;
import edu.cvtc.hooked.model.SpeciesRequest;
import edu.cvtc.hooked.model.Catch;
import edu.cvtc.hooked.model.User;
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
    private final UserDao userDao   = new UserDao();
    private final SpeciesRequestDao speciesRequestDao = new SpeciesRequestDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Boolean isAdmin = (Boolean) req.getSession().getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String view = req.getParameter("view");
        if (!"users".equals(view)) {
            view = "catches"; // default
        }
        req.setAttribute("view", view);

        try {
            if ("users".equals(view)) {
                List<User> users = userDao.findAll();
                req.setAttribute("users", users);
            } else {
                List<Catch> all = catchDao.findAll();
                req.setAttribute("catches", all);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to load data: " + e.getMessage());
        }

        try {
            List<SpeciesRequest> requests = speciesRequestDao.findAll();
            req.setAttribute("speciesRequests", requests);
        } catch (SQLException e) {
            e.printStackTrace();
            // don't kill the page if this fails, just log it
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

        String action = req.getParameter("action");
        String catchIdStr = req.getParameter("catchId");
        String requestIdStr = req.getParameter("requestId");
        String view = req.getParameter("view");
        if (!"users".equals(view)) {
            view = "catches";  // default if missing
        }

        try {
            if ("deleteOne".equals(action) && catchIdStr != null) {
                int catchId = Integer.parseInt(catchIdStr);
                catchDao.deleteById(catchId);

            } else if ("clearAll".equals(action)) {
                catchDao.deleteAll();

            } else if ("deleteSpeciesRequest".equals(action) && requestIdStr != null) {
                int requestId = Integer.parseInt(requestIdStr);
                speciesRequestDao.deleteById(requestId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Operation failed: " + e.getMessage());
        }

        // After POST always go back to catches view (safe default)
        resp.sendRedirect(req.getContextPath() + "/admin/catches?view=catches");
    }
}
