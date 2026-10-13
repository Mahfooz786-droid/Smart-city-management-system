package com.smartcity.database;

import com.smartcity.model.Citizen;

import java.util.List;

public class CitizenService {

    // ==========================================
    // ADD CITIZEN
    // ==========================================

    public void addCitizen(
            String name,
            String email,
            String phone,
            String password,
            String address) {

        CitizenDAO.addCitizen(
                name,
                email,
                phone,
                password,
                address
        );
    }


    // ==========================================
    // GET ALL CITIZENS
    // Collections + Generics
    // ==========================================

    public List<Citizen> getAllCitizens() {

        return CitizenDAO.getAllCitizens();
    }


    // ==========================================
    // UPDATE CITIZEN
    // ==========================================

    public void updateCitizen(
            int citizenId,
            String name,
            String email,
            String phone,
            String address) {

        CitizenDAO.updateCitizen(
                citizenId,
                name,
                email,
                phone,
                address
        );
    }


    // ==========================================
    // DELETE CITIZEN
    // ==========================================

    public void deleteCitizen(
            int citizenId) {

        CitizenDAO.deleteCitizen(
                citizenId
        );
    }
}