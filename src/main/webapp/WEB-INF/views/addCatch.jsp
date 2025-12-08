<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
   <br> When finished, click Add Catch and you will see your catch added to your logged catches table.
  </h4>

  <c:if test="${not empty error}">
    <p class="error">${error}</p>
  </c:if>

  <c:if test="${not empty success}">
    <p class="success">${success}</p>
  </c:if>

  <p id="speciesWarning" class="warning"></p>

  <form action="${pageContext.request.contextPath}/addCatch" method="post">

    <input type="hidden" name="catchId"
           value="${editCatch != null ? editCatch.catchId : ''}">

    <input type="hidden" name="ownerUserId"
           value="${editCatch != null ? editCatch.userId : sessionScope.userId}">

    <input type="hidden" name="userId" value="1">

    <label for="speciesName">Species:</label>
    <select id="speciesName" name="speciesName" required>
      <option value="" disabled
              <c:if test="${editCatch == null && empty param.speciesName}">selected</c:if>>
        Select a species
      </option>

      <c:forEach var="s" items="${speciesList}">
        <c:set var="selected"
               value="${(editCatch != null and editCatch.speciesName == s.speciesName) or
                     (editCatch == null and param.speciesName == s.speciesName)}" />

        <option value="${s.speciesName}"
                data-max-length="${s.maxLength}"
                data-max-weight="${s.maxWeight}"
                <c:if test="${selected}">selected</c:if>>
            ${s.speciesName}
        </option>
      </c:forEach>
    </select>
    <a href="${pageContext.request.contextPath}/species#speciesTableOne"
       class="btn">
      Can't find Species
    </a>

    <label for="length" id="lengthLabel">
      Length (inches):
    </label>
    <input type="number" step=".01" min="0" id="length" name="length"
           placeholder="ex. 11.00"
           value="${editCatch != null ? editCatch.length : param.length}"
           required>
    <br>

    <label for="weight" id="weightLabel">
      Weight (pounds):
    </label>
    <input type="number" step=".01" min="0" id="weight" name="weight"
           placeholder="ex. 1.00"
           value="${editCatch != null ? editCatch.weight : param.weight}"
           required>
    <br>

    <label for="locationName">Location:</label>

    <select id="locationName" name="locationName" required>
      <option value="" disabled
              <c:if test="${editCatch == null && empty param.locationName}">selected</c:if>>
        Select location
      </option>

      <c:forEach var="loc" items="${locationList}">
        <c:set var="selected"
               value="${(editCatch != null and editCatch.locationName == loc.locationName)
                    or (editCatch == null and param.locationName == loc.locationName)}" />
        <option value="${loc.locationName}" <c:if test="${selected}">selected</c:if>>
            ${loc.locationName} (${loc.state})
        </option>
      </c:forEach>
    </select>

    <a href="${pageContext.request.contextPath}/location#locationFormOne"
       class="btn">
      Can't Find Location
    </a>

    <label for="baitType">Bait:</label>
    <select id="baitType" name="baitType" required>
      <option value="" disabled
              <c:if test="${editCatch == null && empty param.baitType}">selected</c:if>>
        Select bait
      </option>

      <c:forEach var="b" items="${baitList}">
        <c:set var="selected"
               value="${(editCatch != null and editCatch.baitType == b.name)
                        or (editCatch == null and param.baitType == b.name)}" />
        <option value="${b.name}" <c:if test="${selected}">selected</c:if>>
            ${b.name}
        </option>
      </c:forEach>
    </select>

    <a href="${pageContext.request.contextPath}/bait#baitTableOne"
       class="btn">
      Can't find Bait
    </a>
    <br>

    <label for="dateCaught">Date Caught (YYYY-MM-DD):</label>
    <input type="date" id="dateCaught" name="dateCaught" placeholder="ex. 2025-09-15" value="${editCatch != null ? editCatch.dateCaught : param.dateCaught}"><br>

    <label for="notes">Notes:</label>
    <input type="text" id="notes" name="notes" placeholder="ex. Slip Bobber" value="${editCatch != null ? editCatch.notes : param.notes}"><br>

    <input type="submit" class="btn" value="${editCatch != null ? 'Update Catch' : 'Add Catch'}">

  </form>

  <br><hr><br>

  <!-- Table of existing catches -->
  <c:if test="${not empty catches}">
    <h2>Your Logged Catches</h2>
    <table>
      <thead>
      <tr>
        <th>Species</th>
        <th>Length (in)</th>
        <th>Weight (lb)</th>
        <th>Location</th>
        <th>Date</th>
        <th>Bait</th>
        <th>Notes</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="c" items="${catches}">
        <tr>
          <td>${c.speciesName}</td>
          <td>${c.length}</td>
          <td>${c.weight}</td>
          <td>${c.locationName}</td>
          <td>${c.dateCaught}</td>
          <td>${c.baitType}</td>
          <td>${c.notes}</td>
          <td>
            <a href="${pageContext.request.contextPath}/addCatch?editId=${c.catchId}">
              Edit
            </a>
            |
            <a href="${pageContext.request.contextPath}/addCatch?deleteId=${c.catchId}"
               onclick="return confirm('Are you sure you want to delete this catch?');">
              Delete
            </a>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:if>

  <script>
    function updateMaxLabels() {
      var select = document.getElementById('speciesName');
      if (!select) return;

      var option = select.options[select.selectedIndex];
      if (!option) return;

      // Read data-* attributes
      var maxLenRaw = option.getAttribute('data-max-length');
      var maxWtRaw  = option.getAttribute('data-max-weight');

      var lengthLabel = document.getElementById('lengthLabel');
      var weightLabel = document.getElementById('weightLabel');

      console.log('Selected species:', option.value,
              'maxLenRaw:', maxLenRaw, 'maxWtRaw:', maxWtRaw);

      if (lengthLabel) {
        if (maxLenRaw) {
          var numLen = parseFloat(maxLenRaw);
          var formattedLen = isNaN(numLen) ? maxLenRaw : numLen.toFixed(2);
          lengthLabel.textContent = 'Length (Max. ' + formattedLen + ' inches):';
        } else {
          lengthLabel.textContent = 'Length (inches):';
        }
      }

      if (weightLabel) {
        if (maxWtRaw) {
          var numWt = parseFloat(maxWtRaw);
          var formattedWt = isNaN(numWt) ? maxWtRaw : numWt.toFixed(2);
          weightLabel.textContent = 'Weight (Max. ' + formattedWt + ' pounds):';
        } else {
          weightLabel.textContent = 'Weight (pounds):';
        }
      }
    }

    // Run when page is fully loaded
    window.addEventListener('load', function () {
      var select = document.getElementById('speciesName');
      if (!select) return;

      // Update once on load (for edit mode / default selection)
      updateMaxLabels();

      // Update whenever user changes species
      select.addEventListener('change', updateMaxLabels);
    });
  </script>

  <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>
</body>
</html>
