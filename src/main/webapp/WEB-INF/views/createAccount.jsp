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

    <!-- Error message display -->
    <% if (request.getAttribute("Error") != null) { %>
    <div style="color: red; padding: 10px; margin: 10px 0; border: 1px solid red; background-color: #ffe6e6;">
        <strong>Error:</strong> <%= request.getAttribute("Error") %>
    </div>
    <% } %>

    <h6></h6>

    <form action="<%= request.getContextPath() %>/Register" method="post">
        <h6>First Name:</h6>
        <input type="text" id="firstName" name="firstName" placeholder="First Name" required>

        <h6>Last Name:</h6>
        <input type="text" id="lastName" name="lastName" placeholder="Last Name" required>

        <h6>Username:</h6>
        <input type="text" id="userName" name="userName" placeholder="Username" required>

        <h6>Email:</h6>
        <input type="email" id="email" name="email" placeholder="Email" required>

        <h6>Password:</h6>
        <input type="password" id="passwordHash" name="passwordHash" placeholder="Password" required minlength="6"> <br><br>

        <input type="submit" value="Create Account">
    </form>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>