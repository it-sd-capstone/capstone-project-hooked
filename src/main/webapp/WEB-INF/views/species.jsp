<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

        <label for="addLength">Length (inches):</label>
        <input type="number" step="0.1" id="addLength" name="addLength">

        <label for="addWeight">Weight (lbs):</label>
        <input type="number" step="0.1" id="addWeight" name="addWeight">

        <input type="submit" value="Add Species">

    </form>

    <br>

    <form action="${pageContext.request.contextPath}/SearchData" method="get">
        <label for="speciesSearch">Choose a Species:</label>
        <select name="speciesSearch" id="speciesSearch" required>
            <option value="" disabled selected>Select a Species</option>

            <c:forEach var="s" items="${speciesList}">
                <option value="${s.speciesName}">
                        ${s.speciesName}
                </option>
            </c:forEach>
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
        <c:forEach var="s" items="${speciesList}">
            <tr>
                <td>${s.speciesName}</td>

                <td>
                    <c:choose>
                        <c:when test="${s.length != null}">
                            ${s.length}
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>

                <td>
                    <c:choose>
                        <c:when test="${s.weight != null}">
                            ${s.weight}
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>

                <!-- empty placeholder cells to match header -->
                <td>-</td>
                <td>-</td>
                <td>-</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>


    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>