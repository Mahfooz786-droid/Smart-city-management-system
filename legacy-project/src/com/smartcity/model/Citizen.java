package com.smartcity.model;

public class Citizen extends Person {

    private int citizenId;
    private String phone;
    private String password;
    private String address;

    public Citizen() {
        super();
    }

    public Citizen(int citizenId, String name, String email,
                   String phone, String password, String address) {

        super(citizenId, name, email);

        this.citizenId = citizenId;
        this.phone = phone;
        this.password = password;
        this.address = address;
    }

    public int getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(int citizenId) {
        this.citizenId = citizenId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Polymorphism
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
                "citizenId=" + citizenId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}