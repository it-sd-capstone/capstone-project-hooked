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

    <img src="<c:url value='/assets/img/temporarySpecies.jpg' />" alt="sturgeon image" class="page-image"><br><br>

    <form action="${pageContext.request.contextPath}/species" method="post">

        <label for="addSpecies">Species Name:</label>
        <input type="text" id="addSpecies" name="addSpecies" required>

        <label for="minLength">Min Length (inches):</label>
        <input type="number" step="0.1" id="minLength" name="minLength">

        <label for="maxLength">Max Length (inches):</label>
        <input type="number" step="0.1" id="maxLength" name="maxLength">

        <label for="minWeight">Min Weight (lbs):</label>
        <input type="number" step="0.1" id="minWeight" name="minWeight">

        <label for="maxWeight">Max Weight (lbs):</label>
        <input type="number" step="0.1" id="maxWeight" name="maxWeight">

        <input type="submit" value="Add Species">

    </form>

    <br>

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
        <input type="submit" value="Search Species">
    </form> <br><br>


    <%--table will need to be update to use dyanmic info from the db--%>
    <%--table could also be removed entirely in opt of a different way--%>
    <table>
        <thead>
        <tr>
            <th>Species</th>
            <th>Length</th>
            <th>Weight(lbs)</th>
            <th>Location</th>
            <th>Bait Used</th>
            <th>Notes</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>Bluegill</td>
            <td>11 inches</td>
            <td>1 lb</td>
            <td>Mississippi River</td>
            <td>Gulp! Minnow</td>
            <td>Slip Bobber</td>
        </tr>
        <tr>
            <td>ShovelNose Sturgeon</td>
            <td>27 inches</td>
            <td>5 lbs</td>
            <td>Chippewa River</td>
            <td>Nightcrawler</td>
            <td>Fishing the edge of an eddy</td>
        </tr>
        </tbody>
    </table>


    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>