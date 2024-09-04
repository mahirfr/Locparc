package com.mahir.locparc.dao;

import com.mahir.locparc.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemDao extends JpaRepository<Item, Long> {
    @Query("FROM Item as i " +
            "WHERE i.existing = true " +
            "AND i.onMaintenance = false " +
            "AND i.id NOT IN (SELECT i2.id FROM Item as i2 " +
                                "JOIN i2.orders oi " +
                                "WHERE oi.returnDate IS NULL " +
                                "AND oi.approved = true ) ")
    List<Item> findAllAvailableItems();

    @Query("FROM Item as i " +
            "WHERE lower(i.name) LIKE concat('%', :name, '%') " +
            "AND i.existing = true " +
            "AND i.onMaintenance = false " +
            "AND i.id NOT IN (SELECT i2.id FROM Item as i2 " +
                                "JOIN i2.orders oi " +
                                    "WHERE oi.returnDate IS NULL " +
                                    "AND oi.approved = true ) ")
    List<Item> findAvailableItemsByName(@Param("name") String name);

    @Query("FROM Item as i " +
            "WHERE lower(i.name) LIKE concat('%', :name, '%') " +
            "AND i.subCategory.name IN :subCategories " +
            "AND i.existing = true " +
            "AND i.onMaintenance = false " +
            "AND i.id NOT IN (SELECT i2.id FROM Item as i2 " +
                                "JOIN i2.orders oi " +
                                "WHERE oi.returnDate IS NULL " +
                                "AND oi.approved = true ) ")
    List<Item> findAvailableItemsByNameAndSubCategories(
            @Param("name") String name,
            @Param("subCategories") List<String> subCategories);

    @Query("FROM Item as i " +
            "WHERE i.subCategory.name IN :subCategories " +
            "AND i.existing = true " +
            "AND i.onMaintenance = false " +
            "AND i.id NOT IN (SELECT i2.id FROM Item as i2 " +
                                "JOIN i2.orders oi " +
                                "WHERE oi.returnDate IS NULL " +
                                "AND oi.approved = true ) ")
    List<Item> findAvailableItemsBySubCategories(
            @Param("subCategories") List<String> subCategories);

    @Query("FROM Item as i " +
            "WHERE i.id = :id " +
            "AND i.existing = true " +
            "AND i.onMaintenance = false " +
            "AND i.id NOT IN (SELECT i2.id FROM Item as i2 " +
                                "JOIN i2.orders oi " +
                                "WHERE oi.returnDate IS NULL " +
                                "AND oi.approved = true )")
    Optional<Item> findAvailableItemById(Long id);

//    TODO: get available items by name and limit the results based on quantity

}
