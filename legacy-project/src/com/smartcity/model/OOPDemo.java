package com.smartcity.model;

public class OOPDemo {

    public static void main(String[] args) {

        // Parent class reference
        Person person = new Citizen(
                1,
                "Mahfooz",
                "mahfooz@gmail.com",
                "9876543210",
                "1234",
                "Ayodhya"
        );

        // Runtime Polymorphism
        person.showRole();

        person.login();

        person.logout();
    }
}