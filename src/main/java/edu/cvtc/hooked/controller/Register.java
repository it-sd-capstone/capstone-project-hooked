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
        String email = request.getParameter("email");
        String passwordHash = request.getParameter("passwordHash");

        User user = new User(firstName, lastName, userName, email, null, null, passwordHash);
        UserDao dao = new UserDao();

        try {
            // Get the database path
            String dbPath = edu.cvtc.hooked.util.DbUtil.databasePath();
            System.out.println("Database file being used: " + dbPath);


            if (dao.findByUserName(userName).isPresent()) {
                request.setAttribute("Error", "Username already taken. Please choose another.");
                request.getRequestDispatcher("/WEB-INF/views/createAccount.jsp").forward(request, response);
                return;
            }

            // Insert the user
            dao.insert(user);

            // Print info after insert
            System.out.println("Inserted user: " + userName + " into DB at " + dbPath);

            // Crash to show console
            //throw new RuntimeException("Crash after inserting user '" + userName + "' into DB at " + dbPath);

            // Redirects to login.
             response.sendRedirect(request.getContextPath() + "/Login");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("Error", "Registration Failed: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/createAccount.jsp").forward(request, response);
        }
    }

}
