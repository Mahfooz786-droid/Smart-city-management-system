package com.smartcity.interfaces;

import com.smartcity.model.CityService;

import java.util.List;

/** Repository contract for {@link CityService} persistence (read-only catalog). */
public interface ServiceRepository {

    /** Returns every city service, ordered by id. */
    List<CityService> findAll();
}
