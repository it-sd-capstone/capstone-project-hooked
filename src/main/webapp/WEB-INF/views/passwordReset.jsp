<%@ page import="java.sql.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String message = (String) request.getAttribute("message");
    String hash = request.getParameter("hash");
    boolean isFormSet = hash != null && !hash.isEmpty();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - PasswordReset</title>
    <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">
    <script>
        window.onload = function () {
            const passwordShow = document.getElementById("passwordView")
            const passwordViewButton = document.getElementById("passwordViewButton")

            passwordViewButton.addEventListener('mousedown', () => {
                passwordShow.type = 'text';
            });

            passwordViewButton.addEventListener('mouseup', () => {
                passwordShow.type = 'password';
            });

            passwordViewButton.addEventListener('mouseleave', () => {
                passwordShow.type = 'password';
            })
        }
    </script>

    <div class="header">
        <h1>Reset Password</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <h3><%= message != null ? message : "" %></h3>

    <% if (!isFormSet) { %>
    <form action="<%= request.getContextPath() %>/resetPassword" method="post">
        <input type="email" name="email" placeholder="User@gmail.com" required>
        <input type="submit" value="Send Reset Link">
    </form>
    <% } else { %>
    <form action="<%= request.getContextPath() %>/resetPassword" method="post">
        <input type="hidden" name="hash" value="<%= hash %>">
        <input type="password" id="passwordView" name="newPassword" placeholder="Enter new password" required>
        <input type="button" id="passwordViewButton" value="View Password" style="width: 120px; margin: 0 auto; text-align: center;">
        <input type="password" name="newPasswordConfirmed" placeholder="Confirm new password" required>
        <input type="submit" value="Reset Password">
    </form>
    <% } %>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>