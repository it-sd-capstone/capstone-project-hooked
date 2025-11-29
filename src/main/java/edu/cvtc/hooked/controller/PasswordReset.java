package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.dao.UserDao;
import edu.cvtc.hooked.model.User;
import edu.cvtc.hooked.util.DbUtil;
import edu.cvtc.hooked.util.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;


@WebServlet(name = "passwordReset", value = "/resetPassword")
public class PasswordReset extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/passwordReset.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String hash = request.getParameter("hash");
        String newPassword = request.getParameter("newPassword");
        String message = "";

        try (Connection c = DbUtil.getConnection()) {
            if (hash != null && !hash.isEmpty()) {
                try (PreparedStatement ps = c.prepareStatement("SELECT * FROM Users WHERE resetHash = ?")) {
                    ps.setString(1, hash);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            if (newPassword == null || newPassword.isEmpty()) {
                                message = "Please enter a new password.";
                            } else {
                                try (PreparedStatement ps2 = c.prepareStatement(
                                        "UPDATE Users SET passwordHash = ?, resetHash = NULL, resetTime = NULL WHERE resetHash = ?")) {
                                    ps2.setString(1, newPassword);
                                    ps2.setString(2, hash);
                                    ps2.executeUpdate();
                                    message = "Password successfully reset!";
                                }
                            }
                        } else {
                            message = "Invalid or expired reset link.";
                        }
                    }
                }
            } else if (email != null && !email.isEmpty()) {
                try (PreparedStatement ps = c.prepareStatement("SELECT * FROM Users WHERE email = ?")) {
                    ps.setString(1, email);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String resetHash = UUID.randomUUID().toString();
                            try (PreparedStatement ps2 = c.prepareStatement(
                                    "UPDATE Users SET resetHash = ?, resetTime = CURRENT_TIMESTAMP WHERE email = ?")) {
                                ps2.setString(1, resetHash);
                                ps2.setString(2, email);
                                ps2.executeUpdate();
                            }
                            String resetLink = request.getRequestURL().toString() + "?hash=" + resetHash;
                            EmailUtil.sendEmail(email, "Password Reset", "Click here to reset your password: " + resetLink);
                            message = "Reset link sent to " + email;
                        } else {
                            message = "Email not found.";
                        }
                    }
                }
            } else {
                message = "Enter an email address.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Error: " + e.getMessage();
        }

        request.setAttribute("message", message);
        request.getRequestDispatcher("/passwordReset.jsp").forward(request, response);
    }
}
