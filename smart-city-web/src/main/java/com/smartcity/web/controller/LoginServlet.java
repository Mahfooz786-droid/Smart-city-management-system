package com.smartcity.web.controller;

import com.smartcity.exception.AuthenticationException;
import com.smartcity.exception.ValidationException;
import com.smartcity.model.Administrator;
import com.smartcity.model.Citizen;
import com.smartcity.util.SessionUser;
import com.smartcity.web.util.CSRFUtil;
import com.smartcity.web.util.SessionUtil;
import com.smartcity.web.util.WebContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles login GET (form) and POST (auth).
 *
 * <p>POST flow:
 * <ol>
 *   <li>role=admin  → WebContext.adminAuth().login → set SessionUser(ADMIN)</li>
 *   <li>role=citizen → WebContext.services().citizenAuthService().login → set SessionUser(CITIZEN)</li>
 *   <li>On success: rotate session ID (anti-fixation), redirect-after-post.</li>
 *   <li>On failure: re-render login.jsp with a generic error.</li>
 * </ol>
 */
public class LoginServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("csrfToken", CSRFUtil.issue(req));
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String role     = param(req, "role");
        String username = param(req, "username");
        String email    = param(req, "email");
        String password = param(req, "password");
        String redirectAfter = (String) req.getSession().getAttribute("redirectAfterLogin");

        try {
            SessionUser user;
            if ("admin".equals(role)) {
                Administrator admin = WebContext.adminAuth().login(username, password);
                user = new SessionUser(admin.getId(), admin.getName(), SessionUser.Role.ADMIN);
            } else {
                Citizen c = WebContext.services().citizenAuthService().login(email, password);
                user = new SessionUser(c.getId(), c.getName(), SessionUser.Role.CITIZEN);
            }
            // Rotate session ID to prevent session-fixation attacks.
            req.changeSessionId();
            SessionUtil.set(req, user);
            req.getSession().removeAttribute("redirectAfterLogin");
            String target = redirectAfter != null ? redirectAfter
                    : (user.isAdmin() ? req.getContextPath() + "/citizens"
                                      : req.getContextPath() + "/complaints");
            resp.sendRedirect(target);
        } catch (AuthenticationException | ValidationException ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("csrfToken", CSRFUtil.issue(req));
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
    }

    private static String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v == null ? "" : v.trim();
    }
}
