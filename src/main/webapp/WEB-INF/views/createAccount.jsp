<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - CreateAccount</title>
    <%@include file="/WEB-INF/includes/header.jsp"%>
</head>
<body>
<div class="container">

    <div class="header">
        <h1>Create Your Account</h1>
    </div>

    <%@include file="/WEB-INF/includes/navigation.jsp"%>

    <!-- Error message display -->
    <% if (request.getAttribute("Error") != null) { %>
    <div style="color: #fecaca; padding: 10px; margin: 10px auto; max-width: 500px; border: 1px solid var(--border-soft); background-color: var(--bg-card); border-radius: 10px;">
        <strong>Error:</strong> <%= request.getAttribute("Error") %>
    </div>
    <% } %>

    <form action="<%= request.getContextPath() %>/Register" method="post" onsubmit="return validateForm()">
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" placeholder="First Name" required
               pattern="[A-Za-z\s\-']+" title="First name can only contain letters, spaces, hyphens, and apostrophes">

        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" placeholder="Last Name" required
               pattern="[A-Za-z\s\-']+" title="Last name can only contain letters, spaces, hyphens, and apostrophes">

        <label for="userName">Username:</label>
        <input type="text" id="userName" name="userName" placeholder="Username (3-50 characters)" required
               minlength="3" maxlength="50" pattern="[A-Za-z0-9_\-]+"
               title="Username must be 3-50 characters and can only contain letters, numbers, underscores, and hyphens">

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" placeholder="Email" required
               title="Please enter a valid email address">

        <label for="passwordHash">Password:</label>
        <input type="password" id="passwordHash" name="passwordHash" placeholder="Password (minimum 6 characters)" required
               minlength="6" maxlength="100" title="Password must be at least 6 characters long">

        <label for="confirmPassword">Confirm Password:</label>
        <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm Password" required
               minlength="6" maxlength="100" title="Please confirm your password">
        <small id="passwordError" style="display: none; color: #fecaca; margin-top: 5px;"></small>

        <br>
        <input type="submit" value="Create Account">
    </form>

    <%@include file="/WEB-INF/includes/footer.jsp"%>
</div>

<script>
    function validateForm() {
        // Get form values
        const firstName = document.getElementById('firstName').value.trim();
        const lastName = document.getElementById('lastName').value.trim();
        const userName = document.getElementById('userName').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('passwordHash').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const errorElement = document.getElementById('passwordError');

        errorElement.style.display = 'none';
        errorElement.textContent = '';

        // check if all fields are filled.
        if (!firstName || !lastName || !userName || !email || !password || !confirmPassword) {
            alert('All fields are required. Please fill in all information.');
            return false;
        }

        // validate first and last name
        const namePattern = /^[A-Za-z\s\-']+$/;
        if (!namePattern.test(firstName)) {
            alert('First name can only contain letters, spaces, hyphens, and apostrophes.');
            return false;
        }
        if (!namePattern.test(lastName)) {
            alert('Last name can only contain letters, spaces, hyphens, and apostrophes.');
            return false;
        }

        // validate username
        const userNamePattern = /^[A-Za-z0-9_\-]+$/;
        if (userName.length < 3 || userName.length > 50) {
            alert('Username must be between 3 and 50 characters.');
            return false;
        }
        if (!userNamePattern.test(userName)) {
            alert('Username can only contain letters, numbers, underscores, and hyphens.');
            return false;
        }

        // validate email
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(email)) {
            alert('Please enter a valid email address.');
            return false;
        }

        // validate password length
        if (password.length < 6) {
            alert('Password must be at least 6 characters long.');
            return false;
        }

        // check if passwords match
        if (password !== confirmPassword) {
            errorElement.textContent = 'Passwords do not match!';
            errorElement.style.display = 'block';
            alert('Passwords do not match!');
            return false;
        }

        return true;
    }

    // password match validation
    document.getElementById('confirmPassword').addEventListener('input', function() {
        const password = document.getElementById('passwordHash').value;
        const confirmPassword = this.value;
        const errorElement = document.getElementById('passwordError');

        if (confirmPassword && password !== confirmPassword) {
            errorElement.textContent = 'Passwords do not match!';
            errorElement.style.display = 'block';
        } else {
            errorElement.style.display = 'none';
        }
    });
</script>
</body>
</html>