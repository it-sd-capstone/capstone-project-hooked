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

    <label for="speciesName">Species:</label>
    <select id="speciesName" name="speciesName" required>
      <option value="" disabled
              <c:if test="${empty param.speciesName}">selected</c:if>>
        Select a species
      </option>

      <c:forEach var="s" items="${speciesList}">
        <option value="${s}"
                <c:if test="${param.speciesName == s}">selected</c:if>>
            ${s}
        </option>
      </c:forEach>
    </select>
    <br><br>

    <label for="length">Length (inches):</label>
    <input type="number" step=".01" min="0" id="length" name="length" placeholder="ex. 11.00" value="${param.length}" required><br><br>

    <label for="weight">Weight (pounds):</label>
    <input type="number" step=".01" min="0" id="weight" name="weight" placeholder="ex. 1.00" value="${param.weight}" required><br><br>

    <label for="locationName">Location:</label>
    <input type="text" id="locationName" name="locationName" placeholder="ex. Mississippi River" value="${param.locationName}" required><br><br>

    <label for="baitType">Bait:</label>
    <input type="text" id="baitType" name="baitType" placeholder="ex. Gulp! Minnow" value="${param.baitType}" required><br><br>

    <label for="dateCaught">Date Caught (YYYY-MM-DD):</label>
    <input type="date" id="dateCaught" name="dateCaught" placeholder="ex. 2025-09-15" value="${param.dateCaught}"><br><br>

    <label for="notes">Notes:</label>
    <input type="text" id="notes" name="notes" placeholder="ex. Slip Bobber" value="${param.notes}"><br><br>

    <input type="submit" value="Add Catch">

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
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:if>

  <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>
