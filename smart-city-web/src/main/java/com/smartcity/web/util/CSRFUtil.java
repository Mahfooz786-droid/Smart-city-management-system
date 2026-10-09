package com.smartcity.web.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * CSRF (Cross-Site Request Forgery) protection.
 *
 * <p>For every GET of a form-bearing page we generate a fresh random
 * token and stash it on the session + a hidden form field. On the
 * matching POST, {@link com.smartcity.web.filter.CSRFFilter} checks the
 * submitted token against the session's. Mismatch → 403.
 *
 * <p>Token is a 16-byte random value, Base64-encoded (no padding). Uses
 * {@link SecureRandom} (the JDK's cryptographically-strong RNG).
 */
public final class CSRFUtil {

    public static final String SESSION_KEY = "smartcity.csrf";
    public static final String FORM_FIELD  = "csrfToken";

    private static final SecureRandom RNG = new SecureRandom();

    private CSRFUtil() {}

    /** Generates a new token, stores it on the session, returns it. */
    public static String issue(HttpServletRequest req) {
        byte[] raw = new byte[16];
        RNG.nextBytes(raw);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
        req.getSession().setAttribute(SESSION_KEY, token);
        return token;
    }

    /** Returns the current session token, or null if none. */
    public static String current(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s == null ? null : (String) s.getAttribute(SESSION_KEY);
    }

    /**
     * Constant-time comparison so an attacker cannot use timing to brute
     * force a valid token.
     */
    public static boolean verify(HttpServletRequest req, String supplied) {
        String stored = current(req);
        if (stored == null || supplied == null || supplied.isEmpty()) return false;
        if (stored.length() != supplied.length()) return false;
        int diff = 0;
        for (int i = 0; i < stored.length(); i++) {
            diff |= stored.charAt(i) ^ supplied.charAt(i);
        }
        return diff == 0;
    }
}
