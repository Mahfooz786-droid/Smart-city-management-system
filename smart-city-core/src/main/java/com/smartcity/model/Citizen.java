package com.smartcity.model;

import java.util.Objects;

/**
 * A registered Smart City citizen.
 *
 * <p>Preserves the original {@code Citizen extends Person} inheritance
 * relationship and overrides {@link #login()}, {@link #logout()},
 * {@link #showRole()} for runtime polymorphism. {@link #toString()} is
 * overridden for friendly logging.
 *
 * <p>Encapsulation: phone / address are private with public getters/setters.
 * The password is also exposed via {@link #getPassword()} because the DAO
 * layer must read it from the DB to verify against a BCrypt hash; however,
 * no public API ever returns the password in plaintext to the UI layer.
 */
public class Citizen extends Person {

    private String phone;
    private String passwordHash;
    private String address;

    public Citizen() {
        super();
    }

    /**
     * Full constructor used by the JDBC row mapper.
     *
     * @param id            database primary key
     * @param name          full name
     * @param email         unique email
     * @param phone         10-digit phone
     * @param passwordHash  BCrypt hash (never the plaintext password)
     * @param address       residential address
     */
    public Citizen(int id, String name, String email, String phone, String passwordHash, String address) {
        super(id, name, email);
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.address = address;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    /** Returns the stored BCrypt hash. Never log this value. */
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public void showRole() {
        System.out.println(name + " is a Citizen.");
    }

    @Override
    public void login() {
        System.out.println(name + " logged in as Citizen.");
    }

    @Override
    public void logout() {
        System.out.println(name + " logged out.");
    }

    @Override
    public String toString() {
        return "Citizen{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Citizen c)) return false;
        return id == c.id && Objects.equals(email, c.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
