<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<link rel="stylesheet" href="<c:url value='/assets/css/styles.css' />">

<c:if test="${not empty sessionScope.user}">
    <div class="nav-user-info">
        <span class="nav-username">
            Logged in as: ${sessionScope.user.userName}
        </span>
        <a href="<c:url value='/Logout' />">Logout</a>
    </div>
</c:if>





