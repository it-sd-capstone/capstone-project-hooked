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
        <h1>Admin – All Data</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <!-- Toggle button: switches between catches view and users view -->
    <form action="${pageContext.request.contextPath}/admin/catches" method="get">
        <c:choose>
            <c:when test="${view == 'users'}">
                <input type="hidden" name="view" value="catches">
                <input type="submit" value="Catches">
            </c:when>
            <c:otherwise>
                <input type="hidden" name="view" value="users">
                <input type="submit" value="Users">
            </c:otherwise>
        </c:choose>
    </form>
    <br>

    <c:choose>
        <c:when test="${view == 'users'}">
            <!-- USERS TABLE -->
            <h2>Registered Users</h2>
            <table>
                <thead>
                <tr>
                    <th>UserID</th>
                    <th>Username</th>
                    <!-- optional:
                    <th>First Name</th>
                    <th>Last Name</th>
                    -->
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty users}">
                        <tr><td colspan="2">No users found.</td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td>${u.userId}</td>
                                <td>${u.userName}</td>
                                    <%-- optional:
                                    <td>${u.firstName}</td>
                                    <td>${u.lastName}</td>
                                    --%>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </c:when>

        <c:otherwise>
            <!-- CATCHES TABLE (current behavior) -->
            <h2>All Catches</h2>

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
                                    <!-- delete single catch -->
                                    <form action="${pageContext.request.contextPath}/admin/catches"
                                          method="post"
                                          style="display:inline"
                                          onsubmit="return confirm('Delete this catch?');">
                                        <input type="hidden" name="action" value="deleteOne">
                                        <input type="hidden" name="catchId" value="${c.catchId}">
                                        <input type="submit" value="Delete">
                                    </form>

                                    <!-- edit as admin (your existing link) -->
                                    <a href="${pageContext.request.contextPath}/addCatch?editId=${c.catchId}">
                                        Edit
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <h2>Requested Species</h2>

    <table>
        <thead>
        <tr>
            <th>RequestID</th>
            <th>Species Name</th>
            <th>UserID</th>
            <th>Requested At</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty speciesRequests}">
                <tr>
                    <td colspan="5">No species requests yet.</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="r" items="${speciesRequests}">
                    <tr>
                        <td>${r.requestId}</td>
                        <td>${r.speciesName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${r.userId != null}">
                                    ${r.userId}
                                </c:when>
                                <c:otherwise>
                                    (guest)
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${r.requestedAt}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/catches"
                                  method="post"
                                  style="display:inline"
                                  onsubmit="return confirm('Delete this species request?');">
                                <input type="hidden" name="action" value="deleteSpeciesRequest">
                                <input type="hidden" name="requestId" value="${r.requestId}">
                                <input type="hidden" name="view" value="${view}">
                                <input type="submit" value="Delete">
                            </form>
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

