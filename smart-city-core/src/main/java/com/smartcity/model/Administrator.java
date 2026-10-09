package com.smartcity.model;

/**
 * Concrete {@link Person} subclass representing a Smart City administrator
 * (staff member). Demonstrates inheritance + runtime polymorphism: an
 * Administrator's {@code showRole()} / {@code login()} produce different
 * output from a {@link Citizen}'s.
 *
 * <p>Admin credentials in this project are intentionally hardcoded
 * ({@code admin / admin123}) to mirror the original Java project's
 * LoginFrame. A real deployment should hash + store admin credentials in
 * the database; see README "Known limitations".
 */
public class Administrator extends Person {

    private String username;
    private String passwordHash;

    public Administrator() {
        super();
    }

    public Administrator(int id, String username, String name, String email, String passwordHash) {
        super(id, name, email);
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    @Override
    public void showRole() {
        System.out.println(name + " is an Administrator.");
    }

    @Override
    public void login() {
        System.out.println(name + " logged in as Administrator.");
    }

    @Override
    public void logout() {
        System.out.println(name + " (Administrator) logged out.");
    }

    @Override
    public String toString() {
        return "Administrator{id=" + id + ", username='" + username + "', name='" + name + "'}";
    }
}
