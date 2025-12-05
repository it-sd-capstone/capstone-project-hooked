<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked</title>
    <%-- header will need to be changed to non-generic too match wireframe --%>
    <%@include file="/WEB-INF/includes/header.jsp"%>

</head>
<body>
<div class="container">

    <div class="header">
        <h1>Welcome to Hooked</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <div class="content">
    <img class="home-image" src="<c:url value='/assets/img/walleye-1.jpg' />" alt="walleye image">

        <h2>Your Fishing Log, All in One Place</h2>

        <p>
            Hooked is a simple and organized place to log your fishing adventures.
            Track each catch by species, length, weight, bait, and location all in one easy-to-use dashboard.
        </p>

        <p>
            Use the search tools to find specific catches by species, bait, or location.
            View your statistics to spot patterns and monitor your progress over time.
        </p>

        <p>
            Whether you're a seasoned angler or just starting out, Hooked helps you stay organized
            and make the most of your time on the water.
        </p>


    <%-- could put user instructions here, or repeat on every page. --%>

</div>

<%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>