package com.mahir.locparc.dao;

import com.mahir.locparc.model.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AddressDao extends JpaRepository<Address, Integer> {

    @Query("FROM Address as a " +
            "JOIN Order o ON o.address.id = a.id " +
                "WHERE a = :address")
    Optional<Address> findAddressByOrderAddress(@Param("address") Address address);
}
