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

  <img src="<c:url value='/assets/img/temporaryLocation.jpg' />" alt="Lake Superior"><br><br>

  <form action="<%= request.getContextPath() %>/AddData" method="post">
    <label for="addLocation">Location:</label>
    <input type="text" id="addLocation" name="addLocation">
    <input type="submit" value="Add Location">
  </form>

  <br>

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
    <input type="submit" value="Search Locations">
  </form> <br><br>

  <%--table will need to be updated to use dyanmic info from the db--%>
  <%--table could also be removed entirely in opt of a different way--%>
  <table>
    <thead>
    <tr>
      <th>Location</th>
      <th>Water Type</th>
      <th>Notes</th>
    </tr>
    </thead>
    <tbody>
    <tr>
      <td>Chippewa River</td>
      <td>River</td>
      <td>Large River</td>
    </tr>
    <tr>
      <td>Lake Wissota</td>
      <td>Lake</td>
      <td>Large Lake</td>
    </tr>
    </tbody>
  </table>

  <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>