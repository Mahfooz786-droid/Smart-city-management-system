package com.smartcity.web.controller;

import com.smartcity.exception.SmartCityException;
import com.smartcity.web.util.CSRFUtil;
import com.smartcity.web.util.WebContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** GET /services — list the city services catalog (available to any authenticated user). */
public class ServicesServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("services", WebContext.services().cityServiceService().getAllServices());
            req.setAttribute("csrfToken", CSRFUtil.issue(req));
            req.getRequestDispatcher("/WEB-INF/jsp/services.jsp").forward(req, resp);
        } catch (SmartCityException ex) {
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }
}
