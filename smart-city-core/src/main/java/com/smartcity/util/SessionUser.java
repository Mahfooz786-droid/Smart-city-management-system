package com.smartcity.util;

/**
 * Lightweight, serialisable value type representing the currently
 * authenticated principal in the Web session.
 *
 * <p>Carries the role + the underlying id (citizenId for citizens, 0 for the
 * hardcoded admin) + display name. No password or hash is ever stored
 * here, so it is safe to attach to an {@code HttpSession}.
 */
public final class SessionUser {

    public enum Role { ADMIN, CITIZEN }

    private final int id;
    private final String name;
    private final Role role;

    public SessionUser(int id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Role getRole() { return role; }

    public boolean isAdmin() { return role == Role.ADMIN; }
    public boolean isCitizen() { return role == Role.CITIZEN; }
}
