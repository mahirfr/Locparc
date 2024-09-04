package com.mahir.locparc.dao;

import com.mahir.locparc.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemDao extends JpaRepository<OrderItem, OrderItem.OrderItemsPK> {

    Optional<OrderItem> findByOrderIdAndAndItemId(Long orderId, Long itemId);

}
