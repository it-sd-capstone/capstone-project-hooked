<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - Species</title>
    <%@include file="layouts/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Species</h1>
    </div>

    <%@include file="layouts/navigation.jsp"%>

    <form action="<%= request.getContextPath() %>/AddData" method="post">
        <label for="addSpecies">Specie:</label>
        <input type="text" id="addSpecies" name="addSpecies"> <br><br>
        <input type="submit" value="Add Location">
    </form>

    <br>

    <form action="${pageContext.request.contextPath}/SearchData" method="get">
        <label for="searchSpecies">Search by Species:</label>
        <input type="text" name="searchSpecies" id="searchSpecies">
        <br><br>
        <input type="submit">
    </form>

    <%@include file="layouts/footer.jsp"%>
</div>
</body>
</html>