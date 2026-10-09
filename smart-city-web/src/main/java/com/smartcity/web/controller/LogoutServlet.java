package com.smartcity.web.controller;

import com.smartcity.web.util.CSRFUtil;
import com.smartcity.web.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** POST /logout — invalidates the session and redirects to /login. */
public class LogoutServlet extends HttpServlet {

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // AuthFilter ensures only logged-in users can reach here. We just
        // blow away the session.
        SessionUtil.invalidate(req);
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    /** GET is intentionally disallowed — logout is a state change. */
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Use POST /logout with a CSRF token.");
    }
}
