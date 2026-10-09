package com.smartcity.web.controller;

import com.smartcity.exception.SmartCityException;
import com.smartcity.exception.ValidationException;
import com.smartcity.model.Citizen;
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
 * Admin-only: list citizens + add a new citizen.
 *
 * <p>Authorization rule: only {@link com.smartcity.util.SessionUser.Role#ADMIN}
 * may call either method; non-admins get a 403.
 */
public class CitizensServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!SessionUtil.isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Only administrators may view citizens.");
            return;
        }
        try {
            List<Citizen> citizens = WebContext.services().citizenService().getAllCitizens();
            req.setAttribute("citizens", citizens);
            req.setAttribute("csrfToken", CSRFUtil.issue(req));
            req.getRequestDispatcher("/WEB-INF/jsp/citizens.jsp").forward(req, resp);
        } catch (SmartCityException ex) {
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!SessionUtil.isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Only administrators may add citizens.");
            return;
        }
        try {
            String name     = param(req, "name");
            String email    = param(req, "email");
            String phone    = param(req, "phone");
            String password = req.getParameter("password"); // do not trim passwords
            String address  = param(req, "address");
            WebContext.services().citizenService().addCitizen(name, email, phone, password, address);
            // Redirect-after-post: GET /citizens refreshes the list.
            resp.sendRedirect(req.getContextPath() + "/citizens");
        } catch (ValidationException ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("csrfToken", CSRFUtil.issue(req));
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        } catch (SmartCityException ex) {
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }

    private static String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v == null ? "" : v.trim();
    }
}
