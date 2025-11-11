<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - Locations</title>
    <%@include file="layouts/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Locations</h1>
    </div>

    <%@include file="layouts/navigation.jsp"%>

    <form action="<%= request.getContextPath() %>/AddData" method="post">
        <label for="addLocation">Location:</label>
        <input type="text" id="addLocation" name="addLocation"> <br><br>
        <input type="submit" value="Add Location">
    </form>

    <br>

    <form action="${pageContext.request.contextPath}/SearchData" method="get">
        <label for="searchLocation">Search by Location:</label>
        <input type="text" name="searchLocation" id="searchLocation">
        <br><br>
        <input type="submit">
    </form>

    <%@include file="layouts/footer.jsp"%>
</div>
</body>
</html>