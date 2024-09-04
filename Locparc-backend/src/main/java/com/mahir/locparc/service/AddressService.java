package com.mahir.locparc.service;

import com.mahir.locparc.dao.CountryDao;
import com.mahir.locparc.model.address.Address;
import com.mahir.locparc.model.address.Country;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;

@Service
public class AddressService {

    private final CountryDao countryDao;
    private final CountryService countryService;

    public AddressService(CountryDao countryDao, CountryService countryService) {
        this.countryDao = countryDao;
        this.countryService = countryService;
    }

    public Address constructAddress(Address address) {
        if (!isAddressValid(address))
            throw new InvalidParameterException("Adresse invalide");

        Country country = countryService.isCountryValid(address.getCountry());
        if (country == null) return null;

        if (address.getAddressDetails() == null)
            address.setAddressDetails("");

        return new Address(
                address.getStreet(),
                address.getStreetNumber(),
                address.getAddressDetails(),
                address.getCity(),
                address.getPostalCode(),
                country);
    }

    public boolean isAddressValid(Address address) {
        if (address                   == null ||
            address.getStreetNumber() == null ||
            address.getStreet()       == null ||
            address.getCity()         == null ||
            address.getPostalCode()   == null ||
            address.getCountry()      == null
        ) return false;

        return true;
    }
}
