package com.smartcity.web.filter;

import com.smartcity.web.util.SessionUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Enforces that every request to a protected URL carries a valid
 * authenticated session. Unauthenticated users are redirected to /login.
 *
 * <p>Role-specific enforcement (admin-only vs. citizen-accessible) is
 * handled separately inside each servlet's doGet / doPost — this filter
 * only checks "is anyone logged in?".
 */
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes = (HttpServletResponse) res;

        if (SessionUtil.get(httpReq) == null) {
            // Redirect-after-login: stash the originally-requested URL so we
            // can resume it after a successful login.
            String original = httpReq.getRequestURI();
            if (httpReq.getQueryString() != null) {
                original += "?" + httpReq.getQueryString();
            }
            httpReq.getSession().setAttribute("redirectAfterLogin", original);
            httpRes.sendRedirect(httpReq.getContextPath() + "/login");
            return;
        }
        chain.doFilter(req, res);
    }
}
