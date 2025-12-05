<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Hooked - Locations</title>
  <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

  <div class="header">
    <h1>Locations</h1>
  </div>

  <%@include file="/WEB-INF/includes/navigation.jsp"%>

  <a id="locationFormOne"></a>

  <!-- Add / Update form -->
  <form action="${pageContext.request.contextPath}/location" method="post">
    <c:choose>
      <c:when test="${not empty locationToEdit}">
        <input type="hidden" name="action" value="update" />
        <input type="hidden" name="locationId" value="${locationToEdit.locationId}" />
      </c:when>
      <c:otherwise>
        <input type="hidden" name="action" value="create" />
      </c:otherwise>
    </c:choose>

    <label for="locationName">Location:</label>
    <input type="text"
           id="locationName"
           name="locationName"
           value="${not empty locationToEdit ? locationToEdit.locationName : ''}"
           placeholder="ex. Chippewa River"
           required />

    <label for="state">State (2-letter):</label>
    <input type="text"
           id="state"
           name="state"
           maxlength="2"
           value="${not empty locationToEdit ? locationToEdit.state : ''}"
           placeholder="ex. WI"
           required />

    <input type="submit"
           value="${not empty locationToEdit ? 'Update Location' : 'Add Location'}" />
  </form>

  <br/>

  <c:if test="${not empty error}">
    <div class="error-message">${error}</div>
  </c:if>
  <c:if test="${not empty success}">
    <div class="success-message">${success}</div>
  </c:if>

  <a id="locationTable"></a>
  <h2>Location List</h2>

  <table>
    <thead>
    <tr>
      <th>
        <a href="${pageContext.request.contextPath}/location?sort=${sortOrder == 'asc' ? 'desc' : 'asc'}#locationTable">
          Location Name
          <c:choose>
            <c:when test="${sortOrder == 'asc'}">▲</c:when>
            <c:otherwise>▼</c:otherwise>
          </c:choose>
        </a>
      </th>
      <th>State</th>
      <th>Added By</th>
      <c:if test="${sessionScope.isAdmin}">
        <th>Actions</th>
      </c:if>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="loc" items="${locationList}">
      <tr>
        <td>${loc.locationName}</td>
        <td>${loc.state}</td>
        <td>
          <c:choose>
            <c:when test="${not empty loc.createdByUserId}">
              User ID ${loc.createdByUserId}
            </c:when>
            <c:otherwise>(preloaded)</c:otherwise>
          </c:choose>
        </td>

        <c:if test="${sessionScope.isAdmin}">
          <td>
            <!-- Edit: reload page with locationToEdit -->
            <a href="${pageContext.request.contextPath}/location?editId=${loc.locationId}#locationForm">
              Edit
            </a>
            |
            <!-- Delete: POST with action=delete -->
            <form action="${pageContext.request.contextPath}/location#locationTable"
                  method="post"
                  style="display:inline">
              <input type="hidden" name="action" value="delete" />
              <input type="hidden" name="locationId" value="${loc.locationId}" />
              <button type="submit"
                      onclick="return confirm('Are you sure you want to delete this location?');">
                Delete
              </button>
            </form>
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
