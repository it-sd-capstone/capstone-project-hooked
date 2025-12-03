<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ul class="nav nav-tabs">
    <li><a href="<c:url value='/IndexServlet'/>">Home</a></li>
    <li><a href="<c:url value='/about'/>">About</a></li>
    <li><a href="<c:url value='/statistics'/>">Statistics</a></li>
    <li><a href="<c:url value='/searchCatches'/>">Search</a></li>

    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <!-- only when logged in -->
            <li><a href="<c:url value='/locations'/>">Locations</a></li>
<%--            <li><a href="<c:url value='/species'/>">Species</a></li>--%>
<%--            <li><a href="<c:url value='/bait'/>">Bait</a></li>--%>
            <li><a href="<c:url value='/searchCatches'/>">Search</a></li>
            <li><a href="<c:url value='/addCatch'/>">Add Catch</a></li>

            <c:if test="${sessionScope.isAdmin}">
                <li><a href="${pageContext.request.contextPath}/admin/catches">Admin Catches</a></li>
            </c:if>

            <!-- User info and logout on the right side -->
            <li style="float: right;"><a href="<c:url value='/Logout'/>">Logout</a></li>
            <li style="float: right; line-height: normal; display: flex; align-items: center;">
                <span style="color: white; padding: 0 15px;">
                    Logged in as: <strong>${sessionScope.user.userName}</strong>
                </span>
            </li>
        </c:when>
        <c:otherwise>
            <!-- only when logged out -->
            <li><a href="<c:url value='/Login'/>">Login</a></li>
            <li><a href="<c:url value='/Register'/>">Create Account</a></li>
        </c:otherwise>
    </c:choose>
</ul>