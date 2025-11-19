package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.UserDao;
import edu.cvtc.hooked.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name = "Login", value = "/Login")
public class Login extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String passwordHash = request.getParameter("passwordHash");

        UserDao dao = new UserDao();

        try {
            Optional<User> optUser = dao.findByUserName(userName);

            if (optUser.isPresent() && optUser.get().getPasswordHash().equals(passwordHash)) {

                HttpSession session = request.getSession();
                session.setAttribute("user", optUser.get());

                response.sendRedirect(request.getContextPath() + "/IndexServlet");
                return;
            } else {

                // Login failed error.
                request.setAttribute("Error", "Invalid username or password.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Database error");
            getServletContext().getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}