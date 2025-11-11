<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - SuccessfulAddition</title>
    <%@include file="layouts/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Data Added</h1>
    </div>

    <%@include file="layouts/navigation.jsp"%>

    <h1>Catch Added Successfully!</h1>
    <p>The information regarding your catch has been added to the database!.
        <a href="species.jsp">Enter another catch</a>
        or return
        <a href="index.jsp">Home</a>
    </p>

    <%@include file="layouts/footer.jsp"%>
</div>
</body>
</html>
