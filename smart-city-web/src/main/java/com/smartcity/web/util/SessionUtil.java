package com.smartcity.web.util;

import com.smartcity.util.SessionUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Helpers for storing / retrieving the authenticated principal on the
 * {@link HttpSession}.
 *
 * <p>The SessionUser is the only security-sensitive attribute stored on the
 * session — never the password hash, never the raw password.
 */
public final class SessionUtil {

    public static final String SESSION_KEY = "smartcity.user";

    private SessionUtil() {}

    public static void set(HttpServletRequest req, SessionUser user) {
        req.getSession().setAttribute(SESSION_KEY, user);
    }

    /** @return the current {@link SessionUser} or null if no one is logged in. */
    public static SessionUser get(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        Object o = session.getAttribute(SESSION_KEY);
        return o instanceof SessionUser u ? u : null;
    }

    public static void invalidate(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static boolean isAdmin(HttpServletRequest req) {
        SessionUser u = get(req);
        return u != null && u.isAdmin();
    }

    public static boolean isCitizen(HttpServletRequest req) {
        SessionUser u = get(req);
        return u != null && u.isCitizen();
    }
}
