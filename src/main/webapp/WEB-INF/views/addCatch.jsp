<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Hooked - Add Catch</title>
  <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

  <div class="header">
    <h1>Log your catch into the database!</h1>
  </div>

  <%@include file="/WEB-INF/includes/navigation.jsp"%>

  <h4>To log a catch, enter the information into the corresponding field.
   <br> When finished, click Add Catch and you will be directed to the successful entry page.
  </h4>

  <form action="<%= request.getContextPath() %>/addCatch" method="post">

    <input type="hidden" name="userId" value="1"> <!-- set dynamically if needed -->

    <label for="speciesName">Species:</label>
    <input type="text" id="speciesName" name="speciesName" placeholder="ex. Bluegill"><br><br>

    <label for="length">Length (inches):</label>
    <input type="text" id="length" name="length" placeholder="ex. 11.00"><br><br>

    <label for="weight">Weight (pounds):</label>
    <input type="text" id="weight" name="weight" placeholder="ex. 1.00"><br><br>

    <label for="locationName">Location:</label>
    <input type="text" id="locationName" name="locationName" placeholder="ex. Mississippi River"><br><br>

    <label for="baitType">Bait:</label>
    <input type="text" id="baitType" name="baitType" placeholder="ex. Gulp! Minnow"><br><br>

    <label for="dateCaught">Date Caught (YYYY-MM-DD):</label>
    <input type="text" id="dateCaught" name="dateCaught" placeholder="ex. 2025-9-15"><br><br>

    <label for="notes">Notes:</label>
    <input type="text" id="notes" name="notes" placeholder="ex. Slip Bobber"><br><br>

    <input type="submit" value="Add Catch">

  </form>

  <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>
