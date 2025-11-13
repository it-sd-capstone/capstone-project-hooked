<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Hooked - Logout</title>
  <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

  <div class="header">
    <h1>Logout</h1>
  </div>

  <p>You have been logged out Successfully.</p>

    <a href="<%= request.getContextPath() %>/Login" class="btn">Login Again</a>
    <a href="index.jsp" class="btn">Return Home</a>

    <%@include file="/WEB-INF/includes/footer.jsp"%>

</div>
</body>
</html>