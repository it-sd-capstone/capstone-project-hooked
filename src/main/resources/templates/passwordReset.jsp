<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - PasswordReset</title>
    <%@include file="layouts/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Reset Password</h1>
    </div>

    <%@include file="layouts/navigation.jsp"%>

    <h6>Email to send reset form too:</h6>
    <form action="<%= request.getContextPath() %>/Login" method="post">
        <input type="text" id="email" name="email" placeholder="User@gmail.com">
        <input type="submit" value="login">
    </form>

    <%@include file="layouts/footer.jsp"%>
</div>
</body>
</html>