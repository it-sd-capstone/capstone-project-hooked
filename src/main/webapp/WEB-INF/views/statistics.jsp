<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - Statistics</title>
    <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Statistics</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } else { %>

    <h3>The heaviest fish caught recently was:
        <% if (request.getAttribute("heaviestFish") != null) { %>
        <%= request.getAttribute("heaviestFish") %>
        at <%= request.getAttribute("heaviestWeight") %> lbs
        (caught at <%= request.getAttribute("heaviestLocation") %>
        on <%= request.getAttribute("heaviestDate") %>)
        <% } else { %>
        No data available yet
        <% } %>
    </h3>

    <h3>The longest fish caught recently was:
        <% if (request.getAttribute("longestFish") != null) { %>
        <%= request.getAttribute("longestFish") %>
        at <%= request.getAttribute("longestLength") %> inches
        (caught at <%= request.getAttribute("longestLocation") %>
        on <%= request.getAttribute("longestDate") %>)
        <% } else { %>
        No data available yet
        <% } %>
    </h3>

    <h3>The most productive location recently has been:
        <% if (request.getAttribute("topLocation") != null) { %>
        <%= request.getAttribute("topLocation") %>
        with <%= request.getAttribute("locationCount") %> catches
        <% } else { %>
        No data available yet
        <% } %>
    </h3>

    <h3>The most productive bait recently has been:
        <% if (request.getAttribute("topBait") != null) { %>
        <%= request.getAttribute("topBait") %>
        with <%= request.getAttribute("baitCount") %> catches
        <% } else { %>
        No data available yet
        <% } %>
    </h3>

    <% } %>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>