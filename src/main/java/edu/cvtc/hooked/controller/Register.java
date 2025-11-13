package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.UserDao;
import edu.cvtc.hooked.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "Register", value = "/Register")
public class Register extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/views/createAccount.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String userName = request.getParameter("userName");
        String passwordHash = request.getParameter("passwordHash");

        User user = new User(firstName, lastName, userName, passwordHash);
        UserDao dao = new UserDao();

        try {
            dao.insert(user);
            response.sendRedirect(request.getContextPath()+"/Login");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("Error", "Registration Failed");
            request.getRequestDispatcher("/WEB-INF/views/createAccount.jsp").forward(request, response);
        }
    }
}
