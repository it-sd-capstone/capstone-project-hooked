<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

  <form action="${pageContext.request.contextPath}/bait" method="post">
    <label for="addBait">Bait:</label>
    <input type="text" id="addBait" name="addBait" required>

    <label for="notes">Notes:</label>
    <input type="text" id="notes" name="notes">

    <input type="submit" value="Add Bait">
  </form>

  <br>

  <%-- <form action="${pageContext.request.contextPath}/SearchData" method="get">
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
  </form> <br><br> --%>

  <!-- Search form using dynamic bait list -->
  <form action="${pageContext.request.contextPath}/bait" method="get">
    <label for="searchTerm">Search Baits:</label>
    <input type="text" id="searchTerm" name="searchTerm"
           value="${param.searchTerm}" placeholder="e.g. worm, crawler, minnow">

    <input type="submit" value="Search Baits">

    <!-- Clear search: just go back to /bait with no query -->
    <a href="${pageContext.request.contextPath}/bait">Clear Search</a>
  </form> <br><br>

  <c:if test="${searchActive}">
    <p>Showing results matching: "<strong>${fn:escapeXml(param.searchTerm)}</strong>"</p>
  </c:if>
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
    <c:forEach var="b" items="${baits}">
      <tr>
        <td>${b.name}</td>
        <td>${b.notes}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

  <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>