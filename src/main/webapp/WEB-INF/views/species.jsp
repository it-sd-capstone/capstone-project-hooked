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
            Species Request added successfully!
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
               placeholder="ex. Smallmouth Bass" required>

        <input type="submit" value="Request New Species">

    </form>

    <br>

    <form action="${pageContext.request.contextPath}/species#speciesTable"" method="get">
    <c:choose>
        <c:when test="${empty currentSpeciesSearch}">
            <!-- No active search: show dropdown -->
            <label for="speciesSearch">Choose a Species:</label>
            <select name="speciesSearch" id="speciesSearch" required>
                <option value="" disabled selected>
                    Select a Species
                </option>

                <c:forEach var="name" items="${speciesNames}">
                    <option value="${name}">${name}</option>
                </c:forEach>
            </select>
        </c:when>

        <c:otherwise>
            <!-- Search active: hide dropdown, show current filter text -->
            <p><strong>Currently viewing:</strong> ${currentSpeciesSearch}</p>
        </c:otherwise>
    </c:choose>


    <c:choose>
            <c:when test="${not empty currentSpeciesSearch}">
                <!-- We are currently filtered: show a Clear button -->
                <button type="submit" name="clear" value="1">
                    Clear Search
                </button>
            </c:when>
            <c:otherwise>
                <!-- No active search: show Search button -->
                <input type="submit" value="Search Species">
            </c:otherwise>
        </c:choose>
    </form> <br><br>

    <a id="speciesTable"></a>
    <h2>Supported Species & Maximum Recorded Size</h2>

    <table>
        <thead>
        <tr>
            <th>Species</th>
            <th>Max Length (in)</th>
            <th>Max Weight (lb)</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="entry" items="${restrictionList}">
            <tr>
                <td>${entry.key}</td>
                <td>${entry.value.maxLength}</td>
                <td>${entry.value.maxWeight}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>