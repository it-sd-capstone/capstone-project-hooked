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
        String newPasswordConfirmed = request.getParameter("newPasswordConfirmed");


        try (Connection c = DbUtil.getConnection()) {

            if (hash != null && !hash.isEmpty()) {
                if (newPassword == null || newPassword.trim().isEmpty() ||
                        newPasswordConfirmed == null || newPasswordConfirmed.trim().isEmpty()
                      || !newPassword.equals(newPasswordConfirmed)) {
                    message = "Passwords do not match";
                } else {

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
                        } else if (newPassword.length() < 6 || newPasswordConfirmed.length() < 6) {
                            message = "Your password is too short";
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
            }

            else if (email != null) {

                PreparedStatement ps = c.prepareStatement(
                        "SELECT UserID FROM Users WHERE email = ?");
                ps.setString(1, email);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    String resetHash = UUID.randomUUID().toString();

                    PreparedStatement ps2 = c.prepareStatement(
                            "UPDATE Users SET resetHash = ?, resetTime = CURRENT_TIMESTAMP WHERE email = ?");
                    ps2.setString(1, resetHash);
                    ps2.setString(2, email);
                    ps2.executeUpdate();

                    String resetLink = request.getScheme() + "://" +
                            request.getServerName() + ":" +
                            request.getServerPort() +
                            request.getContextPath() +
                            "/resetPassword?hash=" + resetHash;

                    // Sends Email
                    EmailUtil.sendEmail(email, "Password Reset",
                            "Click here to reset your password:\n" + resetLink +
                                    "\nIf you did not request to change your password, ignore this email.");

                    message = "A reset link has been sent to your email. You will have 15 minutes to complete the change.";

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


