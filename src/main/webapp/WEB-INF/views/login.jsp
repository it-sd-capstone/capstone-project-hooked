<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - Login</title>
    <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Login</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <!-- Success message display -->
    <% if (session.getAttribute("SuccessMessage") != null) { %>
    <div class="success" style="padding: 10px; margin: 10px auto; max-width: 500px; border: 1px solid #10b981; background-color: rgba(16, 185, 129, 0.2); border-radius: 10px;">
        <strong>✓ Success:</strong> <%= session.getAttribute("SuccessMessage") %>
    </div>
    <%
        session.removeAttribute("SuccessMessage");
    %>
    <% } %>

    <!-- Error message display -->
    <% if (request.getAttribute("Error") != null) { %>
    <div style="color: #fecaca; padding: 10px; margin: 10px auto; max-width: 500px; border: 1px solid var(--border-soft); background-color: var(--bg-card); border-radius: 10px;">
        <strong>Error:</strong> <%= request.getAttribute("Error") %>
    </div>
    <% } %>

    <img id="loginPic" src="<c:url value='/assets/img/walleye-1.jpg' />" alt="walleye image"><br>

    <form action="<%= request.getContextPath() %>/Login" method="post">
        <label for="userName">Username:</label>
        <input type="text" id="userName" name="userName" placeholder="Username" required>

        <label for="passwordHash">Password:</label>
        <input type="password" id="passwordHash" name="passwordHash" placeholder="Password" required>

        <input type="submit" class="btn" value="Login">
    </form>

    <br>

    <ul class="nav nav-tabs">
        <li><a id="passwordReset" href="<%= request.getContextPath() %>/passwordReset">Forgot Password?</a></li>
        <li><a id="createAccount" href="<%= request.getContextPath() %>/Register">Don't Have an Account?</a></li>
    </ul>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>