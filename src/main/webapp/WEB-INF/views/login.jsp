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

    <img src="Images/imagename.jpg" alt="Image description"> <br>

    <form action="<%= request.getContextPath() %>/Login" method="post">
        <h6>Username:</h6>
        <input type="text" id="username" name="username" placeholder="Username">
        <h6>Password:</h6>
        <input type="text" id="password" name="password" placeholder="Password">
        <h6>Email:</h6>
        <input type="text" id="email" name="email" placeholder="User@gmail.com">
        <input type="submit" value="login">
    </form>

    <br>

    <ul class="nav nav-tabs">
        <li><a id="passwordReset" href="passwordReset.jsp">Forgot Password?</a></li>
        <li><a id="createAccount" href="createAccount.jsp">Don't Have an Account?</a></li>
        <li><a href="index.jsp">Home</a></li>
    </ul>

</div>
</body>
</html>