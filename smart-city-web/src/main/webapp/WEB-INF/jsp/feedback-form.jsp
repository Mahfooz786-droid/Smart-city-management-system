<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart City - Give Feedback</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="layout">
    <aside class="sidebar navy">
        <div class="brand coral">● SMART CITY</div>
        <nav>
            <a href="${pageContext.request.contextPath}/complaints">My Complaints</a>
            <a href="${pageContext.request.contextPath}/services">City Services</a>
            <a href="${pageContext.request.contextPath}/feedback" class="active">Give Feedback</a>
            <form method="post" action="${pageContext.request.contextPath}/logout" style="margin-top:auto">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <button type="submit" class="nav-logout">Logout</button>
            </form>
        </nav>
        <div class="user-badge">${sessionScope['smartcity.user'].name} · Citizen</div>
    </aside>
    <main>
        <h1>We value your voice</h1>
        <p class="muted">Rate your experience with city services and share your comments. Your feedback helps administrators prioritise improvements.</p>
        <div class="card">
            <h2>Submit feedback</h2>
            <form method="post" action="${pageContext.request.contextPath}/feedback" class="grid-form">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <label class="span2">Rating
                    <select name="rating" required>
                        <option value="5" selected>5 - Excellent</option>
                        <option value="4">4 - Very Good</option>
                        <option value="3">3 - Good</option>
                        <option value="2">2 - Fair</option>
                        <option value="1">1 - Poor</option>
                    </select>
                </label>
                <label class="span2">Comment
                    <textarea name="comment" rows="5" placeholder="Share your thoughts..." required></textarea>
                </label>
                <div class="span2"><button type="submit" class="btn-primary">Submit Feedback</button></div>
            </form>
        </div>
    </main>
</div>
</body>
</html>
