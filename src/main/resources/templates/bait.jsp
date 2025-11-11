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

    <img src="Images/imagename.jpg" alt="Image description"><br><br>

    <form action="<%= request.getContextPath() %>/AddData" method="post">
        <label for="addBait">Bait:</label>
        <input type="text" id="addBait" name="addBait"> <br><br>
        <input type="submit" value="Add Bait">
    </form>

    <br>

<%--
    <form action="${pageContext.request.contextPath}/SearchData" method="get">
        <label for="searchBait">Search by Bait:</label>
        <input type="text" name="searchBait" id="searchBait">
        <br><br>
        <input type="submit">
    </form>
--%>

    <form action="${pageContext.request.contextPath}/SearchData" method="get">
        <label for="baitSearch">Choose a Location:</label>
        <select name="baitSearch" id="baitSearch" required>
            <option value="" disabled selected>Select a Bait</option>
            <option value="Crankbait">Crankbait</option>
            <option value="Cut fish">Cut fish</option>
            <option value="Inline Spinner">Inline Spinner</option>
            <option value="Leeches">Leeches</option>
            <option value="Live fish">Live fish</option>
            <option value="Minnows">Minnows</option>
            <option value="Nightcrawler">Nightcrawler</option>
            <option value="Soft plastic crawfish">Soft plastic crawfish</option>
            <option value="Soft plastic creature">Soft plastic creature</option>
            <option value="Soft plastic minnow">Soft plastic minnow</option>
            <option value="Soft plastic panfish">Soft plastic panfish</option>
            <option value="Soft plastic worm">Soft plastic worm</option>
            <option value="Spoon">Spoon</option>
            <option value="Swimbait">Swimbait</option>
            <option value="Topwater">Topwater</option>
        </select>
        <br><br>
        <input type="submit" value="Search Baits">
    </form>

    <%@include file="layouts/footer.jsp"%>
</div>
</body>
</html>