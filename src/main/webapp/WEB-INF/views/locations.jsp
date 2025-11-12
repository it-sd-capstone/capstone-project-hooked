<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

  <img src="Images/imagename.jpg" alt="Image description"><br><br>

  <form action="<%= request.getContextPath() %>/AddData" method="post">
    <label for="addLocation">Location:</label>
    <input type="text" id="addLocation" name="addLocation"> <br><br>
    <input type="submit" value="Add Location">
  </form>

  <br>

  <%--
      <form action="${pageContext.request.contextPath}/SearchData" method="get">
          <label for="searchLocation">Search by Location:</label>
          <input type="text" name="searchLocation" id="searchLocation">
          <br><br>
          <input type="submit">
      </form>
  --%>

  <form action="${pageContext.request.contextPath}/SearchData" method="get">
    <label for="locationSearch">Choose a Location:</label>
    <select name="locationSearch" id="locationSearch" required>
      <option value="" disabled selected>Select a Location</option>
      <option value="Altoona Lake">Altoona Lake</option>
      <option value="Dells Pond">Dells Pond</option>
      <option value="Halfmoon Lake">Halfmoon Lake</option>
      <option value="Lake Eau Claire">Lake Eau Claire</option>
      <option value="Lake Hallie">Lake Hallie</option>
      <option value="Lake Wissota">Lake Wissota</option>
      <option value="Old Abe Lake">Old Abe Lake</option>
      <option value="Red Cedar River">Red Cedar River</option>
      <option value="Tainter Lake">Tainter Lake</option>
      <option value="Chippewa River">Chippewa River</option>
      <option value="Mississippi River">Mississippi River</option>
    </select>
    <br><br>
    <input type="submit" value="Search Locations">
  </form>

  <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>