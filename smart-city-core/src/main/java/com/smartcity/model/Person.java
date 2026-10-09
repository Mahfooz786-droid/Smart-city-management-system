package com.smartcity.model;

/**
 * Abstract base class for every "person" entity in the Smart City system
 * (citizens, and any future administrators / staff).
 *
 * <p>Encapsulation: the {@code id}, {@code name}, {@code email} fields are
 * {@code protected} so concrete subclasses in the same package can read
 * them directly, but external callers must go through the getters/setters
 * which can later gain validation without breaking callers.
 *
 * <p>Runtime polymorphism: subclasses override {@link #login()},
 * {@link #logout()} and provide a concrete {@link #showRole()}.
 */
public abstract class Person implements UserOperations {

    protected int id;
    protected String name;
    protected String email;

    protected Person() {
        // Required for frameworks that instantiate via reflection (JDBC row mappers).
    }

    protected Person(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public void login() {
        // Default impl; subclasses override for role-specific messaging.
        System.out.println(name + " logged in.");
    }

    @Override
    public void logout() {
        System.out.println(name + " logged out.");
    }
}
