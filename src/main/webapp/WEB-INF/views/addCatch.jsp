<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
  <title>Hooked - Add Catch</title>
  <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

  <div class="header">
    <h1>Log your catch into the database!</h1>
  </div>

  <%@include file="/WEB-INF/includes/navigation.jsp"%>

  <h4>To log a catch, enter the information into the corresponding field.
   <br> When finished, click Add Catch and you will be directed to the successful entry page.
  </h4>

  <c:if test="${not empty error}">
    <p class="error">${error}</p>
  </c:if>

  <c:if test="${not empty success}">
    <p class="success">${success}</p>
  </c:if>

  <p id="speciesWarning" class="warning"></p>

  <form action="${pageContext.request.contextPath}/addCatch" method="post">

    <input type="hidden" name="catchId"
           value="${editCatch != null ? editCatch.catchId : ''}">

    <input type="hidden" name="userId" value="1">

    <label for="speciesName">Species:</label>
    <select id="speciesName" name="speciesName" required>
      <option value="" disabled
              <c:if test="${editCatch == null && empty param.speciesName}">selected</c:if>>
        Select a species
      </option>

      <c:forEach var="s" items="${speciesList}">
        <c:set var="selected"
               value="${(editCatch != null and editCatch.speciesName == s) or
                     (editCatch == null and param.speciesName == s)}" />
        <option value="${s}" <c:if test="${selected}">selected</c:if>>
            ${s}
        </option>
      </c:forEach>
    </select>
    <br><br>

    <label for="length">Length (inches):</label>
    <input type="number" step=".01" min="0" id="length" name="length" placeholder="ex. 11.00" value="${editCatch != null ? editCatch.length : param.length}" required><br><br>

    <label for="weight">Weight (pounds):</label>
    <input type="number" step=".01" min="0" id="weight" name="weight" placeholder="ex. 1.00" value="${editCatch != null ? editCatch.weight : param.weight}" required><br><br>

    <label for="locationName">Location:</label>
    <input type="text" id="locationName" name="locationName" placeholder="ex. Mississippi River" value="${editCatch != null ? editCatch.locationName : param.locationName}" required><br><br>

    <label for="baitType">Bait:</label>
    <input type="text" id="baitType" name="baitType" placeholder="ex. Gulp! Minnow" value="${editCatch != null ? editCatch.baitType : param.baitType}" required><br><br>

    <label for="dateCaught">Date Caught (YYYY-MM-DD):</label>
    <input type="date" id="dateCaught" name="dateCaught" placeholder="ex. 2025-09-15" value="${editCatch != null ? editCatch.dateCaught : param.dateCaught}"><br><br>

    <label for="notes">Notes:</label>
    <input type="text" id="notes" name="notes" placeholder="ex. Slip Bobber" value="${editCatch != null ? editCatch.notes : param.notes}"><br><br>

    <input type="submit" value="${editCatch != null ? 'Update Catch' : 'Add Catch'}">

  </form>

  <br><hr><br>

  <!-- Table of existing catches -->
  <c:if test="${not empty catches}">
    <h2>Your Logged Catches</h2>
    <table>
      <thead>
      <tr>
        <th>Species</th>
        <th>Length (in)</th>
        <th>Weight (lb)</th>
        <th>Location</th>
        <th>Date</th>
        <th>Bait</th>
        <th>Notes</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="c" items="${catches}">
        <tr>
          <td>${c.speciesName}</td>
          <td>${c.length}</td>
          <td>${c.weight}</td>
          <td>${c.locationName}</td>
          <td>${c.dateCaught}</td>
          <td>${c.baitType}</td>
          <td>${c.notes}</td>
          <td class="actions-cell">
            <div class="actions-wrapper">
              <!-- Edit button -->
              <a href="${pageContext.request.contextPath}/addCatch?editId=${c.catchId}"
                 class="btn-action btn-edit">
                Edit
              </a>

              <!-- Delete button -->
              <form action="${pageContext.request.contextPath}/deleteCatch"
                    method="post"
                    class="inline-form"
                    onsubmit="return confirm('Are you sure you want to delete this catch?');">
                <input type="hidden" name="catchId" value="${c.catchId}">
                <button type="submit" class="btn-action btn-delete">
                  Delete
                </button>
              </form>
            </div>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:if>

  <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>
