package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.util.DbUtil;
import edu.cvtc.hooked.util.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;


@WebServlet(name="passwordReset", value="/resetPassword")
public class PasswordReset extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String hash = request.getParameter("hash");
        request.setAttribute("hash", hash);

        request.getRequestDispatcher("/passwordReset").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String hash = request.getParameter("hash");
        String newPassword = request.getParameter("newPassword");
        String message = "";

        try (Connection c = DbUtil.getConnection()) {

            if (hash != null && !hash.isEmpty()) {

                PreparedStatement ps = c.prepareStatement(
                        "SELECT resetTime FROM Users WHERE resetHash = ?");
                ps.setString(1, hash);

                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    message = "Invalid reset link.";
                } else {
                    Timestamp resetTime = rs.getTimestamp("resetTime");

                    // Sets timer for 15 minutes before link goes invalid
                    if (resetTime.toLocalDateTime().isBefore(LocalDateTime.now().minusMinutes(15))) {
                        message = "Reset link expired.";
                    } else if (newPassword == null || newPassword.isEmpty()) {
                        message = "Please enter a new password.";
                    } else {

                        PreparedStatement ps2 = c.prepareStatement(
                                "UPDATE Users SET passwordHash = ?, resetHash = NULL, resetTime = NULL WHERE resetHash = ?");
                        ps2.setString(1, newPassword);
                        ps2.setString(2, hash);
                        ps2.executeUpdate();

                        message = "Password successfully reset!";
                    }
                }
            }

            else if (email != null) {

                PreparedStatement ps = c.prepareStatement(
                        "SELECT UserID FROM Users WHERE email = ?");  // FIXED
                ps.setString(1, email);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    String resetHash = UUID.randomUUID().toString();

                    PreparedStatement ps2 = c.prepareStatement(
                            "UPDATE Users SET resetHash = ?, resetTime = CURRENT_TIMESTAMP WHERE email = ?");
                    ps2.setString(1, resetHash);
                    ps2.setString(2, email);
                    ps2.executeUpdate();

                    // Sends Email
                    String resetLink = request.getScheme() + "://" +
                            request.getServerName() + ":" +
                            request.getServerPort() +
                            request.getContextPath() +
                            "/resetPassword?hash=" + resetHash;

                    EmailUtil.sendEmail(email, "Password Reset",
                            "Click here to reset your password:\n" + resetLink + "\nThis link is valid for 15 minutes.");

                    message = "Reset link sent to " + email;

                } else {
                    message = "Email not found.";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            message = "Error: " + e.getMessage();
        }

        request.setAttribute("message", message);
        request.getRequestDispatcher("/passwordReset").forward(request, response);
    }
}


