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
        <h1>Fishing Statistics</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <% if (request.getAttribute("error") != null) { %>
    <div style="color: #fecaca; padding: 10px; margin: 10px 0; border: 1px solid var(--border-soft); background-color: var(--bg-card); border-radius: 10px;">
        <strong>Error:</strong> <%= request.getAttribute("error") %>
    </div>
    <% } else { %>

    <div class="stats-container">

        <!-- top catches by weight and length -->
        <div class="stats-section">
            <h2>Record Catches</h2>

            <table class="stats-table">
                <thead>
                <tr>
                    <th>Category</th>
                    <th>Species</th>
                    <th>Measurement</th>
                    <th>Location</th>
                    <th>Date</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="stat-label">Heaviest Fish</td>
                    <% if (request.getAttribute("heaviestFish") != null) { %>
                    <td><%= request.getAttribute("heaviestFish") %></td>
                    <td class="stat-value"><%= request.getAttribute("heaviestWeight") %> lbs</td>
                    <td><%= request.getAttribute("heaviestLocation") %></td>
                    <td><%= request.getAttribute("heaviestDate") %></td>
                    <% } else { %>
                    <td colspan="4" class="no-data">No data available yet</td>
                    <% } %>
                </tr>
                <tr>
                    <td class="stat-label">Longest Fish</td>
                    <% if (request.getAttribute("longestFish") != null) { %>
                    <td><%= request.getAttribute("longestFish") %></td>
                    <td class="stat-value"><%= request.getAttribute("longestLength") %> inches</td>
                    <td><%= request.getAttribute("longestLocation") %></td>
                    <td><%= request.getAttribute("longestDate") %></td>
                    <% } else { %>
                    <td colspan="4" class="no-data">No data available yet</td>
                    <% } %>
                </tr>
                </tbody>
            </table>
        </div>

        <!-- top bait and location -->
        <div class="stats-section">
            <h2>Top Performers</h2>

            <table class="stats-table">
                <thead>
                <tr>
                    <th>Category</th>
                    <th>Name</th>
                    <th>Total Catches</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="stat-label">Most Productive Location</td>
                    <% if (request.getAttribute("topLocation") != null) { %>
                    <td><%= request.getAttribute("topLocation") %></td>
                    <td class="stat-value"><%= request.getAttribute("locationCount") %> catches</td>
                    <% } else { %>
                    <td colspan="2" class="no-data">No data available yet</td>
                    <% } %>
                </tr>
                <tr>
                    <td class="stat-label">Most Productive Bait</td>
                    <% if (request.getAttribute("topBait") != null) { %>
                    <td><%= request.getAttribute("topBait") %></td>
                    <td class="stat-value"><%= request.getAttribute("baitCount") %> catches</td>
                    <% } else { %>
                    <td colspan="2" class="no-data">No data available yet</td>
                    <% } %>
                </tr>
                </tbody>
            </table>
        </div>

    </div>

    <% } %>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>