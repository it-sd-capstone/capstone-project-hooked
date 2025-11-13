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

    <img src="<c:url value='/assets/img/walleye-1.jpg' />" alt="walleye image"><br>

    <form action="<%= request.getContextPath() %>/Login" method="post">
        <h6>Username:</h6>
        <input type="text" id="UserName" name="UserName" placeholder="UserName">

        <h6>Password:</h6>
        <input type="password" id="password" name="password" placeholder="password">

        <input type="submit" value="Login">
    </form>

    <br>

    <ul class="nav nav-tabs">
        <li><a id="passwordReset" href="passwordReset.jsp">Forgot Password?</a></li>
        <li><a id="createAccount" href="<%= request.getContextPath() %>/Register">Don't Have an Account?</a></li>
        <li><a href="index.jsp">Home</a></li>
    </ul>

</div>
</body>
</html>