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

    <img id="loginPic" src="<c:url value='/assets/img/walleye-1.jpg' />" alt="walleye image"><br>

    <form action="<%= request.getContextPath() %>/Login" method="post">
        <h6>Username:</h6>
        <input type="text" id="userName" name="userName" placeholder="userName">

        <h6>Password:</h6>
        <input type="password" id="passwordHash" name="passwordHash" placeholder="password"> <br><br>

        <input type="submit" value="Login">
    </form>

    <br>

    <ul class="nav nav-tabs">
        <li><a id="passwordReset" href="passwordReset.jsp">Forgot Password?</a></li>
        <li><a id="createAccount" href="<%= request.getContextPath() %>/Register">Don't Have an Account?</a></li>
        <li><a href="index.jsp">Home</a></li>
    </ul>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>