package com.smartcity.model;

/**
 * Contract for any "user-like" entity in the Smart City system
 * (citizens, future administrators, support staff, etc.).
 *
 * <p>Used to demonstrate interfaces + runtime polymorphism: every concrete
 * user type provides its own {@link #login()} / {@link #logout()} / {@link #showRole()}
 * behaviour.
 */
public interface UserOperations {

    void login();
    void logout();
    void showRole();
}
