<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart City - Feedback</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="layout">
    <aside class="sidebar navy">
        <div class="brand teal">● SMART CITY</div>
        <nav>
            <a href="${pageContext.request.contextPath}/citizens">Citizens</a>
            <a href="${pageContext.request.contextPath}/complaints">Complaints</a>
            <a href="${pageContext.request.contextPath}/feedback" class="active">Feedback</a>
            <a href="${pageContext.request.contextPath}/services">Services</a>
            <form method="post" action="${pageContext.request.contextPath}/logout" style="margin-top:auto">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <button type="submit" class="nav-logout">Logout</button>
            </form>
        </nav>
        <div class="user-badge">${sessionScope['smartcity.user'].name} · Administrator</div>
    </aside>
    <main>
        <h1>All Feedback (${fn:length(feedbackList)})</h1>
        <p class="muted">Every feedback entry submitted by citizens</p>
        <div class="card">
            <table>
                <thead><tr>
                    <th>ID</th><th>Citizen</th><th>Rating</th><th>Comment</th><th>Submitted</th>
                </tr></thead>
                <tbody>
                <c:forEach var="f" items="${feedbackList}">
                    <tr>
                        <td><c:out value="${f.id}"/></td>
                        <td><c:out value="${f.citizenName}"/></td>
                        <td><span class="rating-pill"><c:out value="${f.rating}"/>/5</span></td>
                        <td><c:out value="${f.comment}"/></td>
                        <td>${f.createdAt == null ? '' : f.createdAt.toLocalDate()}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</div>
</body>
</html>
