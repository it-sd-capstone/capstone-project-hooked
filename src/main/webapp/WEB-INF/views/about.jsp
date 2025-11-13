<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - About</title>
    <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>About</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <img src="<c:url value='/assets/img/walleye-1.jpg' />"
         alt="walleye image"
         style="width: 500px; height: auto; display: block; margin: 0 auto 20px auto;"><br>


    <p>Welcome to <strong>Hooked</strong> - a place where you can log and track the fish you catch!<br>
        Here you can record your catches, locations, and bait you used! <br>
        This is a great place for every angler!</p>

    <p>User Instructions</p>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>