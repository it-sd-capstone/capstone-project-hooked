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

    <div class="header">
        <h1>Reset Password</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <h3><%= message != null ? message : "" %></h3>

    <% if (!isFormSet) { %>
    <form action="<%= request.getContextPath() %>/resetPassword" method="post">
        <input type="email" name="email" placeholder="User@gmail.com" required>
        <input type="submit" class="btn" value="Send Reset Link">
    </form>
    <% } else { %>
    <h3>Your password must be 6-100 characters long, and contain no spaces.</h3>
    <form action="<%= request.getContextPath() %>/resetPassword" method="post">
        <input type="hidden" name="hash" value="<%= hash %>">
        <input type="password" id="passwordView" name="newPassword" minlength="6" maxlength="100" placeholder="Enter new password" required>
        <input type="button" class="btn" id="passwordViewButton" value="View Password" style="width: 140px; margin: 0 auto; text-align: center;">
        <input type="password" name="newPasswordConfirmed" id="newPasswordConfirmed" minlength="6" maxlength="100" placeholder="Confirm new password" required>
        <small id="passwordError" style="display: none; color: #fecaca; margin-top: 5px;"></small>
        <input type="submit" class="btn" value="Reset Password">
    </form>

    <% } %>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
<script>
    const passwordShow = document.getElementById("passwordView");
    const newPasswordConfirmed = document.getElementById("newPasswordConfirmed");
    const passwordViewButton = document.getElementById("passwordViewButton");

    passwordViewButton.addEventListener('mousedown', () => {
        passwordShow.type = 'text';
    });

    passwordViewButton.addEventListener('mouseup', () => {
        passwordShow.type = 'password';
    });

    passwordViewButton.addEventListener('mouseleave', () => {
        passwordShow.type = 'password';
    });

    newPasswordConfirmed.addEventListener('input', function() {
        const password = passwordShow.value;
        const confirmPassword = newPasswordConfirmed.value;
        const errorElement = document.getElementById('passwordError');

        if (confirmPassword && password !== confirmPassword) {
            errorElement.textContent = 'Passwords do not match!';
            errorElement.style.display = 'block';
        } else {
            errorElement.style.display = 'none';
        }
    });
</script>
</body>
</html>