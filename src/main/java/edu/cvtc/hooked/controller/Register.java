package edu.cvtc.hooked.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "Register", value = "/Register")
public class Register extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/views/createAccount.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String UserName = request.getParameter("UserName");
        String password = request.getParameter("password");

        String dbPath = "hooked.db";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath)) {
            String sql = "INSERT INTO users (firstName, lastName, UserName, password) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, UserName);
            stmt.setString(4, password);

            stmt.executeUpdate();

            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);

        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("Error", "Registration Failed");
            request.getRequestDispatcher("/WEB-INF/views/createAccount.jsp").forward(request, response);
        }
    }
}
