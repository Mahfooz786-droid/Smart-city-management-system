<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart City - Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="error-page">
    <h1>Something went wrong</h1>
    <div class="error-box"><c:out value="${error}"/></div>
    <a href="${pageContext.request.contextPath}/login" class="btn-primary">Back to login</a>
</div>
</body>
</html>
