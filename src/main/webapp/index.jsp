<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked</title>
    <%-- header will need to be changed to non-generic too match wireframe --%>
    <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Welcome to the Hooked website</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <div class="content">
    <img class="home-image" src="<c:url value='/assets/img/walleye-1.jpg' />" alt="walleye image"><br>

    <p>Hello, and welcome to Hooked.
        <br> This is example text to prove the technology stack works
        <br> If you are seeing this then the jsp and servlet are working.
    </p>

<%-- could put user instructions here, or repeat on every page. --%>

</div>

<%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>