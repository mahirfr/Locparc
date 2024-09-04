package com.mahir.locparc.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.model.address.Address;
import com.mahir.locparc.model.payment.Payment;
import com.mahir.locparc.view.OrderView;
import com.mahir.locparc.view.UserView;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    public Order(Date startDate, Date endDate, Request request) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.request = request;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({OrderView.class})
    private Long id;

//    @CreationTimestamp
    @JsonView({OrderView.class})
    private Date startDate;

    @JsonView({OrderView.class})
    private Date endDate;

    @Column(length = 100)
    @JsonView({OrderView.class})
    private String event;

    @ManyToOne
    @JsonView({OrderView.class, UserView.class})
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    @JsonView({OrderView.class})
    private Request request;

    @ManyToOne
    @JsonView({OrderView.class})
    private Address address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView({OrderView.class})
    private List<OrderItem> orderedItems = new ArrayList<>();


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView({OrderView.class})
    private List<Payment> payment = new ArrayList<>();



}
