package com.smartcity.web.controller;

import com.smartcity.exception.SmartCityException;
import com.smartcity.exception.ValidationException;
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
 * GET  /feedback      → list all feedback (admin) or show the citizen's submission form
 * POST /feedback      → submit feedback (citizen-only)
 *
 * <p>citizenId on POST is taken from the session, never from the form.
 */
public class FeedbackServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        SessionUser user = SessionUtil.get(req);
        try {
            if (user.isAdmin()) {
                req.setAttribute("feedbackList", WebContext.services().feedbackService().getAllFeedback());
                req.getRequestDispatcher("/WEB-INF/jsp/feedback-list.jsp").forward(req, resp);
            } else {
                req.setAttribute("csrfToken", CSRFUtil.issue(req));
                req.getRequestDispatcher("/WEB-INF/jsp/feedback-form.jsp").forward(req, resp);
            }
        } catch (SmartCityException ex) {
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        SessionUser user = SessionUtil.get(req);
        if (!user.isCitizen()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Only citizens may submit feedback.");
            return;
        }
        try {
            int rating = Integer.parseInt(req.getParameter("rating"));
            String comment = req.getParameter("comment");
            WebContext.services().feedbackService().submitFeedback(user.getId(), rating, comment);
            resp.sendRedirect(req.getContextPath() + "/feedback");
        } catch (NumberFormatException | ValidationException ex) {
            req.setAttribute("error", "Invalid rating.");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        } catch (SmartCityException ex) {
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }
}
