<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked</title>
    <%-- header will need to be changed to non-generic too match wireframe --%>
    <%@include file="layouts/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Welcome to the Hooked website</h1>
    </div>

    <%@include file="layouts/navigation.jsp"%>

    <p>Hello, and welcome to Hooked.
        <br> This is example text to prove the technology stack works
        <br> If you are seeing this then the jsp and servlet are working.

        <%@include file="login.jsp"%>
        <%@include file="logout.jsp"%>

        <%@include file="layouts/footer.jsp"%>
</div>
</body>
</html>