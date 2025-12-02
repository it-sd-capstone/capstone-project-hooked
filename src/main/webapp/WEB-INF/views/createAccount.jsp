<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - CreateAccount</title>
    <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Create Your Account</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <!-- Error message display. -->
    <% if (request.getAttribute("Error") != null) { %>
    <div style="color: red; padding: 10px; margin: 10px 0; border: 1px solid red; background-color: #ffe6e6;">
        <strong>Error:</strong> <%= request.getAttribute("Error") %>
    </div>
    <% } %>

    <form action="<%= request.getContextPath() %>/Register" method="post">
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" placeholder="First Name" required>

        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" placeholder="Last Name" required>

        <label for="userName">Username:</label>
        <input type="text" id="userName" name="userName" placeholder="Username" required>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" placeholder="Email" required>

        <label for="passwordHash">Password:</label>
        <input type="password" id="passwordHash" name="passwordHash" placeholder="Password (minimum 6 characters)" required minlength="6" title="Password must be at least 6 characters long">
        <small style="display: block; color: #666; margin-top: 5px;">Minimum 6 characters</small>

        <br>
        <input type="submit" value="Create Account">
    </form>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>