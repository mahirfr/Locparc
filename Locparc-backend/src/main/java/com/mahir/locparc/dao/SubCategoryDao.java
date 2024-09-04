package com.mahir.locparc.dao;

import com.mahir.locparc.model.categories.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCategoryDao extends JpaRepository<SubCategory, Integer> {
    @Query("FROM SubCategory as sc " +
            "WHERE sc.name = :name")
    Optional<SubCategory> findByName(@Param("name") String name);
}
