<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart City - Services</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="layout">
    <aside class="sidebar navy">
        <div class="brand teal">● SMART CITY</div>
        <nav>
            <a href="${pageContext.request.contextPath}/services" class="active">City Services</a>
            <a href="${pageContext.request.contextPath}/complaints">My Complaints</a>
            <a href="${pageContext.request.contextPath}/feedback">Give Feedback</a>
            <form method="post" action="${pageContext.request.contextPath}/logout" style="margin-top:auto">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <button type="submit" class="nav-logout">Logout</button>
            </form>
        </nav>
        <div class="user-badge">${sessionScope['smartcity.user'].name} · Citizen</div>
    </aside>
    <main>
        <h1>City Services</h1>
        <p class="muted">Available municipal services offered by the city</p>
        <div class="services-grid">
            <c:forEach var="s" items="${services}">
                <div class="service-card">
                    <div class="service-name"><c:out value="${s.name}"/></div>
                    <div class="service-dept"><c:out value="${s.department}"/></div>
                    <div class="service-desc"><c:out value="${s.description}"/></div>
                    <div class="service-status status-${s.status.dbValue}"><c:out value="${s.status.dbValue}"/></div>
                </div>
            </c:forEach>
        </div>
    </main>
</div>
</body>
</html>
