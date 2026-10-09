<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart City - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="split">
    <div class="hero navy">
        <div class="brand teal">●  SMART CITY</div>
        <h1>Empowering citizens.<br>Connecting services.<br>Building smarter cities.</h1>
        <p>A centralised platform for citizens, services and city administrators. Log in to register complaints,
           track city services and manage records.</p>
    </div>
    <div class="form-panel">
        <h2>Welcome back</h2>
        <h3>Sign in to your account</h3>

        <c:if test="${not empty error}">
            <div class="error-box"><c:out value="${error}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login" class="login-form">
            <input type="hidden" name="csrfToken" value="${csrfToken}"/>

            <div class="role-toggle">
                <label><input type="radio" name="role" value="admin" checked onchange="updateRole()"> Administrator</label>
                <label><input type="radio" name="role" value="citizen" onchange="updateRole()"> Citizen</label>
            </div>

            <div id="admin-fields">
                <label>USERNAME</label>
                <input type="text" name="username" placeholder="admin" autocomplete="username" required>
            </div>

            <div id="citizen-fields" style="display:none">
                <label>EMAIL</label>
                <input type="email" name="email" placeholder="you@example.com" autocomplete="email">
            </div>

            <label>PASSWORD</label>
            <input type="password" name="password" placeholder="admin123" autocomplete="current-password" required>

            <button type="submit" class="btn-primary">Sign In</button>
            <p class="hint">Admin: admin / admin123 &nbsp;·&nbsp; Citizen: aarav@example.com / password1</p>
        </form>
    </div>
</div>
<script>
function updateRole() {
    var role = document.querySelector('input[name=role]:checked').value;
    document.getElementById('admin-fields').style.display   = role === 'admin' ? '' : 'none';
    document.getElementById('citizen-fields').style.display = role === 'admin' ? 'none' : '';
    document.querySelector('input[name=password]').placeholder = role === 'admin' ? 'admin123' : 'Your password';
}
</script>
</body>
</html>
