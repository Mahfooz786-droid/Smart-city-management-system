<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart City - Complaints</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="layout">
    <aside class="sidebar ${user.admin ? 'navy' : 'navy'}">
        <div class="brand ${user.admin ? 'teal' : 'coral'}">● SMART CITY</div>
        <nav>
            <a href="${pageContext.request.contextPath}/complaints" class="active">Complaints</a>
            <c:if test="${user.admin}">
                <a href="${pageContext.request.contextPath}/citizens">Citizens</a>
                <a href="${pageContext.request.contextPath}/feedback">Feedback</a>
            </c:if>
            <c:if test="${not user.admin}">
                <a href="${pageContext.request.contextPath}/services">City Services</a>
                <a href="${pageContext.request.contextPath}/feedback">Give Feedback</a>
            </c:if>
            <form method="post" action="${pageContext.request.contextPath}/logout" style="margin-top:auto">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <button type="submit" class="nav-logout">Logout</button>
            </form>
        </nav>
        <div class="user-badge">
            ${user.name} · ${user.admin ? 'Administrator' : 'Citizen'}
        </div>
    </aside>
    <main>
        <h1>${user.admin ? 'All Complaints' : 'My Complaints'}</h1>
        <p class="muted">
            <c:choose>
                <c:when test="${user.admin}">View and update the status of every complaint in the system.</c:when>
                <c:otherwise>Register a new complaint or track the status of existing ones.</c:otherwise>
            </c:choose>
        </p>

        <c:if test="${not user.admin}">
            <div class="card">
                <h2>Register a new complaint</h2>
                <form method="post" action="${pageContext.request.contextPath}/complaints" class="grid-form">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <label class="span2">Subject<input type="text" name="subject" required></label>
                    <label class="span2">Description<textarea name="description" rows="4" required></textarea></label>
                    <div class="span2"><button type="submit" class="btn-primary">Register Complaint</button></div>
                </form>
            </div>
        </c:if>

        <div class="card">
            <h2>${user.admin ? 'All' : 'My'} complaints (${fn:length(complaints)})</h2>
            <table>
                <thead><tr>
                    <th>ID</th>
                    <c:if test="${user.admin}"><th>Citizen</th></c:if>
                    <th>Subject</th>
                    <th>Description</th>
                    <th>Status</th>
                    <c:if test="${user.admin}"><th>Action</th></c:if>
                    <c:if test="${not user.admin}"><th>Filed</th></c:if>
                </tr></thead>
                <tbody>
                <c:forEach var="c" items="${complaints}">
                    <tr>
                        <td><c:out value="${c.id}"/></td>
                        <c:if test="${user.admin}"><td><c:out value="${c.citizenName}"/></td></c:if>
                        <td><c:out value="${c.subject}"/></td>
                        <td><c:out value="${c.description}"/></td>
                        <td><span class="status-pill ${fn:replace(c.status.dbValue, ' ', '-')}"><c:out value="${c.status.dbValue}"/></span></td>
                        <c:if test="${user.admin}">
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/complaints/status" style="display:flex; gap:8px">
                                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                                    <input type="hidden" name="complaintId" value="${c.id}">
                                    <select name="status">
                                        <option value="Pending" ${c.status.dbValue == 'Pending' ? 'selected' : ''}>Pending</option>
                                        <option value="In Progress" ${c.status.dbValue == 'In Progress' ? 'selected' : ''}>In Progress</option>
                                        <option value="Resolved" ${c.status.dbValue == 'Resolved' ? 'selected' : ''}>Resolved</option>
                                    </select>
                                    <button type="submit" class="btn-secondary">Update</button>
                                </form>
                            </td>
                        </c:if>
                        <c:if test="${not user.admin}">
                            <td>${c.createdAt == null ? '' : c.createdAt.toLocalDate()}</td>
                        </c:if>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</div>
</body>
</html>
