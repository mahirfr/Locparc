package com.mahir.locparc.dao;

import com.mahir.locparc.model.manufacturers.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ModelDao extends JpaRepository<Model, Long> {
    @Query("FROM Model as m " +
            "WHERE m.reference = :reference")
    Optional<Model> findByReference(String reference);
}
