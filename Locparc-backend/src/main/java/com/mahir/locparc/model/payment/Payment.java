package com.mahir.locparc.model.payment;

import com.mahir.locparc.model.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double amount;

    @Column(length = 100)
    private String details;

    @ManyToOne
    private PaymentType paymentType;

    @ManyToOne
    private Order order;

}
