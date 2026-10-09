package com.smartcity.web.filter;

import com.smartcity.web.util.CSRFUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Verifies the CSRF token on every state-changing (POST/PUT/DELETE) request.
 *
 * <p>The token is generated per-session by {@link CSRFUtil#issue} and
 * embedded as a hidden field on every JSP-rendered form. On submit, this
 * filter compares the submitted token against the session's stored token in
 * constant time. A mismatch yields a 403 Forbidden page.
 *
 * <p>GETs are allowed through unchecked — they are read-only by REST
 * convention.
 */
public class CSRFFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes = (HttpServletResponse) res;
        String method = httpReq.getMethod();
        if ("GET".equalsIgnoreCase(method) || "HEAD".equalsIgnoreCase(method)) {
            chain.doFilter(req, res);
            return;
        }
        String supplied = httpReq.getParameter(CSRFUtil.FORM_FIELD);
        if (!CSRFUtil.verify(httpReq, supplied)) {
            httpRes.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Invalid or missing CSRF token. Please submit the form from the official site.");
            return;
        }
        // Strip the CSRF field so downstream code never sees it in getParameter().
        Map<String, String[]> stripped = new HashMap<>(httpReq.getParameterMap());
        stripped.remove(CSRFUtil.FORM_FIELD);
        chain.doFilter(new HttpServletRequestWrapper(httpReq) {
            @Override public Map<String, String[]> getParameterMap() { return stripped; }
            @Override public String getParameter(String name) {
                if (CSRFUtil.FORM_FIELD.equals(name)) return null;
                return super.getParameter(name);
            }
            @Override public String[] getParameterValues(String name) {
                if (CSRFUtil.FORM_FIELD.equals(name)) return null;
                return super.getParameterValues(name);
            }
        }, res);
    }
}
