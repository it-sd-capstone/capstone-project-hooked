<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - Logout</title>
    <%@include file="layouts/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Logout</h1>
    </div>

    <form action="Logout" method="post">
        <input type="submit" id="logoutButton" value="Logout">
    </form>

    <form action="index.jsp" method="post">
        <input type="submit" id="returnHome" value="Return Home">
    </form>

</div>
</body>
</html>