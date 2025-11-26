<%--
  Created by IntelliJ IDEA.
  User: Mike.Truscott
  Date: 11/26/2025
  Time: 3:08 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked – Admin Catches</title>
    <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Admin – All Catches</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <!-- Clear-all button -->
    <form action="${pageContext.request.contextPath}/admin/catches"
          method="post"
          onsubmit="return confirm('This will delete ALL catches in the database. Are you sure?');">
        <input type="hidden" name="action" value="clearAll">
        <input type="submit" value="Clear ALL Catches">
    </form>

    <br>

    <table>
        <thead>
        <tr>
            <th>CatchID</th>
            <th>UserID</th>
            <th>Species</th>
            <th>Length</th>
            <th>Weight</th>
            <th>Location</th>
            <th>Date</th>
            <th>Bait</th>
            <th>Notes</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty catches}">
                <tr><td colspan="10">No catches found.</td></tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="c" items="${catches}">
                    <tr>
                        <td>${c.catchId}</td>
                        <td>${c.userId}</td>
                        <td>${c.speciesName}</td>
                        <td>${c.length}</td>
                        <td>${c.weight}</td>
                        <td>${c.locationName}</td>
                        <td>${c.dateCaught}</td>
                        <td>${c.baitType}</td>
                        <td>${c.notes}</td>
                        <td>
                            <!-- Delete single catch -->
                            <form action="${pageContext.request.contextPath}/admin/catches"
                                  method="post"
                                  style="display:inline"
                                  onsubmit="return confirm('Delete this catch?');">
                                <input type="hidden" name="action" value="deleteOne">
                                <input type="hidden" name="catchId" value="${c.catchId}">
                                <input type="submit" value="Delete">
                            </form>

                            <!-- Optional: link to edit via AddCatch page -->
                            <a href="${pageContext.request.contextPath}/addCatch?editId=${c.catchId}">
                                Edit as that user
                            </a>

                        </td>
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

