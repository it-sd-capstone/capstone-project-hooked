<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

  <img src="<c:url value='/assets/img/temporaryBait.jpg' />" alt="tackle box"><br><br>

  <form action="<%= request.getContextPath() %>/AddData" method="post">
    <label for="addBait">Bait:</label>
    <input type="text" id="addBait" name="addBait">
    <input type="submit" value="Add Bait">
  </form>

  <br>

  <form action="${pageContext.request.contextPath}/SearchData" method="get">
    <label for="baitSearch">Choose a Bait:</label>
    <select name="baitSearch" id="baitSearch" required>
      <option value="" disabled selected>Select a Bait</option>
      <option value="Crankbait">Crankbait</option>
      <option value="Cut fish">Cut fish</option>
      <option value="Inline Spinner">Inline Spinner</option>
      <option value="Leeches">Leeches</option>
      <option value="Live fish">Live fish</option>
      <option value="Minnows">Minnows</option>
      <option value="Nightcrawler">Nightcrawler</option>
      <option value="Soft plastic crawfish">Soft plastic crawfish</option>
      <option value="Soft plastic creature">Soft plastic creature</option>
      <option value="Soft plastic minnow">Soft plastic minnow</option>
      <option value="Soft plastic panfish">Soft plastic panfish</option>
      <option value="Soft plastic worm">Soft plastic worm</option>
      <option value="Spoon">Spoon</option>
      <option value="Swimbait">Swimbait</option>
      <option value="Topwater">Topwater</option>
    </select>
    <input type="submit" value="Search Baits">
  </form> <br><br>

  <%--table will need to be updated to use dyanmic info from the db--%>
  <%--table could also be removed entirely in opt of a different way--%>
  <table>
    <thead>
      <tr>
        <th>Bait</th>
        <th>Notes</th>
      </tr>
    </thead>
    <tbody>
    <tr>
      <td>Nightcrawler</td>
      <td>Carolina Rig</td>
    </tr>
    <tr>
      <td>Plastic Worm</td>
      <td>Wacky</td>
    </tr>
    </tbody>


  </table>

  <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>