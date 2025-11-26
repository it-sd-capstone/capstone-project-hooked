package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.CatchDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deleteCatch")
public class DeleteCatchServlet extends HttpServlet {

    private final CatchDao catchDao = new CatchDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/Login");
            return;
        }

        String catchIdStr = req.getParameter("catchId");

        try {
            int catchId = Integer.parseInt(catchIdStr);
            catchDao.deleteForUser(catchId, userId);
            resp.sendRedirect(req.getContextPath() + "/addCatch?deleted=1");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to delete catch.");
            req.getRequestDispatcher("/addCatch").forward(req, resp);
        }
    }
}
