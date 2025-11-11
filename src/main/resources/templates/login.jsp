<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - Login</title>
    <%@include file="layouts/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Login</h1>
    </div>

    <form action="<%= request.getContextPath() %>/Login" method="post">
        <label for="username">Username: </label>
        <input type="text" id="username" name="username"> <br><br>
        <label for="password">Password:</label>
        <input type="text" id="password" name="password"> <br><br>
        <input type="submit" value="login">
    </form>

    <br>

    <ul class="nav nav-tabs">
        <li><a href="passwordReset.jsp">Forgot Password?</a></li>
        <li><a href="index.jsp">Home</a></li>
    </ul>

</div>
</body>
</html>