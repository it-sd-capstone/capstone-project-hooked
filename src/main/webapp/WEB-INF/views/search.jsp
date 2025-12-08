<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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

    <h4>
        To search, type the species, location, or bait you want to search by
        into the corresponding field.<br>
        Then click <strong>Search Catches</strong> to filter the results below.
    </h4>

    <form action="${pageContext.request.contextPath}/searchCatches" method="get">
        <label for="searchSpecies">Species:</label>
        <input type="text" id="searchSpecies" name="searchSpecies"
               placeholder="ex. walleye, bluegill"
               value="${fn:escapeXml(param.searchSpecies)}"> <br><br>

        <label for="searchLocation">Location:</label>
        <input type="text" id="searchLocation" name="searchLocation"
               placeholder="ex. Chippewa River"
               value="${fn:escapeXml(param.searchLocation)}"> <br><br>

        <label for="searchBait">Bait:</label>
        <input type="text" id="searchBait" name="searchBait"
               placeholder="ex. Jerkbait"
               value="${fn:escapeXml(param.searchBait)}"> <br><br>

        <label for="dateFrom">Date from:</label>
        <input type="date" id="dateFrom" name="dateFrom"
               value="${fn:escapeXml(param.dateFrom)}">

        <label for="dateTo">to:</label>
        <input type="date" id="dateTo" name="dateTo"
               value="${fn:escapeXml(param.dateTo)}"> <br><br>

        <input type="submit" value="Search Catches">

        <button type="button"
                onclick="window.location='${pageContext.request.contextPath}/searchCatches'">
            Clear Search
        </button>
    </form>

    <br><hr><br>

    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <h2>Search Results</h2>

    <a id="results"></a>   <%-- anchor for scrolling back here --%>

    <table>
        <thead>
        <tr>
            <%-- Species sort toggle --%>
            <c:set var="speciesDir"
                   value="${param.sort == 'species' and param.dir == 'asc' ? 'desc' : 'asc'}" />
            <c:url var="speciesUrl" value="/searchCatches">
                <c:param name="searchSpecies" value="${param.searchSpecies}" />
                <c:param name="searchLocation" value="${param.searchLocation}" />
                <c:param name="searchBait" value="${param.searchBait}" />
                <c:param name="dateFrom" value="${param.dateFrom}" />
                <c:param name="dateTo" value="${param.dateTo}" />
                <c:param name="sort" value="species" />
                <c:param name="dir" value="${speciesDir}" />
            </c:url>
            <th><a href="${speciesUrl}#results">Species</a></th>

            <%-- Length sort toggle --%>
            <c:set var="lengthDir"
                   value="${param.sort == 'length' and param.dir == 'asc' ? 'desc' : 'asc'}" />
            <c:url var="lengthUrl" value="/searchCatches">
                <c:param name="searchSpecies" value="${param.searchSpecies}" />
                <c:param name="searchLocation" value="${param.searchLocation}" />
                <c:param name="searchBait" value="${param.searchBait}" />
                <c:param name="dateFrom" value="${param.dateFrom}" />
                <c:param name="dateTo" value="${param.dateTo}" />
                <c:param name="sort" value="length" />
                <c:param name="dir" value="${lengthDir}" />
            </c:url>
            <th><a href="${lengthUrl}#results">Length (in)</a></th>

            <%-- Weight sort toggle --%>
            <c:set var="weightDir"
                   value="${param.sort == 'weight' and param.dir == 'asc' ? 'desc' : 'asc'}" />
            <c:url var="weightUrl" value="/searchCatches">
                <c:param name="searchSpecies" value="${param.searchSpecies}" />
                <c:param name="searchLocation" value="${param.searchLocation}" />
                <c:param name="searchBait" value="${param.searchBait}" />
                <c:param name="dateFrom" value="${param.dateFrom}" />
                <c:param name="dateTo" value="${param.dateTo}" />
                <c:param name="sort" value="weight" />
                <c:param name="dir" value="${weightDir}" />
            </c:url>
            <th><a href="${weightUrl}#results">Weight (lb)</a></th>

            <%-- Location sort toggle --%>
            <c:set var="locationDir"
                   value="${param.sort == 'location' and param.dir == 'asc' ? 'desc' : 'asc'}" />
            <c:url var="locationUrl" value="/searchCatches">
                <c:param name="searchSpecies" value="${param.searchSpecies}" />
                <c:param name="searchLocation" value="${param.searchLocation}" />
                <c:param name="searchBait" value="${param.searchBait}" />
                <c:param name="dateFrom" value="${param.dateFrom}" />
                <c:param name="dateTo" value="${param.dateTo}" />
                <c:param name="sort" value="location" />
                <c:param name="dir" value="${locationDir}" />
            </c:url>
            <th><a href="${locationUrl}#results">Location</a></th>

            <%-- Date sort toggle --%>
            <c:set var="dateDir"
                   value="${param.sort == 'date' and param.dir == 'asc' ? 'desc' : 'asc'}" />
            <c:url var="dateUrl" value="/searchCatches">
                <c:param name="searchSpecies" value="${param.searchSpecies}" />
                <c:param name="searchLocation" value="${param.searchLocation}" />
                <c:param name="searchBait" value="${param.searchBait}" />
                <c:param name="dateFrom" value="${param.dateFrom}" />
                <c:param name="dateTo" value="${param.dateTo}" />
                <c:param name="sort" value="date" />
                <c:param name="dir" value="${dateDir}" />
            </c:url>
            <th><a href="${dateUrl}#results">Date</a></th>

            <%-- Bait sort toggle --%>
            <c:set var="baitDir"
                   value="${param.sort == 'bait' and param.dir == 'asc' ? 'desc' : 'asc'}" />
            <c:url var="baitUrl" value="/searchCatches">
                <c:param name="searchSpecies" value="${param.searchSpecies}" />
                <c:param name="searchLocation" value="${param.searchLocation}" />
                <c:param name="searchBait" value="${param.searchBait}" />
                <c:param name="dateFrom" value="${param.dateFrom}" />
                <c:param name="dateTo" value="${param.dateTo}" />
                <c:param name="sort" value="bait" />
                <c:param name="dir" value="${baitDir}" />
            </c:url>
            <th><a href="${baitUrl}#results">Bait</a></th>

            <th>Notes</th>
        </tr>
        </thead>

        <tbody>
        <c:choose>
            <c:when test="${empty catches}">
                <tr>
                    <td colspan="7">No catches found.</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="c" items="${catches}">
                    <tr>
                        <td>${c.speciesName}</td>
                        <td>${c.length}</td>
                        <td>${c.weight}</td>
                        <td>${c.locationName}</td>
                        <td>${c.dateCaught}</td>
                        <td>${c.baitType}</td>
                        <td>${c.notes}</td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>
