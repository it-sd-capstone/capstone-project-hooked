<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - Bait</title>
    <%@include file="layouts/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Bait</h1>
    </div>

    <%@include file="layouts/navigation.jsp"%>

    <form action="<%= request.getContextPath() %>/AddData" method="post">
        <label for="addBait">Bait:</label>
        <input type="text" id="addBait" name="addBait"> <br><br>
        <input type="submit" value="Add Bait">
    </form>

    <br>

    <form action="${pageContext.request.contextPath}/SearchData" method="get">
        <label for="searchBait">Search by Bait:</label>
        <input type="text" name="searchBait" id="searchBait">
        <br><br>
        <input type="submit">
    </form>

    <%@include file="layouts/footer.jsp"%>
</div>
</body>
</html>