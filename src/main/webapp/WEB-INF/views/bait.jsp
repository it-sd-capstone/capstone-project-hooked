<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Hooked - Bait</title>
  <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

  <div class="header">
    <h1>Bait</h1>
  </div>

  <%@include file="/WEB-INF/includes/navigation.jsp"%>

  <img src="<c:url value='/assets/img/temporaryBait.jpg' />"
       alt="tackle box"
       class="page-image"><br><br>

  <a id="baitForm"></a>

  <!-- Add / Update form (same pattern as species) -->
  <form action="${pageContext.request.contextPath}/bait" method="post">
    <c:choose>
      <c:when test="${not empty baitToEdit}">
        <input type="hidden" name="action" value="update" />
        <input type="hidden" name="baitId" value="${baitToEdit.id}" />
      </c:when>
      <c:otherwise>
        <input type="hidden" name="action" value="create" />
      </c:otherwise>
    </c:choose>

    <label for="name">Bait:</label>
    <input type="text"
           id="name"
           name="name"
           value="${not empty baitToEdit ? baitToEdit.name : ''}"
           placeholder="ex. Nightcrawler"
           required />

    <label for="notes">Notes:</label>
    <input type="text"
           id="notes"
           name="notes"
           value="${not empty baitToEdit ? baitToEdit.notes : ''}"
           placeholder="ex. Live Bait"/>

    <input type="submit"
           value="${not empty baitToEdit ? 'Update Bait' : 'Add Bait'}" />
  </form>

  <br/>

  <c:if test="${not empty error}">
    <div class="error-message">${error}</div>
  </c:if>
  <c:if test="${not empty success}">
    <div class="success-message">${success}</div>
  </c:if>

  <a id="baitTable"></a>
  <h2>Bait List</h2>

  <table>
    <thead>
    <tr>
      <th>
        <a href="${pageContext.request.contextPath}/bait?sort=${sortOrder == 'asc' ? 'desc' : 'asc'}#baitTable">
          Bait Name
          <c:choose>
            <c:when test="${sortOrder == 'asc'}">▲</c:when>
            <c:otherwise>▼</c:otherwise>
          </c:choose>
        </a>
      </th>
      <th>Notes</th>
      <th>Added By</th>
      <c:if test="${sessionScope.isAdmin}">
        <th>Actions</th>
      </c:if>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="b" items="${baitList}">
      <tr>
        <td>${b.name}</td>
        <td>${b.notes}</td>
        <td>
          <c:choose>
            <c:when test="${not empty b.createdByUserId}">
              User ID ${b.createdByUserId}
            </c:when>
            <c:otherwise>(preloaded)</c:otherwise>
          </c:choose>
        </td>

        <c:if test="${sessionScope.isAdmin}">
          <td>
            <!-- Edit: reloads page with baitToEdit -->
            <a href="${pageContext.request.contextPath}/bait?editId=${b.id}#baitForm">
              Edit
            </a>
            |
            <!-- Delete: POST with action=delete -->
            <form action="${pageContext.request.contextPath}/bait#baitTable"
                  method="post"
                  style="display:inline">
              <input type="hidden" name="action" value="delete" />
              <input type="hidden" name="baitId" value="${b.id}" />
              <button type="submit"
                      onclick="return confirm('Are you sure you want to delete this bait?');">
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
