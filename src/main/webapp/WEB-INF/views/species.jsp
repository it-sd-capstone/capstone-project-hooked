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

    <a id="speciesTableOne"></a>

    <form action="${pageContext.request.contextPath}/species" method="post">
        <c:if test="${editing}">
            <input type="hidden" name="speciesId" value="${editSpecies.speciesId}"/>
        </c:if>

        <label for="addSpecies">Species Name:</label>
        <input type="text" id="addSpecies" name="addSpecies"
               placeholder="ex. Smallmouth Bass"
               value="${editing ? editSpecies.speciesName : ''}"
               required>

        <label for="maxLength">Max Length (in):</label>
        <input type="text" id="maxLength" name="maxLength"
               placeholder="ex. 24.0"
               value="${editing ? editSpecies.maxLength : ''}"
               required>

        <label for="maxWeight">Max Weight (lb):</label>
        <input type="text" id="maxWeight" name="maxWeight"
               placeholder="ex. 10.0"
               value="${editing ? editSpecies.maxWeight : ''}"
               required>

        <c:choose>
            <c:when test="${editing}">
                <input type="submit" class="btn" value="Save Changes">
                <a href="${pageContext.request.contextPath}/species#speciesTable">Cancel</a>
            </c:when>
            <c:otherwise>
                <input type="submit" class="btn" value="Add Species">
            </c:otherwise>
        </c:choose>
    </form>

    <br>

    <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
    </c:if>

    <c:if test="${not empty success}">
        <div class="success-message">${success}</div>
    </c:if>

    <c:set var="editing" value="${not empty editSpecies}" />

    <a id="speciesTable"></a>
    <h2>Supported Species & Maximum Recorded Size</h2>

    <table>
        <thead>
        <tr>
            <%-- Species sort toggle --%>
            <c:set var="speciesDir"
                   value="${param.sort == 'species' and param.dir == 'asc' ? 'desc' : 'asc'}" />
            <c:url var="speciesUrl" value="/species">
                <c:param name="sort" value="species"/>
                <c:param name="dir"  value="${speciesDir}"/>
            </c:url>
            <th><a href="${speciesUrl}#speciesTable">Species</a></th>

            <%-- Max Length sort toggle --%>
            <c:set var="lengthDir"
                   value="${param.sort == 'length' and param.dir == 'asc' ? 'desc' : 'asc'}" />
            <c:url var="lengthUrl" value="/species">
                <c:param name="sort" value="length"/>
                <c:param name="dir"  value="${lengthDir}"/>
            </c:url>
            <th><a href="${lengthUrl}#speciesTable">Max Length (in)</a></th>

            <%-- Max Weight sort toggle --%>
            <c:set var="weightDir"
                   value="${param.sort == 'weight' and param.dir == 'asc' ? 'desc' : 'asc'}" />
            <c:url var="weightUrl" value="/species">
                <c:param name="sort" value="weight"/>
                <c:param name="dir"  value="${weightDir}"/>
            </c:url>
            <th><a href="${weightUrl}#speciesTable">Max Weight (lb)</a></th>

            <th>Added By</th>

            <c:if test="${sessionScope.isAdmin}">
                <th>Admin</th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="s" items="${speciesList}">
            <tr>
                <td>${s.speciesName}</td>
                <td>${s.maxLength}</td>
                <td>${s.maxWeight}</td>
                <td>
                    <c:choose>
                        <c:when test="${not empty s.createdByUserId}">
                            <c:set var="userName" value="${createdByNames[s.createdByUserId]}" />
                            <c:choose>
                                <c:when test="${not empty userName}">
                                    ${userName}
                                </c:when>
                                <c:otherwise>
                                    (unknown user)
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>(preloaded)</c:otherwise>
                    </c:choose>
                </td>


                <c:if test="${sessionScope.isAdmin}">
                    <td>
                        <a href="${pageContext.request.contextPath}/species?editId=${s.speciesId}#speciesTable">
                            Edit
                        </a>
                        |
                        <a href="${pageContext.request.contextPath}/species?deleteId=${s.speciesId}#speciesTable"
                           onclick="return confirm('Are you sure you want to delete this species?');">
                            Delete
                        </a>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>


    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>