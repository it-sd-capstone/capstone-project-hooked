<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - CreateAccount</title>
    <%@include file="layouts/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Create Your Account</h1>
    </div>

    <%@include file="layouts/navigation.jsp"%>

    <h6></h6>

    <form action="<%= request.getContextPath() %>/Login" method="post">
        <h6>Username:</h6>
        <input type="text" id="username" name="username" placeholder="Username">
        <h6>Password:</h6>
        <input type="text" id="password" name="password" placeholder="Password">
        <h6>Email:</h6>
        <input type="text" id="email" name="email" placeholder="User@gmail.com">
        <input type="submit" value="login">
    </form>

    <%@include file="layouts/footer.jsp"%>
</div>
</body>
</html>