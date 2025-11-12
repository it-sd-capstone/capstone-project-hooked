<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - Species</title>
    <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Species</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <img src="Images/imagename.jpg" alt="Image description"><br><br>

    <form action="<%= request.getContextPath() %>/AddData" method="post">
        <label for="addSpecies">Species:</label>
        <input type="text" id="addSpecies" name="addSpecies"> <br><br>
        <input type="submit" value="Add Species">
    </form>

    <br>

    <%--
    <form action="${pageContext.request.contextPath}/SearchData" method="get">
        <label for="searchSpecies">Search by Species:</label>
        <input type="text" name="searchSpecies" id="searchSpecies">
        <br><br>
        <input type="submit">
    </form>
    --%>

    <form action="${pageContext.request.contextPath}/SearchData" method="get">
        <label for="speciesSearch">Choose a Species:</label>
        <select name="speciesSearch" id="speciesSearch" required>
            <option value="" disabled selected>Select a Species</option>
            <option value="Bluegill">Bluegill</option>
            <option value="Brooke Trout">Brooke Trout</option>
            <option value="Brown Trout">Brown Trout</option>
            <option value="Carp">Carp</option>
            <option value="Catfish">Catfish</option>
            <option value="Crappie">Crappie</option>
            <option value="Largemouth Bass">Largemouth Bass</option>
            <option value="Lake Sturgeon">Lake Sturgeon</option>
            <option value="Muskellunge">Muskellunge</option>
            <option value="Northern Pike">Northern Pike</option>
            <option value="Rainbow Trout">Rainbow Trout</option>
            <option value="Shovelnose Sturgeon">Shovelnose Sturgeon</option>
            <option value="SmallMouth Bass">Smallmouth Bass</option>
            <option value="Sucker">Sucker</option>
            <option value="Yellow Perch">Yellow Perch</option>
        </select>
        <br><br>
        <input type="submit" value="Search Species">
    </form>


    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>