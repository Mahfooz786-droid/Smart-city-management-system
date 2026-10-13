package com.smartcity.ui;

import com.smartcity.config.DatabaseConfig;
import com.smartcity.service.AdminAuthService;
import com.smartcity.service.ServiceFactory;

/**
 * Tiny shared holder that lets the GUI screens reach the wired-up service
 * layer without each one re-creating a {@link ServiceFactory}.
 *
 * <p>The holder is set once from {@link Main#main} and is read-only after
 * that. UI screens pull services from here rather than taking them as
 * constructor arguments, so the action-listener boilerplate stays simple.
 */
public final class GuiContext {

    private static ServiceFactory services;
    private static AdminAuthService adminAuthService;
    private static DatabaseConfig databaseConfig;

    private GuiContext() {}

    public static void init(DatabaseConfig db) {
        databaseConfig = db;
        services = new ServiceFactory(db);
        adminAuthService = new AdminAuthService();
    }

    public static ServiceFactory services() { return services; }
    public static AdminAuthService adminAuth() { return adminAuthService; }
    public static DatabaseConfig databaseConfig() { return databaseConfig; }
}
