<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ul class="nav nav-tabs">
  <li><a href="<c:url value='/IndexServlet'/>">Home</a></li>
  <li><a href="<c:url value='/locations'/>">Locations</a></li>
  <li><a href="<c:url value='/species'/>">Species</a></li>
  <li><a href="<c:url value='/bait'/>">Bait</a></li>
  <li><a href="<c:url value='/about'/>">About</a></li>
  <li><a href="<c:url value='/Login'/>">Login</a></li>
  <li><a href="<c:url value='/searchCatches'/>">Search</a></li>
  <li><a href="<c:url value='/addCatch'/>">Add Catch</a></li>
  <li><a href="<c:url value='/statistics'/>">Statistics</a></li>
  <c:if test="${sessionScope.isAdmin}">
    <li><a href="${pageContext.request.contextPath}/admin/catches">Admin Catches</a></li>
  </c:if>
</ul>
