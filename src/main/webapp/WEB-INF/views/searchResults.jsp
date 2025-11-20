<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - Search Results</title>
    <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Results from search input: </h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <table>
        <tr>
            <th></th>
            <th>Species</th>
            <th>Length (Inches)</th>
            <th>Weight (Pounds)</th>
            <th>Bait</th>
            <th>Location</th>
            <th>Date</th>
            <th>Notes</th>
        </tr>

        <c:forEach var="catch" items="${catchData}">
        <tr>
            <td>${catch.speciesName}</td>
            <td>${catch.length}</td>
            <td>${catch.weight}</td>
            <td>${catch.BaitType}</td>
            <td>${catch.locationName}</td>
            <td>${catch.dateCaught}</td>
            <td>${catch.notes}</td>
        </tr>
    </c:forEach>
    </table>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>