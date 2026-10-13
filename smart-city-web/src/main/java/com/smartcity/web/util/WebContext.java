package com.smartcity.web.util;

import com.smartcity.config.DatabaseConfig;
import com.smartcity.service.AdminAuthService;
import com.smartcity.service.ServiceFactory;

/**
 * Web-module analogue of the GUI's GuiContext. Lazily constructs the
 * ServiceFactory on first use and caches it on the classloader.
 *
 * <p>The database config is read from env vars (DB_URL / DB_USER /
 * DB_PASSWORD) the same way the GUI does it. The Tomcat runtime just
 * needs those env vars set on the JVM that launches Tomcat (e.g. via
 * setenv.sh).
 */
public final class WebContext {

    private static volatile ServiceFactory services;
    private static volatile AdminAuthService adminAuth;

    private WebContext() {}

    public static ServiceFactory services() {
        if (services == null) {
            synchronized (WebContext.class) {
                if (services == null) {
                    services = new ServiceFactory(new DatabaseConfig());
                }
            }
        }
        return services;
    }

    public static AdminAuthService adminAuth() {
        if (adminAuth == null) {
            synchronized (WebContext.class) {
                if (adminAuth == null) {
                    adminAuth = new AdminAuthService();
                }
            }
        }
        return adminAuth;
    }
}
