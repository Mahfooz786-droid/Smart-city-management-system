package com.smartcity.model;

import java.util.ArrayList;
import java.util.List;

public class CitizenCollectionDemo {

    public static void main(String[] args) {

        // Generic Collection
        List<Citizen> citizens = new ArrayList<>();

        // Adding Citizen objects
        citizens.add(new Citizen(
                1,
                "Mahfooz",
                "mahfooz@gmail.com",
                "9876543210",
                "1234",
                "Ayodhya"
        ));

        citizens.add(new Citizen(
                2,
                "Rahul",
                "rahul@gmail.com",
                "9876543211",
                "5678",
                "Greater Noida"
        ));

        citizens.add(new Citizen(
                3,
                "Aman",
                "aman@gmail.com",
                "9876543212",
                "9012",
                "Delhi"
        ));

        // Display all citizens
        System.out.println("===== CITIZEN LIST =====");

        for (Citizen citizen : citizens) {
            System.out.println(citizen);
        }

        // Collection size
        System.out.println("\nTotal Citizens: " + citizens.size());
    }
}