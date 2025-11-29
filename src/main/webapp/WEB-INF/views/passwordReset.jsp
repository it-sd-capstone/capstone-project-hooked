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

    <% if (!isFormSet) { %>
    <!-- Email form -->
    <form action="<%= request.getContextPath() %>/resetPassword" method="post">
        <input type="email" name="email" placeholder="User@gmail.com" required>
        <input type="submit" value="Send Reset Link">
    </form>
    <% } else { %>
    <!-- Password reset form -->
    <form action="<%= request.getContextPath() %>/resetPassword" method="post">
        <input type="hidden" name="hash" value="<%= hash %>">
        <input type="password" name="newPassword" placeholder="Enter new password" required>
        <input type="submit" value="Reset Password">
    </form>
    <% } %>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>