package com.mahir.locparc.dao;

import com.mahir.locparc.model.address.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CountryDao extends JpaRepository<Country, Integer> {

    Optional<Country> findByName(@Param("name") String name);

}
