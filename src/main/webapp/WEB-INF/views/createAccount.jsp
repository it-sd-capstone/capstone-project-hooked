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

    <h6></h6>

    <form action="<%= request.getContextPath() %>/Register" method="post">
        <h6>First Name:</h6>
        <input type="text" id="firstName" name="firstName" placeholder="firstName">

        <h6>Last Name:</h6>
        <input type="text" id="lastName" name="lastName" placeholder="lastName">

        <h6>Username:</h6>
        <input type="text" id="userName" name="userName" placeholder="userName">

        <h6>Password:</h6>
        <input type="password" id="passwordHash" name="passwordHash" placeholder="passwordHash">

        <input type="submit" value="Create Account">
    </form>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>