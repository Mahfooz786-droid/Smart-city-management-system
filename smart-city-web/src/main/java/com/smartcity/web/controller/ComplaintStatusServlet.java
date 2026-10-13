package com.smartcity.web.controller;

import com.smartcity.exception.AuthorizationException;
import com.smartcity.exception.SmartCityException;
import com.smartcity.model.Complaint;
import com.smartcity.web.util.SessionUtil;
import com.smartcity.web.util.WebContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin-only POST endpoint that updates a complaint's status.
 *
 * <p>The service layer's {@code isAdmin} flag is derived from the session,
 * not from a form field — this is the second layer of defence against
 * privilege escalation (the AuthFilter is the first).
 */
public class ComplaintStatusServlet extends HttpServlet {

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!SessionUtil.isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Only administrators may change complaint status.");
            return;
        }
        try {
            int complaintId = Integer.parseInt(req.getParameter("complaintId"));
            Complaint.Status newStatus = Complaint.Status.fromDb(req.getParameter("status"));
            WebContext.services().complaintService().updateStatus(complaintId, newStatus, true);
            resp.sendRedirect(req.getContextPath() + "/complaints");
        } catch (NumberFormatException ex) {
            req.setAttribute("error", "Invalid complaint id.");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        } catch (AuthorizationException ex) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
        } catch (SmartCityException ex) {
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }
}
