package com.mahir.locparc.service;

import com.mahir.locparc.dao.CountryDao;
import com.mahir.locparc.model.address.Country;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryService {

    private final CountryDao countryDao;

    public CountryService(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    public Country isCountryValid(Country country) {
        if (country.getName() == null) return null;

        Optional<Country> existingCountry = countryDao.findByName(country.getName());
        if (existingCountry.isEmpty()) return null;
        return existingCountry.get();
    }

}
