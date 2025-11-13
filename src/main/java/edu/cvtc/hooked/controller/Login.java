package edu.cvtc.hooked.controller;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.ServletException;
import java.sql.*;
import jakarta.servlet.*;

@WebServlet(name = "Login", value = "/Login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String UserName = request.getParameter("UserName");
        String password = request.getParameter("password");

        try (Connection conn = DriverManager.getConnection("hooked.db")) {
            String sql = "SELECT * FROM users WHERE UserName = ? and password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, UserName);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("UserName", UserName);
                request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
            } else {
                request.setAttribute("Error", "userName or password is incorrect");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }

        }  catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("Error", "Database Error");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

}
