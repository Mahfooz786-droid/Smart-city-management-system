package com.smartcity.service;

import com.smartcity.interfaces.ServiceRepository;
import com.smartcity.model.CityService;

import java.util.List;
import java.util.Objects;

/** Read-only service for the city services catalog. */
public class CityServiceService {

    private final ServiceRepository services;

    public CityServiceService(ServiceRepository services) {
        this.services = Objects.requireNonNull(services);
    }

    public List<CityService> getAllServices() {
        return services.findAll();
    }
}
