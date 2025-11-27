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

    <c:if test="${not empty param.error}">
        <div class="error-message" style="color:red; font-weight:bold;">
                ${param.error}
        </div>
    </c:if>

    <c:if test="${not empty param.success}">
        <div class="success-message" style="color:green; font-weight:bold;">
            Species added successfully!
        </div>
    </c:if>

    <c:if test="${not empty param.formatted}">
        <div class="info-message" style="color:#0066cc; font-weight:bold;">
            Auto-formatted to: <strong>${param.formatted}</strong>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/species" method="post">

        <label for="addSpecies">Species Name:</label>

        <input type="text" id="addSpecies" name="addSpecies"
               value="${param.formatted}" required>

        <input type="submit" value="Add Species">

    </form>

    <br>

    <form action="${pageContext.request.contextPath}/SearchData" method="get">
        <label for="speciesSearch">Choose a Species:</label>
        <select name="speciesSearch" id="speciesSearch" required>
            <option value="" disabled selected>Select a Species</option>

            <c:forEach var="s" items="${speciesList}">
                <option value="${s.speciesName}"
                        <c:if test="${param.speciesSearch == s.speciesName}">selected</c:if>>
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
        </tr>
        </thead>
        <tbody>
        <c:forEach var="s" items="${speciesList}">
            <tr>
                <td>${s.speciesName}</td>

            </tr>
        </c:forEach>
        </tbody>
    </table>

    <c:if test="${not empty catchesForSpecies}">
        <h2>Catches for this Species</h2>

        <table>
            <thead>
            <tr>
                <th>Length (in)</th>
                <th>Weight (lbs)</th>
                <th>Location</th>
                <th>Date</th>
                <th>Bait</th>
                <th>Notes</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="c" items="${catchesForSpecies}">
                <tr>
                    <td>${c.length}</td>
                    <td>${c.weight}</td>
                    <td>${c.locationName}</td>
                    <td>${c.dateCaught}</td>
                    <td>${c.baitType}</td>
                    <td>${c.notes}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>