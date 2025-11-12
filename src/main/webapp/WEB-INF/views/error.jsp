<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hooked - Error</title>
</head>
<body>
<h1>An Error Has Occurred</h1>
<h2>Error Details:</h2>
<p>Type: ${pageContext.exception["class"]}</p>
<p>Message: ${pageContext.exception.toString()}</p>
</body>
</html>