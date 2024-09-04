package com.mahir.locparc.controller;

import com.mahir.locparc.dao.CountryDao;
import com.mahir.locparc.model.address.Country;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/countries")
@CrossOrigin
public class CountryController {

    private final CountryDao countryDao;

    public CountryController(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    @GetMapping("/")
    public ResponseEntity<List<Country>> getAllCountries() {
        List<Country> countries = countryDao.findAll();
        if (!countries.isEmpty())
            return new ResponseEntity<>(countries, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Country> getCountryByName(@PathVariable String name) {
        Optional<Country> country = countryDao.findByName(name);
        if (country.isPresent())
            return new ResponseEntity<>(country.get(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
