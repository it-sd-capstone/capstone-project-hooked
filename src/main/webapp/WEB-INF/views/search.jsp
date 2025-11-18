<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - Search</title>
    <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Search the database!</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <h4>To search, type the species, location, or bait you want to search by
    Into the corresponding field. <br> Then, click the Search Catches button,
        and it will guide you to a new page with the results.
    </h4>

    <form action="<%= request.getContextPath() %>/searchCatches" method="get">
        <label for="searchSpecies">Species:</label>
        <input type="text" id="searchSpecies" name="searchSpecies" placeholder="ex. Largemouth Bass"> <br><br>
        <label for="searchLocation">Location:</label>
        <input type="text" id="searchLocation" name="searchLocation" placeholder="ex. Chippewa River"> <br><br>
        <label for="searchBait">Bait:</label>
        <input type="text" id="searchBait" name="searchBait" placeholder="ex. Jerkbait"> <br><br>
        <input type="submit" value="Search Catches">
    </form>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>