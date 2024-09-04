package com.mahir.locparc.dao;

import com.mahir.locparc.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDao extends JpaRepository<Order, Long> {

    @Query("FROM Order as o " +
            "JOIN o.request as os " +
                "WHERE os.responseDate IS NULL " +
                "AND os.approved = false " +
                    "ORDER BY o.startDate")
    List<Order> getPendingOrders();


//    TODO: add queries for users' orders
    @Query("FROM Order as o " +
            "JOIN o.request as ore " +
            "JOIN o.user as u " +
                "WHERE ore.responseDate IS NULL " +
                "AND u.email = :email " +
                "AND ore.approved = false " +
                    "ORDER BY o.startDate")
    List<Order> getPendingOrdersByEmail(@Param("email") String email);

    @Query("FROM Order as o " +
            "JOIN o.request as ore " +
            "JOIN o.user as u " +
                "WHERE ore.responseDate IS NOT NULL " +
                    "AND u.email = :email " +
                    "AND ore.approved = true " +
                        "ORDER BY o.startDate")
    List<Order> getApprovedOrderHistoryByEmail(@Param("email") String email);

//    TODO: add queries for order lookup by date
    @Query("FROM Order as o " +
            "JOIN o.request as ore " +
            "JOIN o.user as u " +
                "WHERE lower(u.firstName) LIKE concat('%', :name, '%') " +
                "OR    lower(u.lastName)  LIKE concat('%', :name, '%')" +
                    "ORDER BY o.startDate")
    List<Order> getOrderHistoryByFirstOrLastName(@Param("name") String name);


}
