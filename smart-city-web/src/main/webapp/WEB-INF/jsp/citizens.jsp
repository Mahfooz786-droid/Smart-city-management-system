<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart City - Citizens</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="layout">
    <aside class="sidebar navy">
        <div class="brand teal">● SMART CITY</div>
        <nav>
            <a href="${pageContext.request.contextPath}/citizens" class="active">Citizens</a>
            <a href="${pageContext.request.contextPath}/complaints">Complaints</a>
            <a href="${pageContext.request.contextPath}/feedback">Feedback</a>
            <a href="${pageContext.request.contextPath}/services">Services</a>
            <form method="post" action="${pageContext.request.contextPath}/logout" style="margin-top:auto">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <button type="submit" class="nav-logout">Logout</button>
            </form>
        </nav>
        <div class="user-badge">A · Administrator</div>
    </aside>
    <main>
        <h1>Admin Dashboard</h1>
        <p class="muted">Manage citizens, complaints and feedback across the city</p>

        <c:if test="${not empty error}">
            <div class="error-box"><c:out value="${error}"/></div>
        </c:if>

        <div class="card">
            <div class="card-row">
                <h2>All Citizens (${fn:length(citizens)})</h2>
                <button class="btn-primary" onclick="document.getElementById('addForm').style.display=''">+ Add Citizen</button>
            </div>

            <div id="addForm" style="display:none; margin-bottom: 24px; padding: 16px; background:#F1F5F9; border-radius:12px">
                <h3>Add a new citizen</h3>
                <form method="post" action="${pageContext.request.contextPath}/citizens" class="grid-form">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <label>Name<input type="text" name="name" required></label>
                    <label>Email<input type="email" name="email" required></label>
                    <label>Phone<input type="text" name="phone" pattern="\d{10}" required></label>
                    <label>Password<input type="password" name="password" minlength="6" required></label>
                    <label class="span2">Address<input type="text" name="address" required></label>
                    <div class="span2"><button type="submit" class="btn-primary">Save Citizen</button></div>
                </form>
            </div>

            <table>
                <thead><tr>
                    <th>ID</th><th>Name</th><th>Email</th><th>Phone</th><th>Address</th>
                </tr></thead>
                <tbody>
                <c:forEach var="c" items="${citizens}">
                    <tr>
                        <td><c:out value="${c.id}"/></td>
                        <td><c:out value="${c.name}"/></td>
                        <td><c:out value="${c.email}"/></td>
                        <td><c:out value="${c.phone}"/></td>
                        <td><c:out value="${c.address}"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</div>
</body>
</html>
