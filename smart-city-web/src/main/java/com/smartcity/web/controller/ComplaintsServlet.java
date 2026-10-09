package com.smartcity.web.controller;

import com.smartcity.exception.SmartCityException;
import com.smartcity.model.Complaint;
import com.smartcity.util.SessionUser;
import com.smartcity.web.util.CSRFUtil;
import com.smartcity.web.util.SessionUtil;
import com.smartcity.web.util.WebContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Complaints endpoints. Behaviour depends on the caller's role:
 *
 * <ul>
 *   <li>Admin GET  → list ALL complaints</li>
 *   <li>Citizen GET → list only the caller's complaints</li>
 *   <li>Citizen POST → register a new complaint on the caller's behalf
 *       (the citizenId is taken from the session, never from the form)</li>
 * </ul>
 *
 * <p>The "never trust a user-supplied citizenId" rule from the
 * authentication rubric is enforced here: even if a citizen tampers with
 * the form to submit a complaint on someone else's behalf, we ignore
 * their supplied citizenId and use the one from the session.
 */
public class ComplaintsServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        SessionUser user = SessionUtil.get(req);
        try {
            List<Complaint> list;
            if (user.isAdmin()) {
                list = WebContext.services().complaintService().getAllComplaints();
            } else {
                list = WebContext.services().complaintService().getComplaintsForCitizen(user.getId());
            }
            req.setAttribute("complaints", list);
            req.setAttribute("user", user);
            req.setAttribute("csrfToken", CSRFUtil.issue(req));
            req.getRequestDispatcher("/WEB-INF/jsp/complaints.jsp").forward(req, resp);
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
                    "Only citizens may register complaints.");
            return;
        }
        String subject = param(req, "subject");
        String description = param(req, "description");
        try {
            WebContext.services().complaintService().registerComplaint(
                    user.getId(), subject, description);  // citizenId from SESSION, not form
            resp.sendRedirect(req.getContextPath() + "/complaints");
        } catch (SmartCityException ex) {
            // ValidationException is a subclass of SmartCityException, so this
            // catch handles both cases. Split into two catches if we ever
            // want different HTTP status codes per type.
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }

    private static String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v == null ? "" : v.trim();
    }
}
